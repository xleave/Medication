public class Main {

    public static void main(String[] args) {


        LoginGUI loginGUI = new LoginGUI();
        loginGUI.displayLoginGUI();

        System.out.println("Hello and Welcome to the Medication Reminder Application");
        User genericUser = new User(loginGUI.getNameFieldContents(), loginGUI.getPasswordFieldContents());
        genericUser.checkIfUserExists();
        genericUser.manageMedicines();




    }
}