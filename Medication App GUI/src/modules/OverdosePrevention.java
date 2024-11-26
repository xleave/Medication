// OverdosePrevention.java
package modules;

import java.util.Properties;
import com.sun.mail.util.MailSSLSocketFactory;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class OverdosePrevention {
    // Method to check if a medicine dose is safe
    public static boolean isDoseSafe(Medicine medicine) {
        return !medicine.isOverDose();
    }

    // Method to alert user
    public static void alertOverdose(Medicine medicine) {
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