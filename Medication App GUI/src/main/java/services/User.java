package services;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class User {

    private String name;
    private String password;
    private boolean isAdmin = false;
    private boolean isLoggedIn = false;

    // Added fields for password attempt tracking
    private int failedAttempts = 0;
    private long firstFailedTimestamp = 0L;
    private long lockoutEndTimestamp = 0L;

    private final String overdoseAlertDirectory = "src/main/resources/medications";
    private final String historyDirectory = "src/main/resources/medication_history";
    private final List<String> medicineIntakeHistory = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    /**
     * Checks if the user exists by searching in the users.csv file.
     * If the user fails to log in three times within ten minutes, the account is
     * locked for five minutes.
     * If the user does not exist, it will be automatically created.
     */

    public boolean checkIfUserExists() {
        boolean userFound = false;
        boolean userExists = false;
        try {
            // Ensure users directory exists
            File userDir = new File("src/main/resources/users");
            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            File userFile = new File(userDir, "users.csv");
            if (!userFile.exists()) {
                // If users.csv doesn't exist, create it
                userFile.createNewFile();
            }

            // Read all users from the CSV file
            List<String[]> userList = new ArrayList<>();
            Scanner userFileScanner = new Scanner(userFile);

            while (userFileScanner.hasNextLine()) {
                String line = userFileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 3) {
                    String existingName = parts[0].trim();
                    String existingPassword = parts[1].trim();
                    String role = parts[2].trim();

                    // Retrieve lockout information if available
                    int failedAttempts = parts.length >= 4 ? Integer.parseInt(parts[3].trim()) : 0;
                    long firstFailedTimestamp = parts.length >= 5 ? Long.parseLong(parts[4].trim()) : 0L;
                    long lockoutEndTimestamp = parts.length >= 6 ? Long.parseLong(parts[5].trim()) : 0L;

                    if (existingName.equals(this.name)) {
                        userExists = true;
                        long currentTime = System.currentTimeMillis();

                        // Check if account is locked
                        if (lockoutEndTimestamp > currentTime) {
                            JOptionPane.showMessageDialog(null,
                                    "Your account has been locked, please try again later.",
                                    "account lockout",
                                    JOptionPane.WARNING_MESSAGE);
                            userFound = false;
                        } else {
                            // Reset lockout if time has passed
                            if (lockoutEndTimestamp != 0L) {
                                failedAttempts = 0;
                                firstFailedTimestamp = 0L;
                                lockoutEndTimestamp = 0L;
                            }

                            if (existingPassword.equals(this.password)) {
                                // Successful login
                                userFound = true;
                                if (role.equalsIgnoreCase("admin")) {
                                    isAdmin = true;
                                }
                                isLoggedIn = true;

                                // Reset failed attempts
                                failedAttempts = 0;
                                firstFailedTimestamp = 0L;
                                lockoutEndTimestamp = 0L;
                            } else {
                                // Incorrect password
                                if (failedAttempts == 0) {
                                    firstFailedTimestamp = currentTime;
                                }
                                failedAttempts++;

                                if (failedAttempts >= 3 && (currentTime - firstFailedTimestamp <= 10 * 60 * 1000)) {
                                    lockoutEndTimestamp = currentTime + 5 * 60 * 1000;
                                    JOptionPane.showMessageDialog(null,
                                            "Too many failures and your account has been locked for 5 minutes.",
                                            "account lockout",
                                            JOptionPane.ERROR_MESSAGE);
                                } else if (currentTime - firstFailedTimestamp > 10 * 60 * 1000) {
                                    // Reset failed attempts after 10 minutes
                                    failedAttempts = 1;
                                    firstFailedTimestamp = currentTime;
                                    JOptionPane.showMessageDialog(null,
                                            "Password error. You have 1 failed attempt.",
                                            "Login Failure",
                                            JOptionPane.ERROR_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "Password error. You have failed " + failedAttempts + " Times。",
                                            "Login Failure",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }

                    // Update user information
                    userList.add(new String[] {
                            existingName,
                            existingPassword,
                            role,
                            String.valueOf(failedAttempts),
                            String.valueOf(firstFailedTimestamp),
                            String.valueOf(lockoutEndTimestamp)
                    });
                }
            }

            userFileScanner.close();

            if (!userExists) {
                // User does not exist, create it automatically
                userCreate();
                userFound = true;
            } else {
                // Write updated user information back to the CSV file
                BufferedWriter bw = new BufferedWriter(new FileWriter(userFile));
                for (String[] userInfo : userList) {
                    bw.write(String.join(",", userInfo));
                    bw.newLine();
                }
                bw.close();
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "The user file could not be found.",
                    "documentation error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred while accessing a user file.",
                    "documentation error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return userFound;
    }

    /**
     * Creates a new user and writes to users.csv.
     */
    public void userCreate() {
        try {
            File userDir = new File("src/main/resources/users");
            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            File userFile = new File(userDir, "users.csv");
            FileWriter fw = new FileWriter(userFile, true);
            BufferedWriter bw = new BufferedWriter(fw);

            // New users are added with default lockout parameters
            bw.write(name + "," + password + "," + "user" + ",0,0,0\n");
            bw.close();

            JOptionPane.showMessageDialog(null,
                    "A new user has been created and logged in.",
                    "User Creation",
                    JOptionPane.INFORMATION_MESSAGE);
            isLoggedIn = true;
            isAdmin = false;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Create new medication file for user
        try {
            File medicationDir = new File("src/main/resources/medications");
            if (!medicationDir.exists()) {
                medicationDir.mkdirs();
            }

            File userMedFile = new File(medicationDir, name + "_medications.csv");
            if (!userMedFile.exists()) {
                userMedFile.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred while creating a drug file",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}