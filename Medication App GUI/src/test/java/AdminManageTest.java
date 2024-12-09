
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.AdminManage;
import services.User;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class AdminManageTest {

    private AdminManage adminManage;
    private User testUser;
    private String testUserName = "testUser";
    private String testPassword = "testPassword";
    private String usersFilePath = "src/main/resources/users/users.csv";
    private String medicationsDirPath = "src/main/resources/medications/";

    @Before
    public void setUp() throws Exception {
        // Initialize test users
        testUser = new User(testUserName, testPassword);
        testUser.userCreate();
        adminManage = new AdminManage(testUser);

        // Confirm that the user has been created
        File userFile = new File(usersFilePath);
        assertTrue("User file does not exist", userFile.exists());

        boolean userExists = false;
        try (Scanner scanner = new Scanner(userFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(testUserName + ",")) {
                    userExists = true;
                    break;
                }
            }
        }
        assertTrue("Test user not created successfully", userExists);

        // Confirmation that a drug file has been created
        File medicationFile = new File(medicationsDirPath + testUserName + "_medications.csv");
        assertTrue("Drug file not created", medicationFile.exists());
    }

    @After
    public void tearDown() throws Exception {
        // Clean up user files
        File userFile = new File(usersFilePath);
        File tempFile = new File("src/main/resources/users/users_temp.csv");

        try (Scanner scanner = new Scanner(userFile);
                PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith(testUserName + ",")) {
                    writer.println(line);
                }
            }
        }

        if (userFile.exists() && userFile.delete()) {
            tempFile.renameTo(userFile);
        }

        // Delete drug files
        File medicationFile = new File(medicationsDirPath + testUserName + "_medications.csv");
        if (medicationFile.exists()) {
            medicationFile.delete();
        }
    }

    @Test
    public void testDeleteUser() {
        // Confirm that the user exists
        File userFile = new File(usersFilePath);
        assertTrue("User file does not exist", userFile.exists());

        boolean userExists = false;
        try (Scanner scanner = new Scanner(userFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(testUserName + ",")) {
                    userExists = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue("The user does not exist", userExists);

        // Confirmation of the existence of a drug file
        File medicationFile = new File(medicationsDirPath + testUserName + "_medications.csv");
        assertTrue("Drug file does not exist", medicationFile.exists());

        // Delete user
        adminManage.deleteUser(testUserName);

        // Confirm that the user has been deleted
        userExists = false;
        try (Scanner scanner = new Scanner(userFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(testUserName + ",")) {
                    userExists = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse("User not deleted", userExists);

        // Confirmation that drug files have been deleted
        assertFalse("Drug files have not been deleted", medicationFile.exists());
    }
    @Test
    public void testGetAllUsers() {
        ArrayList<String[]> users = adminManage.getAllUsers();
        // Confirm that the returned user list does not contain the current user
        for (String[] userDetails : users) {
            assertNotEquals("The returned user list contains the current administrator", testUserName, userDetails[0]);
        }
    }

    @Test
    public void testDeleteNonExistentUser() {
        String nonExistentUser = "nonExistentUser";
        // Verify that the user file does not contain the user
        File userFile = new File(usersFilePath);
        boolean userExists = false;
        try (Scanner scanner = new Scanner(userFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(nonExistentUser + ",")) {
                    userExists = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse("Unexpected presence of the user in the user file", userExists);

        // Delete user
        adminManage.deleteUser(nonExistentUser);

        // Verify that the user file has not been modified (except for users that do not exist)
        try (Scanner scanner = new Scanner(userFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                assertFalse("Accidentally finding a non-existing user when deleting it", line.startsWith(nonExistentUser + ","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}