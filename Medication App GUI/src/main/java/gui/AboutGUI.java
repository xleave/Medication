package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AboutGUI extends JPanel {

    public AboutGUI() {

        // Set up fonts
        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/resources/fonts/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);

        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            applicationFont = new Font("Arial", Font.PLAIN, 16); // Use default font
        }

        // Set layout for the panel
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(980, 600));

        // Create heading label
        JLabel aboutSubmenuHeading = new JLabel("ABOUT", SwingConstants.CENTER);
        aboutSubmenuHeading.setFont(applicationFont);

        // Create about text labels
        JLabel aboutText = new JLabel(
                "<html><div style='text-align: center;'>This application was made by Group 1 - Java for CE320 Large Scale Software Systems and Extreme Programming.<br>The goal of this task was to create a Desktop Application to allow users to log their medications and receive<br>reminders for when to take their medicines.</div></html>",
                SwingConstants.CENTER);
        aboutText.setFont(applicationFont);

        JLabel aboutTextXP = new JLabel(
                "<html><div style='text-align: center;'>Throughout the development of this project, we used Extreme Programming values to work as a team,<br>prioritize tasks, and use pair programming to get it done.</div></html>",
                SwingConstants.CENTER);
        aboutTextXP.setFont(applicationFont);

        // Create a panel to hold labels
        JPanel aboutPanelContents = new JPanel();
        aboutPanelContents.setLayout(new BoxLayout(aboutPanelContents, BoxLayout.Y_AXIS));
        aboutPanelContents.setPreferredSize(new Dimension(980, 600));

        // Add components to the panel
        aboutPanelContents.add(Box.createVerticalStrut(50)); // Add vertical space
        aboutPanelContents.add(aboutSubmenuHeading);
        aboutPanelContents.add(Box.createVerticalStrut(30));
        aboutPanelContents.add(aboutText);
        aboutPanelContents.add(Box.createVerticalStrut(20));
        aboutPanelContents.add(aboutTextXP);

        // Add the contents panel to the main panel
        add(aboutPanelContents, BorderLayout.CENTER);
    }
}