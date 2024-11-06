package main.java.com.example.medicationreminder;

public class User {

        private String name;
        private String password;

        private String userfile = "";

        public String getName() {
                return name;
        }
        public String getPassword() {
                return password;
        }

        public void userLogin() {
                System.out.println("Please enter your login credentials to log into your account.");

        }
}
