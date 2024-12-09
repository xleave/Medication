package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        // Current Label and Field
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

        // New Label and Field
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

        // Action Button
        JButton actionButton = new JButton(buttonText);
        actionButton.setPreferredSize(new Dimension(180, 40));
        actionButton.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(actionButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add Action Listener
        actionButton.addActionListener(e -> handleButtonAction(title, currentField, newField));

        return panel;
    }

    /**
     * Handles the action for change password button.
     * You can extend this method to handle email changes similarly.
     */
    private void handleButtonAction(String title, JTextField currentField, JTextField newField) {
        if (title.equals("Change Password")) {
            String currentPassword = currentField.getText().trim();
            String newPassword = newField.getText().trim();

            if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in both fields.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!currentPassword.equals(currentUser.getPassword())) {
                JOptionPane.showMessageDialog(this, "Incorrect current password.", "Authentication Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (currentPassword.equals(newPassword)) {
                JOptionPane.showMessageDialog(this, "New password cannot be the same as the current password.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ChangePassword changePassword = new ChangePassword();
            boolean isUpdated = changePassword.updatePassword(currentUser.getName(), newPassword);

            if (isUpdated) {
                currentUser.setPassword(newPassword);
                JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update password.", "Update Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        // Add similar blocks for other settings if needed
    }

    /**
     * Creates the logout panel with a logout button.
     */
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

        // Add Action Listener for Logout Button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLogoutConfirmationDialog();
            }
        });

        return panel;
    }

    /**
     * Displays a confirmation dialog for logout and handles the logout process.
     */
    private void showLogoutConfirmationDialog() {
        int result = JOptionPane.showConfirmDialog(mainWindow,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            mainWindow.dispose(); // Close Main Window
            SwingUtilities.invokeLater(() -> {
                LoginGUI loginGUI = new LoginGUI();
                loginGUI.setVisible(true); // Show Login GUI
            });
        }
    }
}