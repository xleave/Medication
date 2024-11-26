package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AboutGUI extends JPanel {

    public AboutGUI() {

        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/marley/Library/Mobile Documents/com~apple~CloudDocs/Documents/University Work - NAS/Year 3/CE320 Large Scale Software Systems/Group_Project/Medication App/src/EB_Garamond,Roboto_Condensed/Roboto_Condensed/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);

        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            throw new RuntimeException(e);
        }

        JPanel aboutPanel = new JPanel(new BorderLayout());
        aboutPanel.setPreferredSize(new Dimension(980, 600));

        JPanel aboutPanelContents = new JPanel();
        aboutPanelContents.setPreferredSize(new Dimension(980, 600));
        aboutPanelContents.setLayout(null);


        JLabel aboutSubmenuHeading = new JLabel("ABOUT");
        aboutSubmenuHeading.setBounds(470, -20, 50, 50);
        aboutSubmenuHeading.setFont(applicationFont);


        JLabel aboutText = new JLabel("<html>This application was made by Group 1 - Java for CE320 Large Scale Software Systems and Extreme Programming.<br>The goal of this task was to create a Desktop Application to allow users to log their medications and receive<br>reminders for when to take their medicines.</html>");
        aboutText.setBounds(125, 200, 880, 200);
        aboutText.setFont(applicationFont);

        JLabel aboutTextXP = new JLabel("<html><br> Throughout the development of this project, we used Extreme Programming values to work as a team,<br>prioritise tasks and use pair programming to get it done.</html>");
        aboutTextXP.setBounds(125, 250, 880, 200);
        aboutTextXP.setFont(applicationFont);

        aboutPanelContents.add(aboutSubmenuHeading);
        aboutPanelContents.add(aboutText);
        aboutPanelContents.add(aboutTextXP);


        aboutPanel.add(aboutPanelContents);
        add(aboutPanel);



    }
}
