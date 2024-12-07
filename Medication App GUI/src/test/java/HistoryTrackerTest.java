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

    @Test
    public void testLoadTakenRecords() {
        assertEquals("Para taken count should be 1", 1, historyTracker.getTakenCount("Para"));
        assertEquals("Ibu taken count should be 2", 2, historyTracker.getTakenCount("Ibu"));
        assertEquals("Med3 taken count should be 0", 0, historyTracker.getTakenCount("Med3"));
        assertEquals("Med4 taken count should be 4", 4, historyTracker.getTakenCount("Med4"));
        assertEquals("Med5 taken count should be 1", 1, historyTracker.getTakenCount("Med5"));
    }

    @Test
    public void testIsExceeded() {
        assertFalse("Para should not have exceeded daily limit", historyTracker.isExceeded("Para"));
        assertTrue("Ibu should have exceeded daily limit", historyTracker.isExceeded("Ibu"));
        assertFalse("Med3 should not have exceeded daily limit", historyTracker.isExceeded("Med3"));
        assertTrue("Med4 should have exceeded daily limit", historyTracker.isExceeded("Med4"));
        assertFalse("Med5 should not have exceeded daily limit", historyTracker.isExceeded("Med5"));
    }

    @Test
    public void testSaveTakenRecord() {
        // Before saving, Para taken count is 1, limit is 3
        assertFalse("Para should not have exceeded daily limit", historyTracker.isExceeded("Para"));

        // Save once
        historyTracker.saveTakenRecord("Para");
        assertEquals("Para taken count should be 2", 2, historyTracker.getTakenCount("Para"));
        assertFalse("Para should not have exceeded daily limit", historyTracker.isExceeded("Para"));

        // Save again
        historyTracker.saveTakenRecord("Para");
        assertEquals("Para taken count should be 3", 3, historyTracker.getTakenCount("Para"));
        assertTrue("Para should have exceeded daily limit", historyTracker.isExceeded("Para"));
    }

    @Test
    public void testReloadMedicationData() throws IOException {
        // Modify medications file by adding Med6
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testUserMedicationsFile, true))) {
            writer.write("Med6,5\n");
        }

        // Modify taken records file by adding Med6 with 0 taken
        LocalDate today = LocalDate.now();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testUserTakenRecordsFile, true))) {
            writer.write("Med6," + today.toString() + ",0\n");
        }

        // Reload data
        historyTracker.reloadMedicationData();

        // Check if Med6 is loaded correctly
        assertEquals("Number of medications should be 6", 6, historyTracker.getMedicationDailyLimits().size());
        assertEquals("Med6 daily limit should be 5", 5, historyTracker.getDailyLimit("Med6"));
        assertEquals("Med6 taken count should be 0", 0, historyTracker.getTakenCount("Med6"));
    }

    @Test
    public void testGetDailyLimit() {
        assertEquals("Para daily limit should be 3", 3, historyTracker.getDailyLimit("Para"));
        assertEquals("Ibu daily limit should be 2", 2, historyTracker.getDailyLimit("Ibu"));
        assertEquals("Med3 daily limit should be 1", 1, historyTracker.getDailyLimit("Med3"));
        assertEquals("Med4 daily limit should be 4", 4, historyTracker.getDailyLimit("Med4"));
        assertEquals("Med5 daily limit should be 2", 2, historyTracker.getDailyLimit("Med5"));
    }

    @Test
    public void testGetTakenCount() {
        assertEquals("Para taken count should be 1", 1, historyTracker.getTakenCount("Para"));
        assertEquals("Ibu taken count should be 2", 2, historyTracker.getTakenCount("Ibu"));
        assertEquals("Med3 taken count should be 0", 0, historyTracker.getTakenCount("Med3"));
        assertEquals("Med4 taken count should be 4", 4, historyTracker.getTakenCount("Med4"));
        assertEquals("Med5 taken count should be 1", 1, historyTracker.getTakenCount("Med5"));
    }

}