package services;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
// import javax.sound.sampled.*;


public class User_old{

    private String name;
    private String password;
    private boolean isAdmin = false;
    private boolean isLoggedIn = false;
    private Scheduler scheduler;
    private String overdoseAlertDirectory = "src/main/resources/medicications";
    private String historyDirectory = "src/main/resources/medication_history";
    private List<String> medicineIntakeHistory = new ArrayList<>();

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

    public User_old(String name, String password) {
        this.name = name;
        this.password = password;
        // Initialize scheduler or other components if needed
    }

    public boolean checkIfUserExists() {
        // Check if user exists
        boolean userFound = false;
        try {
            // Ensure parent directory exists
            File userDir = new File("src/main/resources/users");
            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            File userFile = new File(userDir, "users.csv");
            if (!userFile.exists()) {
                userFile.createNewFile();
            }
            Scanner userFileScanner = new Scanner(userFile);

            while (userFileScanner.hasNextLine()) {
                String line = userFileScanner.nextLine();
                String[] userDetails = line.split(",");
                if (userDetails.length >= 3) {
                    String existingName = userDetails[0];
                    String existingPassword = userDetails[1];
                    String existingPrivilege = userDetails[2];
                    if (existingName.equals(this.name)) {
                        userFound = true;
                        if (existingPassword.equals(this.password)) {
                            System.out.println("User authenticated successfully.");
                            isLoggedIn = true;
                            isAdmin = existingPrivilege.equalsIgnoreCase("admin");
                        } else {
                            System.out.println("Incorrect password.");
                            System.exit(0);
                        }
                        break;
                    }
                }
            }

            userFileScanner.close();
            if (!userFound) {
                System.out.println("User not found. Creating new user.");
                userCreate();
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
                userCreate();
            } catch (IOException ex) {
                System.out.println("An error occurred while creating the user file.");
                ex.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return userFound;
    }

    public void userCreate() {
        // Create a new user
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
//                        scheduler.viewHistory();
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
//                        scheduler.viewHistory();
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
            File listsDir = new File("src/main/resources/lists");
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

            File medFile = new File("src/main/resources/lists/" + patientName + "_medicines.csv");
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

            File medFile = new File("src/main/resources/lists/" + patientName + "_medicines.csv");
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

    private void manageAllUsersMedicines(Scanner scanner) {
        System.out.println("\nAll Users' Medicine Management:");
        System.out.println("1. View modules.User's Medicines");
        System.out.println("2. Add Medicine to modules.User");
        System.out.println("3. Remove Medicine from modules.User");
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

//    public void setOverdoseAlertDirectory(String directory, Medicine medicine) {
//            OverdosePrevention.alertOverdose(medicine);
//    }

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
//                setOverdoseAlertDirectory("default/directory", med);
                // Prepare to send the email (currently creating an empty file as a placeholder)
                prepareOverdoseAlert(med);
//                OverdosePrevention overdosePrevention = new OverdosePrevention();
//                overdosePrevention.alertOverdose(med);
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

    private void timeToTakeMedicine(Scanner scanner) {
        try {
            // Ensure the output directory exists
            File directory = new File("src/main/resources");
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
            File medFile = new File("src/main/resources/medications" + name + "_medicines.csv");
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
            File medFile = new File("src/main/resources/lists/" + name + "_medicines.csv");
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