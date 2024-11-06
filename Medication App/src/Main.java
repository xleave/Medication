public class Main {

    public static void main(String[] args) {

        System.out.println("Hello and Welcome to the Medication Reminder Application");
        User genericUser = new User();
        genericUser.checkIfUserExists();
        genericUser.manageMedicines();
    }
}