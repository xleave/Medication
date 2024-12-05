// Scheduler.java
package modules;

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
    private HistoryTracker historyTracker;
    private String username; // Username associated with this scheduler
    private String historyDirectory = "Medication App/src/resources/medication_history"; // Default history directory
    private String overdoseAlertDirectory = "Medication App/src/resources/medications"; // Default overdose alert
                                                                                        // directory

    public Scheduler(String username, String historyDirectory) {
        this.username = username;
        this.historyDirectory = historyDirectory;
        medicines = new ArrayList<>();
        // Initialize HistoryTracker with user's history file
        historyTracker = new HistoryTracker(historyDirectory, username);
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
            // Prepare overdose alert
            prepareOverdoseAlert(medicine);

            alertOverdose(medicine);
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
                System.out.println("Overdose alert prepared at " + alertFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while preparing overdose alert.");
            e.printStackTrace();
        }
    }

    // View history
    public void viewHistory() {
        historyTracker.displayHistory();
    }

    // Get the list of medicines
    public List<Medicine> getMedicines() {
        return medicines;
    }

    // Set history directory (optional)
    public void setHistoryDirectory(String directory) {
        this.historyDirectory = directory;
        // Reinitialize HistoryTracker with the new path
        historyTracker = new HistoryTracker(directory, username);
    }

    // Set overdose alert directory (optional)
    public void setOverdoseAlertDirectory(String directory) {
        this.overdoseAlertDirectory = directory;
    }

    public static  void alertOverdose(Medicine medicine) {
        System.out.println("Alert: Possible overdose detected for medicine: " + medicine.getName());
        // Additional alert mechanisms can be added here
        try {
            Properties prop = new Properties();
            prop.setProperty("mail.host", "smtp.qq.com"); // Set QQ mail server
            prop.setProperty("mail.transport.protocol", "smtp"); // Mail sending protocol
            prop.setProperty("mail.smtp.auth", "true"); // Enable authentication

            // Configure SSL encryption for QQ mail
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);

            // Create a session with the mail server
            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("2056250677@qq.com", "uunmuqpfqzcsejef"); // Replace with actual email and authorization code
                }
            });
            session.setDebug(true); // Enable debug mode to view email sending process

            // Get transport object and connect to the mail server
            Transport ts = session.getTransport();
            ts.connect("smtp.qq.com", "2056250677@qq.com", "uunmuqpfqzcsejef"); // Replace with actual email and authorization code

            // Create the email message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("2056250677@qq.com")); // Replace with actual sender email
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("ambrose021031@gmail.com")); // Replace with actual recipient email
            message.setSubject("Overdose Alert");
            message.setContent("Alert: Possible overdose detected for medicine:"+medicine.getName(), "text/html;charset=UTF-8");

            // Send the email
            ts.sendMessage(message, message.getAllRecipients());
            System.out.println("Email alert sent successfully.");

            // Close the transport connection
            ts.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}