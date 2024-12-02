import gui.*;
import services.User;

public class Main {

    public static void main(String[] args) {

        // Create Login GUI
        LoginGUI loginGUI = new LoginGUI();

        // Get user input from Login GUI
        String username = loginGUI.getNameFieldContents();
        String password = loginGUI.getPasswordFieldContents();

        // Create modules.User object and check if user exists
        User genericUser = new User(username, password);
        genericUser.checkIfUserExists();

        // Launch Main GUI with the user information
        MainGUI mainGUI = new MainGUI(genericUser);
        mainGUI.displayMainGUI(); // Call the method to display the GUI
    }
}