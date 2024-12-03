//package gui;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import modules.User;
//
//public class SettingGUI extends JPanel {
//
//    private User currentUser;
//    private JFrame mainWindow;
//
//    public SettingGUI(User user, JFrame mainWindow) {
//        this.currentUser = user;
//        this.mainWindow = mainWindow;
//        initializeUI();
//    }
//
//    private void initializeUI() {
//        this.setLayout(new BorderLayout());
//
//        JButton logoutButton = new JButton("Log out of your account");
//        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
//        logoutButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Close the main window
//                mainWindow.dispose();
//
//                // Display the login screen
//                SwingUtilities.invokeLater(new Runnable() {
//                    public void run() {
//                        LoginGUI loginGUI = new LoginGUI();
//                        loginGUI.displayLoginGUI();
//                    }
//                });
//            }
//        });
//
//        this.add(logoutButton, BorderLayout.CENTER);
//    }
//}