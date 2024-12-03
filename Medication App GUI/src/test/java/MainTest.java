import services.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testUserExists() {
        User user = new User("testUser", "testPass");
        assertTrue(user.checkIfUserExists());
    }

    @Test
    public void testPasswordVerification() {
        User user = new User("testUser", "testPass");
        assertTrue(user.verifyPassword("testPass"));
        assertFalse(user.verifyPassword("wrongPass"));
    }

    @Test
    public void testCreateNewUser() {
        User newUser = new User("newUser", "newPass");
        assertTrue(newUser.createUser());
        assertTrue(newUser.checkIfUserExists());
    }

    @Test
    public void testLoadFontSuccess() {
        String fontPath = "src/main/resources/fonts/RobotoCondensed-VariableFont_wght.ttf";
        Font font = MedicationManage.loadFont(fontPath);
        assertNotNull(font);
    }

    @Test(expected = RuntimeException.class)
    public void testLoadFontFailure() {
        String invalidFontPath = "invalid/path/font.ttf";
        MedicationManage.loadFont(invalidFontPath);
    }

    @Test
    public void testSaveMedicationInfo() {
        JTextField[] textFields = {
                new JTextField("Med1"),
                new JTextField("10mg"),
                new JTextField("30"),
                new JTextField("08:00"),
                new JTextField("Daily"),
                new JTextField("300mg")
        };
        JButton acceptButton = new JButton();
        JFrame frame = new JFrame();
        MedicationManage.saveMedicationInfo("testUser", textFields, acceptButton, frame);
        // 验证文件是否存在
        File medicationFile = new File("src/main/resources/medications/testUser_medications.csv");
        assertTrue(medicationFile.exists());
        // 清理测试文件
        medicationFile.delete();
    }

    @Test
    public void testGetAdminMedicationData() {
        Object[][] data = MedicationManage.getAdminMedicationData();
        assertNotNull(data);
        // 根据测试环境进一步验证数据内容
    }

    @Test
    public void testGetUserMedicationData() {
        User user = new User("testUser", "testPass");
        Object[][] data = MedicationManage.getUserMedicationData(user);
        assertNotNull(data);
        // 根据测试环境进一步验证数据内容
    }
}