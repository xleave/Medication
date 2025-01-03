import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
// import javax.sound.sampled.*;

import modules.*;

import javax.swing.*;

public class User {

    private String name;
    private String password;
    private boolean isAdmin = false;
    private boolean isLoggedIn = false;
    private Scheduler scheduler;
    private String overdoseAlertDirectory = "Medication App/src/resources/medicications"; 
    private String historyDirectory = "Medication App/src/resources/medication_history"; 
    private List<String> medicineIntakeHistory = new ArrayList<>(); 

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.scheduler = new Scheduler(name, historyDirectory);
//        // Read users.csv to see if entry for user already exists.
    }


    public void checkIfUserExists() {
        // Read users.csv to see if entry for user already exists.
        System.out.println("Please enter your details");
        System.out.print("Name: ");
        String temporaryName = name;
        String temporaryPassword = password;

        boolean userFound = false;

        try {
            // Ensure the parent directory exists
            File userDir = new File("Medication App/src/resources/users");
            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            File userFile = new File(userDir, "users.csv");
            System.out.println("Using file path: " + userFile.getAbsolutePath());
            if (!userFile.exists()) {
                System.out.println("User file does not exist. Creating a new one.");
                userFile.createNewFile();
            }
            Scanner userFileScanner = new Scanner(userFile);
            System.out.println("Current users and passwords in the file:");   //this is just for beta testing
            while (userFileScanner.hasNextLine()) {
                String line = userFileScanner.nextLine();
                String[] data = line.split(",");
                if (data.length >= 2) {
                    System.out.println("User: " + data[0] + ", Password: " + data[1]);
                }
            }

            userFileScanner = new Scanner(userFile);
            while (userFileScanner.hasNextLine()) {
                String line = userFileScanner.nextLine();
                String[] data = line.split(",");

                if (data.length >= 2 && data[0].equalsIgnoreCase(temporaryName)) {
                    // If user exists, load password and log in
                    userFound = true;
                    if (temporaryPassword.equals(data[1])) {
                        JOptionPane.showMessageDialog(null, "Login successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        this.name = temporaryName;
                        this.isLoggedIn = true;
                        userFileScanner.close();
                        loadMedicines();  // Load user's medicines
                        return;
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        userFileScanner.close();
                        //checkIfUserExists();  // Retry login
                        return;
                    }
                }
            }

            userFileScanner.close();
            if (!userFound) {
                System.out.println("User not found. Please create a new account.");
                // Prompt the user to create a new account
                userCreate();
            }
        } catch (FileNotFoundException e) {
            System.out.println("The user file could not be found! Program will now close.");
            e.printStackTrace();
            System.exit(2);
        } catch (IOException e) {
            System.out.println("An error occurred while accessing the user file.");
            e.printStackTrace();
        }
    }

    public void userCreate() {
        // Get user input from LoginGUI (assuming `name` and `password` have been set)
        String newName = name;  // Get the name from the LoginGUI input field
        String newPassword = password;  // Get the password from the LoginGUI input field

        // Ensure the parent directory exists
        try {
            File userDir = new File("Medication App/src/resources/users");
            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            // Open the users.csv file in append mode and write the new user data
            FileWriter fw = new FileWriter(new File(userDir, "users.csv"), true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(newName + "," + newPassword + ",user");  // Add new user to the CSV file
            out.close();
            bw.close();
            fw.close();

            JOptionPane.showMessageDialog(null, "Account created successfully. Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Now login with the new user
            checkIfUserExists();  // Check if user can log in with new account
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error creating account.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Optionally, create a new medication file for the user in the 'lists' directory
        try {
            File listsDir = new File("Medication App/src/resources/lists");
            if (!listsDir.exists()) {
                listsDir.mkdirs();
            }

            // Create a new medication file for the user
            File medFile = new File(listsDir, newName + "_medicines.csv");
            if (medFile.createNewFile()) {
                JOptionPane.showMessageDialog(null, "Medication file created for user.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while creating the medication file.", "Error", JOptionPane.ERROR_MESSAGE);
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
            System.out.println("5. View History");
            System.out.println("6. Time to Take Medicine");
            if (isAdmin) {
                System.out.println("6. Manage All Users' Medicines");
                System.out.println("7. Logout");
            } else {
                System.out.println("7. Logout");
            }
            System.out.print("Choose an option: ");
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
                        takeMedicine(scanner);
                        break;
                    case "5":
                        scheduler.viewHistory();
                        break;
                    case "6":
                        manageAllUsersMedicines(scanner);
                        break;
                    case "7":
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
                        takeMedicine(scanner);
                        break;
                    case "5":
                        scheduler.viewHistory();
                        break;
                    case "6":
                        timeToTakeMedicine(scanner);
                        break;
                    case "7":
                        isLoggedIn = false;
                        System.out.println("Logged out successfully.");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }


            // Check reminders
            scheduler.checkReminders();
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
            System.out.print("Time to Take (HH:mm): ");
            String timeToTake = scanner.next();
            System.out.print("Daily Frequency: ");
            int dailyFrequency = Integer.parseInt(scanner.next());
            System.out.print("Max Daily Dose: ");
            int maxDailyDose = Integer.parseInt(scanner.next());

            Medicine med = new Medicine(medName, dosage, quantity, timeToTake, patientName, dailyFrequency,
                    maxDailyDose);
            scheduler.addMedicine(med);

            // Ensure parent directory exists
            File listsDir = new File("Medication App/src/resources/lists");
            if (!listsDir.exists()) {
                listsDir.mkdirs();
            }

            FileWriter fw = new FileWriter(new File(listsDir, patientName + "_medicines.csv"), true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(med.getName() + "," + med.getDosage() + "," + med.getQuantity() + "," + med.getTimeToTake()
                    + "," + med.getUserName() + "," + med.getDailyFrequency() + "," + med.getMaxDailyDose());
            out.close();
            bw.close();
            fw.close();
            System.out.println("Medicine added successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while adding medicine.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers.");
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
                if (data.length >= 7) {
                    System.out.println("Name: " + data[0] + ", Dosage: " + data[1] + ", Quantity: " + data[2]
                            + ", Time: " + data[3] + ", Daily Frequency: " + data[5] + ", Max Daily Dose: " + data[6]);
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
    private void timeToTakeMedicine(Scanner scanner) {
        try {
            // Ensure the output directory exists
            File directory = new File("src/resources/time");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Open the file in append mode
            FileWriter fw = new FileWriter(new File(directory, name + " TimeToTakeMedicine.csv"), true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);


            System.out.print("Enter the name of the medicine you are taking: ");
            String medName = scanner.next();

            // Check if the user's medicine CSV exists
            File medFile = new File("Medication App/src/resources/lists/" + name + "_medicines.csv");
            if (!medFile.exists()) {
                System.out.println("No medicines found for user.");
                return;
            }

            // Find the medicine in the user's medicine list
            Scanner fileScanner = new Scanner(medFile);
            boolean medicineFound = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] data = line.split(",");

                if (data[0].equalsIgnoreCase(medName)) {
                    medicineFound = true;

                    System.out.print("Enter the times you need to take the medicine (comma-separated, e.g., 10:00,12:00 in 24HR): ");
                    String timesInput = scanner.next();
                    String[] times = timesInput.split(",");

                    // Append the medicine name and times to the CSV file
                    out.print(data[0]);
                    for (String time : times) {
                        out.print("," + time); // Add each time as a separate column
                    }
                    out.println(); // Move to the next row
                    System.out.println("Recorded times for taking " + data[0] + ": " + timesInput);
                    break;
                }
            }

            fileScanner.close();
            out.close();
            bw.close();
            fw.close();

            if (!medicineFound) {
                System.out.println("Medicine \"" + medName + "\" not found in your medication database.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while processing the medicine times.");
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

    public void setOverdoseAlertDirectory(String directory) {
        this.overdoseAlertDirectory = directory;
    }

    public void setHistoryDirectory(String directory) {
        this.historyDirectory = directory;
    }

    private void takeMedicine(Scanner scanner) {
        System.out.print("Enter the name of the medicine you are taking: ");
        String medName = scanner.next();
        // Find the medicine in the scheduler
        Medicine med = scheduler.getMedicineByName(medName);
        if (med != null) {
            // Before labelling medication, check for overdose if another dose is taken
            if (med.willOverDose()) {
                System.out.println("Warning: Taking this medicine now will exceed the maximum daily dose.");
                System.out.println("You are not allowed to take more of this medicine today.");
                // Prepare to send the email (currently creating an empty file as a placeholder)
                prepareOverdoseAlert(med);
            } else {
                scheduler.takeMedicine(med);
                System.out.println("Medicine " + medName + " has been marked as taken.");

                // Record medication history
                logMedicineIntake(med);
            }
        } else {
            System.out.println("Medicine not found in your schedule.");
        }
    }

    private void prepareOverdoseAlert(Medicine medicine) {
        try {
            // Make sure the output directory exists
            File directory = new File(overdoseAlertDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create an empty file with the name of the drug and the current time.
            String filename = "overdose_alert_" + medicine.getName() + "_"
                    + LocalDateTime.now().toString().replace(":", "-") + ".txt";
            File alertFile = new File(directory, filename);
            if (alertFile.createNewFile()) {
                System.out.println("Overdose alert prepared at " + alertFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while preparing overdose alert.");
            e.printStackTrace();
        }
    }

    private void logMedicineIntake(Medicine medicine) {
        try {
            // Make sure the history directory exists
            File directory = new File(historyDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create or open a user's history file (named after the user name)
            File historyFile = new File(directory, name + "_history.csv");
            FileWriter fw = new FileWriter(historyFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            // Record format: time, drug name, dosage, quantity.
            String record = LocalDateTime.now() + "," + medicine.getName() + "," + medicine.getDosage() + ","
                    + medicine.getQuantity();
            out.println(record);

            // Add records to the in-memory history list
            medicineIntakeHistory.add(record);

            out.close();
            bw.close();
            fw.close();
            System.out.println("Medicine intake logged.");
        } catch (IOException e) {
            System.out.println("An error occurred while logging medicine intake.");
            e.printStackTrace();
        }
    }

    private void loadMedicineIntakeHistory() {
        try {
            File historyFile = new File(historyDirectory, name + "_history.csv");
            if (!historyFile.exists()) {
                System.out.println("No medicine intake history found for user.");
                return;
            }
            Scanner fileScanner = new Scanner(historyFile);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                medicineIntakeHistory.add(line);
            }
            fileScanner.close();
            System.out.println("Medicine intake history loaded.");
        } catch (IOException e) {
            System.out.println("An error occurred while loading medicine intake history.");
            e.printStackTrace();
        }
    }

    private void viewMedicineIntakeHistory() {
        if (medicineIntakeHistory.isEmpty()) {
            System.out.println("No medicine intake history to display.");
        } else {
            System.out.println("Medicine Intake History:");
            for (String record : medicineIntakeHistory) {
                System.out.println(record);
            }
        }
    }

    // Load medicines from file into scheduler
    private void loadMedicines() {
        try {
            File medFile = new File("Medication App/src/resources/lists/" + name + "_medicines.csv");
            if (!medFile.exists()) {
                System.out.println("No medicines found for user.");
                return;
            }
            Scanner fileScanner = new Scanner(medFile);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] data = line.split(",");
                if (data.length >= 7) {
                    Medicine med = new Medicine(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4],
                            Integer.parseInt(data[5]), Integer.parseInt(data[6]));
                    scheduler.addMedicine(med);
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while loading medicines.");
            e.printStackTrace();
        }
    }

    // // Use Windows built-in speech to remind
    // private void speak(String text) {
    // try {
    // String command = "PowerShell -Command \"Add-Type –AssemblyName System.Speech;
    // " +
    // "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
    // "$speak.Speak('" + text + "');\"";
    // Runtime.getRuntime().exec(command);
    // } catch (IOException e) {
    // System.out.println("An error occurred while attempting to speak.");
    // e.printStackTrace();
    // }
    // }

}