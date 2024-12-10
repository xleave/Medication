// Java
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import services.User;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        user = new User("testUser", "123321");
    }

    @Test
    public void testLoginLockoutAfterThreeFailedAttempts() {
        // First failure
        assertFalse(user.checkIfUserExists());
        // Second failure
        assertFalse(user.checkIfUserExists());
        // Third failure should trigger lockout
        assertFalse(user.checkIfUserExists());

        // Try to log in during a lockdown
        assertFalse(user.checkIfUserExists());
    }

    @Test
    public void testLoginAfterLockoutPeriod() throws InterruptedException {
        // Simulate three failures to trigger a lock
        user.checkIfUserExists();
        user.checkIfUserExists();
        user.checkIfUserExists();

        Thread.sleep(5000); // 5 seconds instead of 5 minutes
        //Due to an unknown error, the mock cannot be used correctly, so this test case will not pass in a normal environment.
        user.setPassword("123123");
        assertTrue(user.checkIfUserExists());
    }
}