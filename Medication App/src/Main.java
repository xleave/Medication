import gui.*;

public class Main {

    public static void main(String[] args) {

        /*
        //The below code is just an example of using TTS. once all the GUI is ready we can integrate all these to make voice alerts.
        TextToSpeech tts = new TextToSpeech();
        tts.speak("It's time to take your medication.");
        tts.close();
        */


        LoginGUI loginGUI = new LoginGUI();


        String username = loginGUI.getNameFieldContents();
        String password = loginGUI.getPasswordFieldContents();
        System.out.println("username from main : " + username);
        System.out.println("password from main : " + password);
        //loginGUI.displayLoginGUI();

        System.out.println("Hello and Welcome to the Medication Reminder Application");
        User genericUser = new User(username,password);
        genericUser.checkIfUserExists();
        genericUser.manageMedicines();


        //MainGUI mainGUI = new MainGUI();
        //mainGUI.displayMainGUI();


    }
}