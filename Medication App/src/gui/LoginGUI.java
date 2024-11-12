package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {

    private String nameFieldContents;
    private String passwordFieldContents;

    public String getNameFieldContents() {
        return nameFieldContents;
    }

    public LoginGUI() {
        displayLoginGUI();
    }
    public String getPasswordFieldContents() {
        return passwordFieldContents;
    }

    public void displayLoginGUI() {
        System.out.println("Login GUI");
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField1 = new JPasswordField(15);
        jp.add(new JLabel("Username Input :"));
        jp.add(usernameField);
        jp.add(Box.createVerticalStrut(10));
        jp.add(new JLabel("Password Input :"));
        jp.add(passwordField1);
        int result = JOptionPane.showConfirmDialog(null,jp, "Log In ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION){
            nameFieldContents = usernameField.getText();
            passwordFieldContents = new String(passwordField1.getPassword());
        }else{
            System.out.println("Login Cancelled");
            System.exit(0);
        }

         /**
        loginWindow.setSize(500, 250);
        loginWindow.setBackground(Color.white);
        loginWindow.setResizable(false);
        loginWindow.setLocationRelativeTo(null);
        loginWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Panel
        loginPanel.setSize(500, 250);
        loginPanel.setRequestFocusEnabled(true);

        //Icon
        String applicationIconFile = "src/pillIcon.png";
        ImageIcon applicationIcon = new ImageIcon(applicationIconFile);
        JLabel applicationIconLabel = new JLabel(applicationIcon);
        //applicationIconLabel.setBounds(160, -50, applicationIcon.getIconWidth(), applicationIcon.getIconHeight());
        loginPanel.setLayout(null);
        loginPanel.add(applicationIconLabel);

        //Title
        JLabel applicationTitle = new JLabel("Medication Reminder App");
        applicationTitle.setBounds(175, 0, 200, 25);
        loginPanel.add(applicationTitle);

        //Input
        JLabel nameLabel = new JLabel("Username");
        JTextField nameField = new JTextField(15);
        nameLabel.setBounds(65, 75, 80, 25);
        nameField.setBounds(145, 75, 200, 25);
        loginPanel.add(nameLabel);
        loginPanel.add(nameField);

        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField(15);
        passwordLabel.setBounds(65, 100, 80, 25);
        passwordField.setBounds(145, 100, 200, 25);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(250, 150, 100, 50);
        loginPanel.add(exitButton);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent exitEvent) {
                System.out.println("Exit button clicked");
                System.exit(3);

            }
        });

        JButton loginButton = new JButton("Log In");
        loginButton.setBounds(150, 150, 100, 50);
        loginPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent loginEvent) {

                nameFieldContents = nameField.getText();
                passwordFieldContents = new String(passwordField.getPassword());

                System.out.println("Login Button clicked");
                //System.out.println("Usermname :" + nameFieldContents);
                //System.out.println("Password :" + passwordFieldContents);

                //getNameFieldContents();
                //getPasswordFieldContents();

            }
        });

        //Rendering
        loginWindow.add(loginPanel);
        loginPanel.setVisible(true);
        loginWindow.setVisible(true);
        **/

    }






}
