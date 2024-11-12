public class Main {

    public static void main(String[] args) {


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




    }
}