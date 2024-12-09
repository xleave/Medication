import services.ChangePassword;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.*;
import java.util.ArrayList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChangePasswordTest {

    private ChangePassword changePassword;
    private File mockFile;
    private BufferedReader mockReader;
    private BufferedWriter mockWriter;

    @Before
    public void setUp() throws IOException {
        changePassword = new ChangePassword();
        mockFile = mock(File.class);
        mockReader = mock(BufferedReader.class);
        mockWriter = mock(BufferedWriter.class);

        // Replace the FileReader and FileWriter with mocks
        // This requires ChangePassword to allow dependency injection or use other techniques
    }

    @Test
    public void testUpdatePasswordSuccess() throws IOException {
        String username = "testUser";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        // Mocking the file content
        when(mockReader.readLine())
                .thenReturn(username + "," + oldPassword)
                .thenReturn(null);

        // Mocking FileReader and BufferedReader
        // This part depends on how ChangePassword is implemented to allow mocking

        boolean result = changePassword.updatePassword(username, newPassword);
        assertTrue(result);

        // Verify that the password was updated
        // Verify that writer.write was called with updated password
    }


}