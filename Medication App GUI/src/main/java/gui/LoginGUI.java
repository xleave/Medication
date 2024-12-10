package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import services.User;

public class LoginGUI extends JFrame {

    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;

    public LoginGUI() {
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        initializeUI();
    }

    private void initializeUI() {
        // Set layout manager
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Username Label
        JLabel nameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(nameLabel, gbc);

        // Username Field
        nameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(nameField, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(passwordLabel, gbc);

        // Password Field
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Login Button
        loginButton = new JButton("Login");
        buttonsPanel.add(loginButton);

        // Cancel Button
        cancelButton = new JButton("Cancel");
        buttonsPanel.add(cancelButton);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(buttonsPanel, gbc);

        add(panel);

        // Add Action Listener to Login Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Add Action Listener to Cancel Button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancel();
            }
        });

        // Add Enter key functionality for password field
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    /**
     * Handles the login process when the login button is clicked.
     */
    private void handleLogin() {
        String username = nameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User(username, password);
        boolean exists = user.checkIfUserExists();

        if (exists) {
            // Successful login, launch MainGUI
            MainGUI mainGUI = new MainGUI(user);
            mainGUI.displayMainGUI();
            this.dispose(); // Close the login window
        }
    }

    /**
     * Handles the cancel action when the cancel button is clicked.
     */
    private void handleCancel() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0); // Exit the application
        }
    }

}