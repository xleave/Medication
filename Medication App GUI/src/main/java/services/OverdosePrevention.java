// OverdosePrevention.java
package services;

import javax.mail.PasswordAuthentication;
import java.util.Properties;
import com.sun.mail.util.MailSSLSocketFactory;
import com.sun.net.httpserver.HttpExchange;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class OverdosePrevention {
    // Method to alert user
    public static  void alertOverdose(Medicine medicine) {
        System.out.println("Alert: Possible overdose detected for medicine: " + medicine.getName());
        // Additional alert mechanisms can be added here
        try {
            Properties prop = new Properties();
            prop.setProperty("mail.host", "smtp.qq.com");
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");


            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);


            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("2056250677@qq.com", "uunmuqpfqzcsejef");
                }
            });
            session.setDebug(true);


            Transport ts = session.getTransport();
            ts.connect("smtp.qq.com", "2056250677@qq.com", "uunmuqpfqzcsejef");


            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("2056250677@qq.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("ambrose021031@gmail.com"));
            message.setSubject("Overdose Alert");
            message.setContent("Alert: Possible overdose detected for medicine:"+medicine.getName(), "text/html;charset=UTF-8");


            ts.sendMessage(message, message.getAllRecipients());
            System.out.println("Email alert sent successfully.");


            ts.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}