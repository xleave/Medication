import gui.LoginGUI;
import services.User;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {


        // Launch the Login GUI on the Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            loginGUI.setVisible(true);
        });
    }
}
