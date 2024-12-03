
import services.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testUserExists() {
        User user = new User("testUser", "testPass");
        assertTrue(user.checkIfUserExists());
    }
}