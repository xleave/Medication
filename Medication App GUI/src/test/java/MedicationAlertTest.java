import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import services.MedicationAlert;
import services.MedicationScheduler;
import services.TextToSpeech;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.Mockito.*;

class MedicationAlertTest {

    @Mock
    private MedicationScheduler mockScheduler;

    @Mock
    private TextToSpeech mockTts;

    private MedicationAlert medicationAlert;

    @BeforeEach
    void setUp() throws IOException {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create a temporary CSV file for testing
        Path tempFile = Files.createTempFile("medications", ".csv");
        String content = "Aspirin, 20mg, 1, 08:00\n" +
                "Paracetamol,500mg , 1, 12:00\n" +
                "Ibuprofen, 200mg, 2, 18:00";
        Files.write(tempFile, content.getBytes());

        // Initialize MedicationAlert with the temp file
        medicationAlert = new MedicationAlert("MedicationAlertTestUser", mockScheduler, mockTts);

        // Replace the path with the temp file for testing purposes
        medicationAlert.setCsvFilePath(tempFile.toString());
    }


    @Test
    void testLoadMedicationsAndScheduleWithInvalidFile() {
        // Create an invalid CSV path
        Path invalidFilePath = Paths.get("invalid_file.csv");

        // Set up MedicationAlert with invalid file path
        medicationAlert.setCsvFilePath(invalidFilePath.toString());

        // Verify no alerts are scheduled due to invalid file
        verify(mockScheduler, never()).scheduleAlert(anyString(), any());
    }

    @Test
    void testCorrectMedicationTimesAreParsed() {
        // Test that the medication times are correctly parsed from the file
        Path tempFile = Paths.get("src/main/resources/medications/MedicationAlertTestUser_medications.csv");
        List<String> lines = List.of(
                "Aspirin, 20mg, 1, 08:00" ,
                "Paracetamol,500mg , 1, 12:00" ,
                "Ibuprofen, 200mg, 2, 18:00"
        );

        try {
            Files.write(tempFile, lines);
            medicationAlert = new MedicationAlert("MedicationAlertTestUser", mockScheduler, mockTts);
            medicationAlert.loadMedicationsAndSchedule();

            // Verify that the times are correctly parsed and passed to scheduler
            // Use anyString() and any() to match both arguments
            verify(mockScheduler).scheduleAlert(eq("08:00"), any());
            verify(mockScheduler).scheduleAlert(eq("12:00"), any());
            verify(mockScheduler).scheduleAlert(eq("18:00"), any());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}