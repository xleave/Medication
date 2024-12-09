import services.*;
import javax.swing.*;
import java.io.*;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class MedicationManageTest {

    @Test
    public void testSaveMedicationInfoCreatesNewFile() throws IOException {
        JTextField[] textFields = {new JTextField("Aspirin")};
        JButton acceptButton = new JButton();
        JFrame frame = new JFrame();

        MedicationManage.saveMedicationInfo("testUser", textFields, acceptButton, frame);

        File expectedFile = new File("src/main/resources/medications/testUser_medications.csv");
        assertTrue(expectedFile.exists());
        // Clean up
        expectedFile.delete();
    }


    @Test
    public void testGetUserMedicationDataHandlesMissingFile() {
        User mockUser = Mockito.mock(User.class);
        Mockito.when(mockUser.getName()).thenReturn("nonexistentUser");

        Object[][] data = MedicationManage.getUserMedicationData(mockUser);
        assertEquals(0, data.length);
    }

}
