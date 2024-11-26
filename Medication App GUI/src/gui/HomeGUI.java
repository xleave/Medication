package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import modules.User;
public class HomeGUI extends JPanel {

    private modules.User currentUser;

    public HomeGUI(modules.User user) {

        this.currentUser = user;

        // Set up fonts
        Font applicationFont;
        try {
            applicationFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File("src/resources/fonts/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);
        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            applicationFont = new Font("Arial", Font.PLAIN, 16);
        }

        // Set up application icon
        BufferedImage applicationImage;
        try {
            applicationImage = ImageIO.read(new File("src/resources/icons/App Icon.png"));
        } catch (IOException e) {
            System.out.println("Application icon not found!");
            applicationImage = null;
        }

        // Set up home panel
        setLayout(null);
        setPreferredSize(new Dimension(980, 600));

        JLabel homeSubmenuHeading = new JLabel("HOME");
        homeSubmenuHeading.setBounds(450, 10, 100, 50);
        homeSubmenuHeading.setFont(applicationFont);

        JLabel applicationIconLabel = new JLabel();
        if (applicationImage != null) {
            applicationIconLabel.setIcon(new ImageIcon(applicationImage));
        }
        applicationIconLabel.setBounds(390, 70, 200, 200);

        JLabel homeWelcomeText = new JLabel("Welcome " + currentUser.getName());
        homeWelcomeText.setBounds(300, 300, 500, 50);
        homeWelcomeText.setFont(new Font("Courier", Font.PLAIN, 40));

        JLabel nextMedicineDueText = new JLabel(
                "Your next medication is due in " + calculateNextMedicineTime() + " hours");
        nextMedicineDueText.setBounds(275, 380, 500, 50);
        nextMedicineDueText.setFont(new Font("Courier", Font.PLAIN, 20));

        add(homeSubmenuHeading);
        add(applicationIconLabel);
        add(homeWelcomeText);
        add(nextMedicineDueText);
    }

    private String calculateNextMedicineTime() {
        // Placeholder for actual calculation
        return "2";
    }
}