import services.AdminManage;
import services.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.ArrayList;
import static org.junit.Assert.*;

//Warning: please do not run this test without any need!
// this unit test will cause user.csv to be completely erased!

public class AdminManageTest {

    private File userFile;
    private File tempUserFile;
    private File medicationFile;
    private User adminUser;
    private AdminManage adminManage;

    @Before
    public void setUp() throws IOException {
        // Initialize test files
        userFile = new File("src/main/resources/users/users.csv");
        tempUserFile = new File("src/main/resources/users/users_temp.csv");
        medicationFile = new File("src/main/resources/medications/testUser_medications.csv");

        // Create users.csv with test data
        BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
        writer.write("admin,testPass,admin\n");
        writer.write("user1,pass1,role1\n");
        writer.write("user2,pass2,role2\n");
        writer.close();

        // Create a medication file for user1
        BufferedWriter medWriter = new BufferedWriter(new FileWriter(medicationFile));
        medWriter.write("med1,10mg\n");
        medWriter.close();

        // Initialize AdminManage with admin user
        adminUser = new User("admin", "testPass");
        adminManage = new AdminManage(adminUser);
    }

    @After
    public void tearDown() {
        // Delete test files
        if (userFile.exists()) {
            userFile.delete();
        }
        if (tempUserFile.exists()) {
            tempUserFile.delete();
        }
        if (medicationFile.exists()) {
            medicationFile.delete();
        }
    }

    @Test
    public void testGetAllUsers() {
        ArrayList<String[]> users = adminManage.getAllUsers();
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0)[0]);
        assertEquals("user2", users.get(1)[0]);
    }

    @Test
    public void testGetAllUsersEmpty() throws IOException {
        // Clear users.csv
        BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
        writer.close();

        ArrayList<String[]> users = adminManage.getAllUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    public void testGetAllUsersOnlyAdmin() throws IOException {
        // Modify users.csv to contain only admin
        BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
        writer.write("admin,testPass,admin\n");
        writer.close();

        ArrayList<String[]> users = adminManage.getAllUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    public void testDeleteUserExists() {
        adminManage.deleteUser("user1");
        // Verify user1 is deleted from users.csv
        ArrayList<String[]> users = adminManage.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("user2", users.get(0)[0]);
        // Verify medication file is deleted
        assertFalse(medicationFile.exists());
    }

    @Test
    public void testDeleteUserDoesNotExist() {
        adminManage.deleteUser("nonExistingUser");
        // Verify users.csv remains unchanged
        ArrayList<String[]> users = adminManage.getAllUsers();
        assertEquals(2, users.size());
        // Verify no medication file is deleted
        assertTrue(medicationFile.exists());
    }

    @Test
    public void testDeleteUserFileDeletionFailure() throws IOException {
        // Make users.csv read-only to simulate deletion failure
        userFile.setReadOnly();
        adminManage.deleteUser("user1");
        // Cleanup: make it writable again for tearDown
        userFile.setWritable(true);
    }
}