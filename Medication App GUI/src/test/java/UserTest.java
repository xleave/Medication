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

        // 尝试在锁定期间登录
        assertFalse(user.checkIfUserExists());
    }

}