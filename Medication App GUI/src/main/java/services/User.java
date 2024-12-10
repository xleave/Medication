package services;

import java.io.*;
import java.util.Scanner;

public class User {

    private String name;
    private String password;
    private boolean isAdmin = false;
    private boolean isLoggedIn = false;
    private final String overdoseAlertDirectory = "src/main/resources/medications";
    private final String historyDirectory = "src/main/resources/medication_history";
    private final java.util.List<String> medicineIntakeHistory = new java.util.ArrayList<>();

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        // Initialize scheduler or other components if needed
    }

    /**
     * Checks if the user exists by searching in the users.csv file.
     * 
     * @return true if user exists and password matches, false otherwise
     */
    public boolean checkIfUserExists() {
        boolean userFound = false;
        try {
            // Ensure parent directory exists
            File userDir = new File("src/main/resources/users");
            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            File userFile = new File(userDir, "users.csv");
            if (!userFile.exists()) {
                // If users.csv doesn't exist, create it
                userFile.createNewFile();
                return false;
            }

            Scanner userFileScanner = new Scanner(userFile);

            while (userFileScanner.hasNextLine()) {
                String line = userFileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String existingName = parts[0].trim();
                    String existingPassword = parts[1].trim();
                    String role = parts[2].trim();

                    if (existingName.equals(this.name) && existingPassword.equals(this.password)) {
                        userFound = true;
                        if (role.equalsIgnoreCase("admin")) {
                            isAdmin = true;
                        }
                        break;
                    }
                }
            }

            userFileScanner.close();

            if (!userFound) {
                // Optionally, you can create a new user or notify that user doesn't exist
                // Here, we'll assume user must exist to login
                return false;
            }

        } catch (FileNotFoundException e) {
            System.out.println("User file not found. Creating user file.");
            try {
                File userDir = new File("src/main/resources/users");
                if (!userDir.exists()) {
                    userDir.mkdirs();
                }

                File userFile = new File(userDir, "users.csv");
                userFile.createNewFile();
            } catch (IOException ex) {
                System.out.println("Failed to create user file.");
                ex.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
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
            // New users are not admin by default
            bw.write(name + "," + password + "," + "user" + "\n");
            bw.close();
            System.out.println("New user created and logged in.");
            isLoggedIn = true;
            isAdmin = false;
        } catch (IOException e) {
            System.out.println("An error occurred.");
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
            System.out.println("An error occurred while creating medication file.");
            e.printStackTrace();
        }
    }

    // Medicine Management Methods

    public void manageMedicines() {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to manage medicines.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMedicine Management:");
            System.out.println("1. Add Medicine");
            System.out.println("2. Remove Medicine");
            System.out.println("3. View Medicines");
            System.out.println("4. Take Medicine");
            System.out.print("Choose an option: ");
            String choice = scanner.next();

            switch (choice) {
                case "1":
                    addMedicine(scanner);
                    break;
                case "2":
                    removeMedicine(scanner);
                    break;
                case "3":
                    viewMedicines();
                    break;
                case "4":
                    takeMedicine(scanner);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addMedicine(Scanner scanner) {
        // Implementation for adding medicine
    }

    private void removeMedicine(Scanner scanner) {
        // Implementation for removing medicine
    }

    private void viewMedicines() {
        // Implementation for viewing medicines
    }

    private void takeMedicine(Scanner scanner) {
        System.out.print("Enter the name of the medicine you are taking: ");
        String medName = scanner.next();
        // Find the medicine in the scheduler
        // Implementation for taking medicine
    }

    // Additional methods can be implemented as needed
}