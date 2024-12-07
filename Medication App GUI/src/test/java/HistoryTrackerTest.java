import org.junit.*;
import services.HistoryTracker;
import services.User;

import static org.junit.Assert.*;

import java.awt.Font;
import java.io.*;
import java.time.LocalDate;
import java.util.Map;

public class HistoryTrackerTest {

    private HistoryTracker historyTracker;
    private User testUser;
    private final String medicationsDir = "src/main/resources/medications/";
    private final String takenRecordsDir = "src/main/resources/taken_records/";

    private final String testUserMedicationsFile = medicationsDir + "testuser_medications.csv";
    private final String testUserTakenRecordsFile = takenRecordsDir + "testuser_taken_records.csv";

    @Before
    public void setUp() throws IOException {
        // Create test user with name and password
        testUser = new User("testuser", "password123");

        // Create directories if they don't exist
        new File(medicationsDir).mkdirs();
        new File(takenRecordsDir).mkdirs();

        // Create medications file for the test user
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testUserMedicationsFile))) {
            writer.write("Para,3\n");
            writer.write("Ibu,2\n");
            writer.write("Med3,1\n");
            writer.write("Med4,4\n");
            writer.write("Med5,2\n");
        }

        // Create taken records file with today's date
        LocalDate today = LocalDate.now();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testUserTakenRecordsFile))) {
            writer.write("Para," + today.toString() + ",1\n");
            writer.write("Ibu," + today.toString() + ",2\n");
            writer.write("Med3," + today.toString() + ",0\n");
            writer.write("Med4," + today.toString() + ",4\n");
            writer.write("Med5," + today.toString() + ",1\n");
        }

        // Initialize HistoryTracker instance with the test user
        historyTracker = new HistoryTracker(testUser);
    }

    @After
    public void tearDown() {
        // Delete test medications file
        new File(testUserMedicationsFile).delete();

        // Delete test taken records file
        new File(testUserTakenRecordsFile).delete();
    }

    @Test
    public void testLoadMedicationLimits() {
        Map<String, Integer> medicationLimits = historyTracker.getMedicationDailyLimits();
        assertEquals("Number of medications should be 5", 5, medicationLimits.size());
        assertEquals("Para daily limit should be 3", 3, (int) medicationLimits.get("Para"));
        assertEquals("Ibu daily limit should be 2", 2, (int) medicationLimits.get("Ibu"));
        assertEquals("Med3 daily limit should be 1", 1, (int) medicationLimits.get("Med3"));
        assertEquals("Med4 daily limit should be 4", 4, (int) medicationLimits.get("Med4"));
        assertEquals("Med5 daily limit should be 2", 2, (int) medicationLimits.get("Med5"));
    }


}