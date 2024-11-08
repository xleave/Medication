import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import modules.Medicine;

public class User {

    private String name;
    private String password;
    private boolean isAdmin = false;
    private boolean isLoggedIn = false;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public User(String name, String password) {

    }

    public void checkIfUserExists() { // Read users.csv to see if entry for user already exists.
        System.out.println("Please enter your details");
        System.out.print("Name: ");
        Scanner userInputScanner = new Scanner(System.in);
        String temporaryName = userInputScanner.next();

        boolean userFound = false;
//Path references may cause problems, please note that if you get an error, delete the Medication App/ paragraph.
        try {
            File userFile = new File("Medication App/src/resources/users/users.csv");
            if (!userFile.exists()) {
                System.out.println("User file does not exist. Creating a new one.");
                userFile.createNewFile();
            }
            Scanner userFileScanner = new Scanner(userFile);
            while (userFileScanner.hasNextLine()) {
                String line = userFileScanner.nextLine();
                String[] data = line.split(",");

                if (data.length >= 2 && data[0].equalsIgnoreCase(temporaryName)) { // If user exists, load password and
                                                                                   // log in.
                    userFound = true;
                    System.out.println("Hello " + temporaryName + ". Please enter your Password.");
                    System.out.print("Password: ");
                    String temporaryPassword = userInputScanner.next();
                    String passwordInFile = data[1];
                    String role = data.length > 2 ? data[2] : "user";
                    if (temporaryPassword.equals(passwordInFile)) {
                        System.out.println("Login successful.");
                        this.name = temporaryName;
                        this.isAdmin = role.equalsIgnoreCase("admin");
                        this.isLoggedIn = true;
                        userFileScanner.close();
                        return;
                    } else {
                        System.out.println("Incorrect password.");
                        userFileScanner.close();
                        checkIfUserExists(); // Recursive call for re-login
                        return;
                    }
                }
            }
            userFileScanner.close();
            if (!userFound) {
                System.out.println("User not found. Please create a new account.");
                userCreate();
            }
        } catch (FileNotFoundException e) {
            System.out.println("The user file could not be found! Program will now close.");
            System.exit(2);
        } catch (IOException e) {
            System.out.println("An error occurred while accessing the user file.");
            e.printStackTrace();
        }
    }

    public void userCreate() {
        System.out.println("Please enter your details to create your new account.");
        Scanner userInput = new Scanner(System.in);
        System.out.print("Name: ");
        name = userInput.next();
        System.out.print("Password: ");
        password = userInput.next();

        // Write new details to user file.
        try {
            FileWriter fw = new FileWriter("Medication App/src/resources/users/users.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(name + "," + password + ",user");
            out.close();
            bw.close();
            fw.close();
            System.out.println("Account created successfully. Please log in.");
            checkIfUserExists();
        } catch (IOException e) {
            System.out.println("An error occurred while creating the account.");
            e.printStackTrace();
        }

        // Create a new medication file for the user in the 'lists' directory.
        try {
            File listsDir = new File("Medication App/src/resources/lists");
            if (!listsDir.exists()) {
                listsDir.mkdirs();
            }
            File medFile = new File("Medication App/src/resources/lists/" + name + "_medicines.csv");
            if (medFile.createNewFile()) {
                System.out.println("Medication file created for user.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the medication file.");
            e.printStackTrace();
        }
    }

    public void userLogin() {
        System.out.println("Please enter your login credentials to log into your account.");
        Scanner userInput = new Scanner(System.in);
        System.out.print("Name: ");
        name = userInput.next();
        System.out.print("Password: ");
        password = userInput.next();
        // Implement login logic similar to checkIfUserExists
        try {
            File userFile = new File("Medication App/src/resources/users/users.csv");
            Scanner userFileScanner = new Scanner(userFile);
            while (userFileScanner.hasNextLine()) {
                String line = userFileScanner.nextLine();
                String[] data = line.split(",");
                if (data.length >= 2 && data[0].equalsIgnoreCase(name)) {
                    String passwordInFile = data[1];
                    String role = data.length > 2 ? data[2] : "user";
                    if (password.equals(passwordInFile)) {
                        System.out.println("Login successful.");
                        this.isAdmin = role.equalsIgnoreCase("admin");
                        this.isLoggedIn = true;
                        userFileScanner.close();
                        return;
                    } else {
                        System.out.println("Incorrect password.");
                        userFileScanner.close();
                        userLogin(); // Recursive call for re-login
                        return;
                    }
                }
            }
            userFileScanner.close();
            System.out.println("User not found. Please create a new account.");
            userCreate();
        } catch (FileNotFoundException e) {
            System.out.println("The user file could not be found! Program will now close.");
            System.exit(2);
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
            if (isAdmin) {
                System.out.println("4. Manage All Users' Medicines");
                System.out.println("5. Logout");
                System.out.print("Choose an option: ");
            } else {
                System.out.println("4. Logout");
                System.out.print("Choose an option: ");
            }
            String choice = scanner.next();

            if (isAdmin) {
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
                        manageAllUsersMedicines(scanner);
                        break;
                    case "5":
                        isLoggedIn = false;
                        System.out.println("Logged out successfully.");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } else {
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
                        isLoggedIn = false;
                        System.out.println("Logged out successfully.");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    private void addMedicine(Scanner scanner) {
        try {
            String patientName = this.name;
            if (isAdmin) {
                System.out.print("Enter patient's name: ");
                patientName = scanner.next();
            }

            System.out.print("Medicine Name: ");
            String medName = scanner.next();
            System.out.print("Dosage: ");
            String dosage = scanner.next();
            System.out.print("Quantity: ");
            int quantity = Integer.parseInt(scanner.next());
            System.out.print("Time to Take: ");
            String timeToTake = scanner.next();

            Medicine med = new Medicine(medName, dosage, quantity, timeToTake, patientName);
            FileWriter fw = new FileWriter("Medication App/src/resources/lists/" + patientName + "_medicines.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(med.getName() + "," + med.getDosage() + "," + med.getQuantity() + "," + med.getTimeToTake()
                    + "," + med.getUserName());
            out.close();
            bw.close();
            fw.close();
            System.out.println("Medicine added successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while adding medicine.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for quantity. Please enter a valid number.");
        }
    }

    private void removeMedicine(Scanner scanner) {
        try {
            String patientName = this.name;
            if (isAdmin) {
                System.out.print("Enter patient's name: ");
                patientName = scanner.next();
            }

            File medFile = new File("Medication App/src/resources/lists/" + patientName + "_medicines.csv");
            if (!medFile.exists()) {
                System.out.println("No medicines found for user.");
                return;
            }

            List<String> medicines = new ArrayList<>();
            Scanner fileScanner = new Scanner(medFile);
            while (fileScanner.hasNextLine()) {
                medicines.add(fileScanner.nextLine());
            }
            fileScanner.close();

            if (medicines.isEmpty()) {
                System.out.println("No medicines to remove.");
                return;
            }

            System.out.println("Medicines:");
            for (int i = 0; i < medicines.size(); i++) {
                String[] data = medicines.get(i).split(",");
                System.out.println(
                        (i + 1) + ". " + data[0] + " - " + data[1] + " - Qty: " + data[2] + " - Time: " + data[3]);
            }

            System.out.print("Enter the number of the medicine to remove: ");
            int index = Integer.parseInt(scanner.next()) - 1;
            if (index >= 0 && index < medicines.size()) {
                medicines.remove(index);
                FileWriter fw = new FileWriter(medFile, false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw);
                for (String med : medicines) {
                    out.println(med);
                }
                out.close();
                bw.close();
                fw.close();
                System.out.println("Medicine removed successfully.");
            } else {
                System.out.println("Invalid medicine number.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while removing medicine.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

    private void viewMedicines() {
        try {
            String patientName = this.name;
            if (isAdmin) {
                System.out.print("Enter patient's name to view medicines: ");
                Scanner scanner = new Scanner(System.in);
                patientName = scanner.next();
            }

            File medFile = new File("Medication App/src/resources/lists/" + patientName + "_medicines.csv");
            if (!medFile.exists()) {
                System.out.println("No medicines found for user.");
                return;
            }

            Scanner fileScanner = new Scanner(medFile);
            boolean hasMedicines = false;
            System.out.println("\nMedicines for " + patientName + ":");
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] data = line.split(",");
                if (data.length >= 4) {
                    System.out.println("Name: " + data[0] + ", Dosage: " + data[1] + ", Quantity: " + data[2]
                            + ", Time: " + data[3]);
                    hasMedicines = true;
                }
            }
            fileScanner.close();
            if (!hasMedicines) {
                System.out.println("No medicines to display.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while viewing medicines.");
            e.printStackTrace();
        }
    }

    private void manageAllUsersMedicines(Scanner scanner) {
        System.out.println("\nAll Users' Medicine Management:");
        System.out.println("1. View User's Medicines");
        System.out.println("2. Add Medicine to User");
        System.out.println("3. Remove Medicine from User");
        System.out.println("4. Back to Main Menu");
        System.out.print("Choose an option: ");
        String choice = scanner.next();

        switch (choice) {
            case "1":
                viewMedicines();
                break;
            case "2":
                addMedicine(scanner);
                break;
            case "3":
                removeMedicine(scanner);
                break;
            case "4":
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
}