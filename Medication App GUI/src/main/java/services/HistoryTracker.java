package services;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class HistoryTracker {

    private User currentUser;

    // Map to store daily dosage limits for each medication
    private Map<String, Integer> medicationDailyLimits = new HashMap<>();

    // Map to store taken counts for each medication today
    private Map<String, Integer> medicationTakenCounts = new HashMap<>();

    // Directory paths
    private static final String MEDICATIONS_DIR = "src/main/resources/medications/";
    private static final String TAKEN_RECORDS_DIR = "src/main/resources/taken_records/";

    public HistoryTracker(User user) {
        this.currentUser = user;
        loadMedicationLimits();
        loadTakenRecords();
    }

    public void loadMedicationLimits() {
        String userMedicationFilePath = MEDICATIONS_DIR + currentUser.getName() + "_medications.csv";
        File medicationFile = new File(userMedicationFilePath);
        if (!medicationFile.exists()) {
            System.out.println("Medication file not found for user: " + currentUser.getName());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(medicationFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] medInfo = line.split(",");
                if (medInfo.length >= 2) {
                    String medName = medInfo[0].trim();
                    // get the last column as dailyLimit
                    int dailyLimit = Integer.parseInt(medInfo[medInfo.length - 1].trim());
                    medicationDailyLimits.put(medName, dailyLimit);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read medication file!");
            throw new RuntimeException(e);
        }
    }

    public void loadTakenRecords() {
        String takenRecordsFilePath = TAKEN_RECORDS_DIR + currentUser.getName() + "_taken_records.csv";
        File file = new File(takenRecordsFilePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create taken records file!");
                throw new RuntimeException(e);
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(takenRecordsFilePath))) {
            String line;
            LocalDate today = LocalDate.now();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String medName = parts[0].trim();
                    String date = parts[1].trim();
                    int count = Integer.parseInt(parts[2].trim());
                    if (date.equals(today.toString())) {
                        medicationTakenCounts.put(medName, count);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read taken records file!");
            throw new RuntimeException(e);
        }
    }

    public void saveTakenRecord(String medicationName) {
        String takenRecordsFilePath = TAKEN_RECORDS_DIR + currentUser.getName() + "_taken_records.csv";
        LocalDate today = LocalDate.now();
        int currentCount = medicationTakenCounts.getOrDefault(medicationName, 0) + 1;
        medicationTakenCounts.put(medicationName, currentCount);

        // Rewrite the entire file to update the number of pickups
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(takenRecordsFilePath))) {
            for (Map.Entry<String, Integer> entry : medicationTakenCounts.entrySet()) {
                writer.write(entry.getKey() + "," + today.toString() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Could not write to taken records file!");
            throw new RuntimeException(e);
        }
    }

    public boolean isExceeded(String medicationName) {
        int takenCount = medicationTakenCounts.getOrDefault(medicationName, 0);
        int dailyLimit = medicationDailyLimits.getOrDefault(medicationName, Integer.MAX_VALUE);
        return takenCount >= dailyLimit;
    }

    public int getDailyLimit(String medicationName) {
        return medicationDailyLimits.getOrDefault(medicationName, Integer.MAX_VALUE);
    }

    public int getTakenCount(String medicationName) {
        return medicationTakenCounts.getOrDefault(medicationName, 0);
    }

    public Map<String, Integer> getMedicationDailyLimits() {
        return medicationDailyLimits;
    }

    public Font loadFont() {
        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/main/resources/fonts/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);
        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            throw new RuntimeException(e);
        }
        return applicationFont;
    }

    public void reloadMedicationData() {
        loadMedicationLimits();
        loadTakenRecords();
    }

}