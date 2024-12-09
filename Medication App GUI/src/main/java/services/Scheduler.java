// Scheduler.java
package services;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Scheduler {
private List<Medicine> medicines;

private String username; // Username associated with this scheduler
private String historyDirectory = "src/main/resources/medication_history"; //

private String overdoseAlertDirectory = "src/main/resources/medications"; //

// directory

public Scheduler(String username, String historyDirectory) {
this.username = username;
this.historyDirectory = historyDirectory;
medicines = new ArrayList<>();
// Initialize HistoryTracker with user's history file

}

// Get Medicine object by name
public Medicine getMedicineByName(String name) {
for (Medicine med : medicines) {
if (med.getName().equalsIgnoreCase(name)) {
return med;
}
}
return null; // Return null if not found
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
String currentTime = now.toLocalTime().withSecond(0).withNano(0).toString();
for (Medicine med : medicines) {
// Implement logic to check if it's time to take the medicine
if (med.getTimeToTake().equals(currentTime)) {
sendReminder(med);
}
}
}

// Send reminder for a medicine
private void sendReminder(Medicine medicine) {
// Send visual and auditory notifications
System.out.println("Reminder: It's time to take your medicine - " +
medicine.getName());

// Here you can implement voice reminders using a Text-to-Speech API

// Log the reminder

}

// modules.User takes a dose of medicine
public void takeMedicine(Medicine medicine) {
medicine.addTakenTime(LocalDateTime.now());


if (medicine.isOverDose()) {
System.out
.println("Warning: You have reached or exceeded the maximum daily dose for "
+ medicine.getName());
// Prepare overdose alert
prepareOverdoseAlert(medicine);
}
}

// Prepare overdose alert
public void prepareOverdoseAlert(Medicine medicine) {
try {
// Use the updated overdoseAlertDirectory
File directory = new File(overdoseAlertDirectory);
if (!directory.exists()) {
directory.mkdirs();
}

// Create an empty file with the name of the drug and the current time
String filename = "overdose_alert_" + medicine.getName() + "_"
+ LocalDateTime.now().toString().replace(":", "-") + ".txt";
File alertFile = new File(directory, filename);
if (alertFile.createNewFile()) {
System.out.println("Overdose alert prepared at " +
alertFile.getAbsolutePath());
}
} catch (IOException e) {
System.out.println("An error occurred while preparing overdose alert.");
e.printStackTrace();
}
}



// Get the list of medicines
public List<Medicine> getMedicines() {
return medicines;
}

// Set history directory (optional)
public void setHistoryDirectory(String directory) {
this.historyDirectory = directory;

}

// Set overdose alert directory (optional)
public void setOverdoseAlertDirectory(String directory) {
this.overdoseAlertDirectory = directory;
}
}