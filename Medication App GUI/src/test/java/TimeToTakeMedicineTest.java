import org.junit.Test;
import services.User_old;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Scanner;

import static org.junit.Assert.*;

public class TimeToTakeMedicineTest {

    @Test
    public void testTimeToTakeMedicineValidMedicine() {
        try {

            // Create the User instance
            User_old user = new User_old("testUser", "testPassword");

            // Create the medications CSV file with valid medicine
            File medsFile = new File("src/main/resources/medications" + user.getName() + "_medicines.csv");
            if (!medsFile.exists()) {
                medsFile.createNewFile();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(medsFile))) {
                writer.write("Paracetamol\n"); // Add valid medicine to the file
            }

            // Access the private method using reflection
            Method timeToTakeMedicineMethod = User_old.class.getDeclaredMethod("timeToTakeMedicine", Scanner.class);
            timeToTakeMedicineMethod.setAccessible(true);

            // Simulate user input (medicine name and times)
            String userInput = "Paracetamol\n10:00,14:00\n"; // Medicine and times
            Scanner scanner = new Scanner(new ByteArrayInputStream(userInput.getBytes()));

            timeToTakeMedicineMethod.invoke(user, scanner);

            // Check if the "TimeToTakeMedicine.csv" file is updated correctly
            File timeTakenFile = new File("src/main/resources/" + user.getName() + " TimeToTakeMedicine.csv");
            assertTrue(timeTakenFile.exists());

            // Read and verify the content of the file
            try (BufferedReader reader = new BufferedReader(new FileReader(timeTakenFile))) {
                String line = reader.readLine();
                assertNotNull(line); // Ensure there's content
                assertTrue(line.contains("Paracetamol")); // Check if the medicine name is there
                assertTrue(line.contains("10:00")); // Ensure times are written
                assertTrue(line.contains("14:00"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testTimeToTakeMedicineInvalidMedicine() {
        try {
            // Create a User instance
            User_old user = new User_old("testUser", "testPassword");

            // Create medications CSV file without the valid medicine
            File medsFile = new File("src/main/resources/medications" + user.getName() + "_medicines.csv");
            if (!medsFile.exists()) {
                medsFile.createNewFile();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(medsFile))) {
                writer.write("Paracetamol\n"); // Add a different medicine to the file
            }

            // Access the private method using reflection
            Method timeToTakeMedicineMethod = User_old.class.getDeclaredMethod("timeToTakeMedicine", Scanner.class);
            timeToTakeMedicineMethod.setAccessible(true);

            // Simulate user input for an invalid medicine
            String userInput = "Ibuprofen\n10:00\n"; // This will be invalid since the file contains Ibuprofen
            Scanner scanner = new Scanner(new ByteArrayInputStream(userInput.getBytes()));

            timeToTakeMedicineMethod.invoke(user, scanner);

            // Assert that the medicine is not found, and the file is not created
            File timeTakenFile = new File("src/main/resources/" + user.getName() + " TimeToTakeMedicine.csv");
            assertTrue("TimeToTakeMedicine file should not be created when medicine is not found.", timeTakenFile.exists()); // No file should be created

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

}
