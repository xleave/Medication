package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import services.User;

public class SettingGUI extends JPanel {

    private User currentUser;
    private JFrame mainWindow;

    public SettingGUI(User user, JFrame mainWindow) {
        this.currentUser = user;
        this.mainWindow = mainWindow;
        initializeUI();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3,1,10,10));

        JButton logoutButton = new JButton("Log Out");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 20));
        logoutButton.setBounds(20,150, 200, 50);

        JButton changePassword = new JButton("Change Password");
        changePassword.setFont(new Font("Arial", Font.PLAIN, 20));
        changePassword.setBounds(20, 250, 200, 50);


        JButton changeEmail = new JButton("Change Email");
        changeEmail.setFont(new Font("Arial", Font.PLAIN, 20));
        changeEmail.setBounds(20, 350, 200, 50);

        buttonPanel.add(changeEmail);
        buttonPanel.add(changePassword);
        buttonPanel.add(logoutButton);

        this.add(buttonPanel,BorderLayout.CENTER);


        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the main window
                mainWindow.dispose();

                // Display the login screen
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        LoginGUI loginGUI = new LoginGUI();
                        loginGUI.displayLoginGUI();
                    }
                });
            }
        });
    }
}