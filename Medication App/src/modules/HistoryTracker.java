// HistoryTracker.java
package modules;

import java.util.ArrayList;
import java.util.List;

public class HistoryTracker {
    private List<String> history; // Stores history logs

    public HistoryTracker() {
        history = new ArrayList<>();
    }

    // Log when a medicine is taken
    public void logTakenMedicine(Medicine medicine) {
        String log = "Medicine taken: " + medicine.getName() + " at " + java.time.LocalDateTime.now().toString();
        history.add(log);
    }

    // Log missed dose
    public void logMissedDose(Medicine medicine) {
        String log = "Missed dose: " + medicine.getName() + " at " + java.time.LocalDateTime.now().toString();
        history.add(log);
    }

    // Log when a reminder is sent
    public void logReminder(Medicine medicine) {
        String log = "Reminder sent for: " + medicine.getName() + " at " + java.time.LocalDateTime.now().toString();
        history.add(log);
    }

    // Display the history log
    public void displayHistory() {
        System.out.println("\n--- History Log ---");
        for (String record : history) {
            System.out.println(record);
        }
    }
}