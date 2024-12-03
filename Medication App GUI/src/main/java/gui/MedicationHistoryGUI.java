package gui;
import services.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.AWTEventMulticaster.add;
import static services.MedicationManage.loadFont;

public class MedicationHistoryGUI extends JPanel {

    private User currentUser;

    public MedicationHistoryGUI(User user) {
        this.currentUser = user;
        initializeGUI();
    }

    public void initializeGUI() {
        Font applicationFont = loadFont();

        JPanel medicationHistoryPanel = new JPanel();
        medicationHistoryPanel.setPreferredSize(new Dimension(980, 600));

        JPanel medicationHistoryPanelContents = new JPanel();
        medicationHistoryPanelContents.setPreferredSize(new Dimension(980, 600));

        JLabel medicationHistoryGUISubheading = new JLabel("MEDICATION HISTORY");
        medicationHistoryGUISubheading.setBounds(10, 300, 100,20);
        medicationHistoryPanel.add(medicationHistoryGUISubheading);

        medicationHistoryPanel.add(medicationHistoryPanelContents);
        add(medicationHistoryPanel);
    }


    private Font loadFont() {
        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/main/resources/fonts/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);
        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            throw new RuntimeException(e);
        }
        return applicationFont;
    }
}
