import java.io.*;
import java.util.Scanner;

public class User {

    private String name;
    private String password;

    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }

    public void checkIfUserExists() {    //Read users.csv to see if entry for user already exists.
        System.out.println("Please enter your details");
        System.out.print("Name:");
        Scanner userInputScanner = new Scanner(System.in);
        String temporaryName = userInputScanner.next();

        try {
            File userFile = new File("src/users.csv");
            Scanner userFileScanner = new Scanner(userFile);
            while (userFileScanner.hasNextLine()) {
                String line = userFileScanner.next();

                if (line.contains(temporaryName)) {     //If user exists, load password and log in.
                    System.out.println("Hello " + line  + ". Please enter your Password."  );
                    System.out.print("Password:");
                    String temporaryPassword = userInputScanner.next();
                    String passwordInFile = userFile;

                }

                //if (!line.contains(temporaryName)) {  //If no user is found, let them make account.
                    //userCreate();
                }
           // }

        } catch (FileNotFoundException e) {
            System.out.println("The user file could not be found! Program will now close.");
            System.exit(2);
        }
    }


    public void userCreate() {
        System.out.println("Please enter your details to create your new account.");
        Scanner userInput = new Scanner(System.in);
        name = userInput.next();
        password = userInput.next();

        //Write new details to user file.

        //Create a new medication file for the user.
    }

    public void userLogin() {
        System.out.println("Please enter your login credentials to log into your account.");
        Scanner userInput = new Scanner(System.in);
        password = userInput.next();
        System.out.println(password);
    }
}

