package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import services.ChangePassword;
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
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel emailPanel = createPanel("Change Email", "Current Email", "New Email", "Update Email");
        JPanel passwordPanel = createPanel("Change Password", "Current Password", "New Password", "Update Password");
        JPanel logoutPanel = createLogoutPanel();

        this.add(emailPanel);
        this.add(Box.createVerticalStrut(20));
        this.add(passwordPanel);
        this.add(Box.createVerticalStrut(20));
        this.add(logoutPanel);
    }

    private JPanel createPanel(String title, String currentLabel, String newLabel, String buttonText) {
        JPanel panel = new JPanel(new BorderLayout());

        Font titleFont = new Font("Arial", Font.BOLD, 18);
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(titleFont);
        panel.setBorder(border);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel currentLabelComponent = new JLabel(currentLabel + ":");
        currentLabelComponent.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(currentLabelComponent, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField currentField = new JTextField(20);
        currentField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(currentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        JLabel newLabelComponent = new JLabel(newLabel + ":");
        newLabelComponent.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(newLabelComponent, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField newField = new JTextField(20);
        newField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(newField, gbc);

        panel.add(inputPanel, BorderLayout.CENTER);

        JButton actionButton = new JButton(buttonText);
        actionButton.setPreferredSize(new Dimension(180, 40));
        actionButton.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(actionButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        actionButton.addActionListener(e -> handleButtonAction(title, currentField, newField));

        return panel;
    }

    private void handleButtonAction(String title, JTextField currentField, JTextField newField) {
        if (title.equals("Change Password")) {
            String currentPassword = currentField.getText();
            String newPassword = newField.getText();
            if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(mainWindow, "Please fill in both fields.");
            } else if (!currentPassword.equals(currentUser.getPassword())) {
                JOptionPane.showMessageDialog(mainWindow, "Incorrect current password.");
            } else if (currentPassword.equals(newPassword)) {
                JOptionPane.showMessageDialog(mainWindow, "New password cannot be the same as the current password.");
            } else {

                ChangePassword changePassword = new ChangePassword();
                boolean isUpdated = changePassword.updatePassword(currentUser.getName(), newPassword);

                if (isUpdated) {
                    currentUser.setPassword(newPassword);
                    JOptionPane.showMessageDialog(mainWindow, "Password updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Failed to update password.");
                }
            }
        }
    }

    private JPanel createLogoutPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        Font titleFont = new Font("Arial", Font.BOLD, 18);
        TitledBorder border = BorderFactory.createTitledBorder("Log Out");
        border.setTitleFont(titleFont);
        panel.setBorder(border);

        JButton logoutButton = new JButton("Log Out");
        logoutButton.setPreferredSize(new Dimension(180, 40));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        logoutButton.addActionListener(e -> {
            mainWindow.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginGUI loginGUI = new LoginGUI();
                loginGUI.displayLoginGUI();
            });
        });

        return panel;
    }
}