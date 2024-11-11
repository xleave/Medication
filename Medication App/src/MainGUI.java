import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDate;

public class MainGUI extends JFrame {

    public void displayMainGUI() {

        //Font formatting
        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT, new File("Medication App/src/EB_Garamond,Roboto_Condensed/Roboto_Condensed/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);

        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            throw new RuntimeException(e);
        }

        //Windows, Panels, Areas.
        JFrame mainWindow = new JFrame("Medication Reminder App");
        JPanel mainPanel = new JPanel();
        JPanel navigationPanel = new JPanel();

        JSeparator horizontalSpacer1 = new JSeparator(SwingConstants.HORIZONTAL);
        JSeparator horizontalSpacer2 = new JSeparator(SwingConstants.HORIZONTAL);

        //Title and Subtitle
        JLabel applicationTitle = new JLabel("Medication Reminder App");
        applicationTitle.setBounds(425, 0, 1000, 50);
        applicationTitle.setFont(applicationFont);
        applicationTitle.setVisible(true);

        //User Info
        JLabel userInfoTitle = new JLabel("User: " );
        userInfoTitle.setBounds(0, 0 , 1000, 15);
        userInfoTitle.setFont(applicationFont);
        userInfoTitle.setVisible(true);

        //Date and Time
        JLabel dateTimeTitle = new JLabel("Date and Time:");
        dateTimeTitle.setBounds(900, 0, 1000, 15);
        dateTimeTitle.setFont(applicationFont);
        dateTimeTitle.setVisible(true);

        LocalDate currentDateTime = LocalDate.now();
        JLabel currentDateTitle = new JLabel(currentDateTime.toString());
        currentDateTitle.setBounds(900, 15, 1000, 15);
        currentDateTitle.setVisible(true);

        //Window
        mainWindow.setSize(1000, 750);
        mainWindow.setBackground(Color.white);
        mainWindow.setResizable(false);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainWindow.setVisible(true);

        //Main Panel
        mainPanel.setSize(1000, 750);
        mainPanel.setBackground(Color.white);

        horizontalSpacer1.setBounds(10, 50, 980, 20);
        horizontalSpacer1.setBackground(Color.BLACK);
        horizontalSpacer1.setVisible(true);

        JLabel submenuTitle = new JLabel("Home");
        submenuTitle.setBounds(500, 40, 1000, 50);
        submenuTitle.setFont(applicationFont);
        submenuTitle.setVisible(true);

        horizontalSpacer2.setBounds(10, 70, 980, 20);
        horizontalSpacer2.setBackground(Color.BLACK);
        horizontalSpacer2.setVisible(true);


        JMenuBar jmbar = new JMenuBar();
        JMenu jmHomeMenu = new JMenu("Home");
        JMenu jmMedicnes= new JMenu("Medications");
        JMenu jmLog = new JMenu("Log");
        JMenuItem jmihomepage = new JMenuItem("Main Page");
        JMenuItem jmiadd = new JMenuItem("Add Medicines");
        JMenuItem jmiedit = new JMenuItem("Edit Medicines");
        JMenuItem jmilog = new JMenuItem("Help");
        jmbar.add(jmHomeMenu);
        jmbar.add(jmMedicnes);
        jmbar.add(jmLog);
        jmHomeMenu.add(jmihomepage);
        jmMedicnes.add(jmiedit);
        jmMedicnes.add(jmiadd);
        jmLog.add(jmilog);
        this.setJMenuBar(jmbar);
        JPanel menuPanel = new JPanel();
        menuPanel.add(jmbar);
        menuPanel.setBounds(0, 90, 1000, 50);
        menuPanel.setVisible(true);

        //Adding and rendering.
        mainPanel.add(horizontalSpacer1);
        mainPanel.add(applicationTitle);
        mainPanel.add(userInfoTitle);
        mainPanel.add(dateTimeTitle);
        mainPanel.add(currentDateTitle);
        mainPanel.add(submenuTitle);
        mainPanel.add(menuPanel);
        mainPanel.add(horizontalSpacer2);
        mainPanel.add(navigationPanel);
        mainPanel.setVisible(true);
        mainWindow.add(mainPanel);

    }


}

