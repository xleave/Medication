import gui.*;
import services.MedicationAlert;
import services.User;
import services.MedicationScheduler;
import services.TextToSpeech;

public class Main {

    public static void main(String[] args) {
        // Initialize Text-to-Speech and Scheduler
        TextToSpeech tts = new TextToSpeech();
        MedicationScheduler scheduler = new MedicationScheduler();

        // Create Login GUI
        LoginGUI loginGUI = new LoginGUI();

        // Get user input from Login GUI
        String username = loginGUI.getNameFieldContents();
        String password = loginGUI.getPasswordFieldContents();

        // Create modules.User object and check if user exists
        User genericUser = new User(username, password);
        genericUser.checkIfUserExists();

        // Schedule medication alerts
        MedicationAlert medicationAlert = new MedicationAlert(username, scheduler, tts);

        // Launch Main GUI with the user information
        MainGUI mainGUI = new MainGUI(genericUser);
        mainGUI.displayMainGUI(); // Call the method to display the GUI

        // Add shutdown hook to clean up resources
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduler.shutdown();
            tts.close();
        }));
    }
}