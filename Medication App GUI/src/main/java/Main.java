import gui.LoginGUI;
import services.User;

public class Main {

    public static void main(String[] args) {
        // Launch the Login GUI on the Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }
}