// HistoryTracker.java
package modules;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryTracker {
    private List<String> history; // Stores history logs
    private String historyFilePath;

    public HistoryTracker(String historyDirectory, String userName) {
        history = new ArrayList<>();
        historyFilePath = Paths.get(historyDirectory, userName + "_history.txt").toString();
        loadHistory();
    }

    // Log when a medicine is taken
    public void logTakenMedicine(Medicine medicine) {
        String log = "Medicine taken: " + medicine.getName() + " at " + java.time.LocalDateTime.now().toString();
        history.add(log);
        saveHistory();
    }

    // Log missed dose
    public void logMissedDose(Medicine medicine) {
        String log = "Missed dose: " + medicine.getName() + " at " + java.time.LocalDateTime.now().toString();
        history.add(log);
        saveHistory();
    }

    // Log when a reminder is sent
    public void logReminder(Medicine medicine) {
        String log = "Reminder sent for: " + medicine.getName() + " at " + java.time.LocalDateTime.now().toString();
        history.add(log);
        saveHistory();
    }

    // Display the history log
    public void displayHistory() {
        System.out.println("\n--- History Log ---");
        for (String record : history) {
            System.out.println(record);
        }
    }

    // Save history to file
    private void saveHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFilePath, true))) {
            writer.write(history.get(history.size() - 1));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }

    // Load history from file
    private void loadHistory() {
        Path path = Paths.get(historyFilePath);
        if (Files.exists(path)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(historyFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    history.add(line);
                }
            } catch (IOException e) {
                System.out.println("Error loading history: " + e.getMessage());
            }
        }
    }
}