import gui.LoginGUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {


        LoginGUI loginGUI = new LoginGUI();

        loginGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });


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