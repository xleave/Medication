package gui;

import javax.swing.*;

public class LoginGUI extends JFrame {

    private String nameFieldContents;
    private String passwordFieldContents;

    public String getNameFieldContents() {
        return nameFieldContents;
    }

    public String getPasswordFieldContents() {
        return passwordFieldContents;
    }

    public LoginGUI() {
        displayLoginGUI();
    }

    public void displayLoginGUI() {
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField1 = new JPasswordField(15);

        jp.add(new JLabel("Username Input:"));
        jp.add(usernameField);
        jp.add(Box.createVerticalStrut(10));
        jp.add(new JLabel("Password Input:"));
        jp.add(passwordField1);

        String[] options = { "Sign In", "Cancel" };

        int result = JOptionPane.showOptionDialog(
                null,
                jp,
                "Sign In",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (result == 0) { // "Sign In" button clicked
            nameFieldContents = usernameField.getText();
            passwordFieldContents = new String(passwordField1.getPassword());
        } else if (result == 1) { // "Cancel" button clicked
            System.out.println("Login Cancelled");
            System.exit(0);
        }
    }
}