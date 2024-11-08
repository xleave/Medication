// Scheduler.java
package modules;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Medicine> medicines;
    private HistoryTracker historyTracker;

    public Scheduler() {
        medicines = new ArrayList<>();
        historyTracker = new HistoryTracker();
    }

    // 添加此方法以根据药品名称获取 Medicine 对象
    public Medicine getMedicineByName(String name) {
        for (Medicine med : medicines) {
            if (med.getName().equalsIgnoreCase(name)) {
                return med;
            }
        }
        return null; // 如果未找到，返回 null
    }

    // Add medicine to the schedule
    public void addMedicine(Medicine medicine) {
        medicines.add(medicine);
    }

    // Remove medicine from the schedule
    public void removeMedicine(Medicine medicine) {
        medicines.remove(medicine);
    }

    // Check and send reminders for medicines due
    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now();
        for (Medicine med : medicines) {
            // Implement logic to check if it's time to take the medicine
            // For simplicity, we will compare strings here
            if (med.getTimeToTake().equals(now.toLocalTime().toString())) {
                sendReminder(med);
            }
        }
    }

    // Send reminder for a medicine
    private void sendReminder(Medicine medicine) {
        // Send visual and auditory notifications
        System.out.println("Reminder: It's time to take your medicine - " + medicine.getName());

        // Here you can implement voice reminders using a Text-to-Speech API

        // Log the reminder
        historyTracker.logReminder(medicine);
    }

    // User takes a dose of medicine
    public void takeMedicine(Medicine medicine) {
        medicine.addTakenTime(LocalDateTime.now());
        historyTracker.logTakenMedicine(medicine);

        if (medicine.isOverDose()) {
            System.out
                    .println("Warning: You have reached or exceeded the maximum daily dose for " + medicine.getName());
        }
    }

    // View history
    public void viewHistory() {
        historyTracker.displayHistory();
    }
}