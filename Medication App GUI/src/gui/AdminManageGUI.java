package gui;

import javax.swing.*;
import java.awt.*;
import modules.User;

public class AdminManageGUI extends JPanel {

    private User currentUser;

    public AdminManageGUI(User user) {
        this.currentUser = user;
        initialize();
    }

    private void initialize() {
        // Setting up the layout
        this.setLayout(new BorderLayout());

        // Add a title
        JLabel titleLabel = new JLabel("Admin Management Module");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titleLabel, BorderLayout.NORTH);

        // Add components for administrator functions, such as user management, log
        // viewing, etc.

        JTextArea adminFunctionsArea = new JTextArea();
        adminFunctionsArea.setText("Here is the administrator ribbon, which contains functions such as user management。");
        adminFunctionsArea.setEditable(false);
        this.add(adminFunctionsArea, BorderLayout.CENTER);
    }
}