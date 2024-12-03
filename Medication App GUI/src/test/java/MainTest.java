import services.*;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class MainTest {

    // Create/Test newuser
    @Test
    public void testUserExists() {
        User user = new User("testUser", "testPass");
        assertTrue(user.checkIfUserExists());
    }

    // Check the font
   @Test
   public void testLoadFontSuccess() {
       String fontPath = "src/main/resources/fonts/RobotoCondensed-VariableFont_wght.ttf";
       Font font = MedicationManage.loadFont(fontPath);
       assertNotNull(font);
   }

    // Check the font
   @Test(expected = RuntimeException.class)
   public void testLoadFontFailure() {
       String invalidFontPath = "src/main/resources/fonts/RobotoCondensed-VariableFont_wght.ttf\"";
       MedicationManage.loadFont(invalidFontPath);
   }

}