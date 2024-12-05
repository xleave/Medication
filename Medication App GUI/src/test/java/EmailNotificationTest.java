import org.junit.Test;
import services.OverdosePrevention;
import java.util.*;
import services.*;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import static org.mockito.Mockito.*;


public class EmailNotificationTest {
    @Test
    public void testAlertOverdoseEmailSending() throws Exception {
        // Mock Medicine object
        Medicine mockMedicine = mock(Medicine.class);
        when(mockMedicine.getName()).thenReturn("Paracetamol");

        // Mock Session, Transport, and MimeMessage
        Session mockSession = mock(Session.class);
        Transport mockTransport = mock(Transport.class);
        MimeMessage mockMessage = mock(MimeMessage.class);

        // Mock the mail server properties and session creation
        Session session = Session.getDefaultInstance(new Properties(), null);
        mockStatic(Session.class);
        when(Session.getDefaultInstance(any(Properties.class), any(Authenticator.class))).thenReturn(mockSession);

        // Mock Transport object and connection
        when(mockSession.getTransport()).thenReturn(mockTransport);

        // Mock MimeMessage creation
        when(new MimeMessage(mockSession)).thenReturn(mockMessage);

        // Call the alertOverdose method
        OverdosePrevention.alertOverdose(mockMedicine);

        // Verify email properties
        verify(mockMessage).setFrom(new InternetAddress("2056250677@qq.com"));
        verify(mockMessage).setRecipient(eq(Message.RecipientType.TO), any(InternetAddress.class));
        verify(mockMessage).setSubject("Overdose Alert");
        verify(mockMessage).setContent("Alert: Possible overdose detected for medicine:Paracetamol", "text/html;charset=UTF-8");

        // Verify email was sent
        verify(mockTransport).sendMessage(mockMessage, mockMessage.getAllRecipients());
    }

    @Test
    public void testAlertOverdoseHandlesException() {
        // Mock Medicine object
        Medicine mockMedicine = mock(Medicine.class);
        when(mockMedicine.getName()).thenReturn("Paracetamol");

        // Simulate an exception during email sending
        Session mockSession = mock(Session.class);
        Transport mockTransport = mock(Transport.class);
        try {
            when(mockSession.getTransport()).thenReturn(mockTransport);
            doThrow(new MessagingException("Simulated exception")).when(mockTransport).sendMessage(any(MimeMessage.class), any(Address[].class));

            // Call the method to ensure it handles exceptions
            OverdosePrevention.alertOverdose(mockMedicine);
        } catch (Exception e) {
            // Ensure exception does not propagate
            assert false : "Exception should have been handled internally";
        }
    }
}
