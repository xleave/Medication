import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.io.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MedicationManageTest {

    @Mock
    private JTextField textField1;

    @Mock
    private JTextField textField2;

    @Mock
    private JButton acceptButton;

    @Mock
    private JFrame frame;

    @Mock
    private FileWriter fileWriter;

    @Mock
    private BufferedWriter bufferedWriter;

    @Mock
    private PrintWriter printWriter;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveMedicationInfo_FileDoesNotExist_Success() throws Exception {
        String targetUserName = "testUser";
        JTextField[] textFields = {textField1, textField2};

        when(textField1.getText()).thenReturn("Med1");
        when(textField2.getText()).thenReturn("Med2");

        File mockFile = mock(File.class);
        whenNew(File.class).withArguments("src/main/resources/medications/" + targetUserName + "_medications.csv").thenReturn(mockFile);
        when(mockFile.exists()).thenReturn(false);

        whenNew(FileWriter.class).withArguments(mockFile, true).thenReturn(fileWriter);
        whenNew(BufferedWriter.class).withArguments(fileWriter).thenReturn(bufferedWriter);
        whenNew(PrintWriter.class).withArguments(bufferedWriter).thenReturn(printWriter);

        MedicationManage.saveMedicationInfo(targetUserName, textFields, acceptButton, frame);

        verify(mockFile).createNewFile();
        verify(printWriter).println("Med1,Med2");
        verify(JOptionPane).showMessageDialog(acceptButton, "Medication has been added.", "Success", JOptionPane.INFORMATION_MESSAGE);
        verify(frame).dispose();
    }

    @Test
    public void testSaveMedicationInfo_FileExists_Success() throws Exception {
        String targetUserName = "testUser";
        JTextField[] textFields = {textField1, textField2};

        when(textField1.getText()).thenReturn("Med1");
        when(textField2.getText()).thenReturn("Med2");

        File mockFile = mock(File.class);
        whenNew(File.class).withArguments("src/main/resources/medications/" + targetUserName + "_medications.csv").thenReturn(mockFile);
        when(mockFile.exists()).thenReturn(true);

        whenNew(FileWriter.class).withArguments(mockFile, true).thenReturn(fileWriter);
        whenNew(BufferedWriter.class).withArguments(fileWriter).thenReturn(bufferedWriter);
        whenNew(PrintWriter.class).withArguments(bufferedWriter).thenReturn(printWriter);

        MedicationManage.saveMedicationInfo(targetUserName, textFields, acceptButton, frame);

        verify(mockFile, never()).createNewFile();
        verify(printWriter).println("Med1,Med2");
        verify(JOptionPane).showMessageDialog(acceptButton, "Medication has been added.", "Success", JOptionPane.INFORMATION_MESSAGE);
        verify(frame).dispose();
    }

    @Test
    public void testSaveMedicationInfo_IOException() throws Exception {
        String targetUserName = "testUser";
        JTextField[] textFields = {textField1, textField2};

        when(textField1.getText()).thenReturn("Med1");
        when(textField2.getText()).thenReturn("Med2");

        File mockFile = mock(File.class);
        whenNew(File.class).withArguments("src/main/resources/medications/" + targetUserName + "_medications.csv").thenReturn(mockFile);
        when(mockFile.exists()).thenReturn(true);

        whenNew(FileWriter.class).withArguments(mockFile, true).thenThrow(new IOException("Mock IO Exception"));

        MedicationManage.saveMedicationInfo(targetUserName, textFields, acceptButton, frame);

        verify(JOptionPane).showMessageDialog(acceptButton, "An error occurred while saving medication.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}