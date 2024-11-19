import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class MainGUI extends JFrame {

    public JLayeredPane baseLayerPanel;

    public void displayMainGUI()  {

        //Font formatting
        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\spars\\OneDrive\\Desktop\\Extreme Folder\\Group_Project\\Medication App\\src\\EB_Garamond,Roboto_Condensed\\Roboto_Condensed\\RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);

        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            throw new RuntimeException(e);
        }

        //Windows, Panels, Areas.
        JFrame mainWindow = new JFrame("Medication Reminder App");
        JPanel mainPanel = new JPanel();

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
        mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainWindow.setLayout(null);
        mainWindow.setLocationRelativeTo(null);

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

        //Separate screens within the app that get displayed on menu selection.
        baseLayerPanel = new JLayeredPane();
        baseLayerPanel.setBounds(0, 50, 1000, 750);
        mainWindow.getContentPane().add(baseLayerPanel);

        //Screen 1
        JPanel medicationPanel = new JPanel();
        medicationPanel.setBackground(Color.green);
        medicationPanel.setBounds(0, 50, 1000, 750);
        baseLayerPanel.add(medicationPanel);

        //Screen 2
        JPanel logPanel = new JPanel();
        logPanel.setBackground(Color.blue);
        logPanel.setBounds(0, 50, 1000, 750);
        baseLayerPanel.add(logPanel);

        //Screen 3
        JPanel helpPanel = new JPanel();
        helpPanel.setBackground(Color.pink);
        helpPanel.setBounds(0, 50, 1000, 750);
        baseLayerPanel.add(helpPanel);

        //Navigation.
        JMenuBar applicationMenu = new JMenuBar();

        applicationMenu.setBounds(10, 100, 980, 75);
        applicationMenu.setBackground(Color.darkGray);
        JMenu homeMenu = new JMenu("Home");
        JMenu medicationMenu = new JMenu("Medications");
        JMenu logMenu = new JMenu("Logs");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem mainPageItem = new JMenuItem("Main Page");
        JMenuItem addMedicationItem = new JMenuItem("Add Medication");
        JMenuItem editMedicicationItem = new JMenuItem("Edit Medications");
        JMenuItem removeMedicicationItem = new JMenuItem("Remove Medications");
        JMenuItem viewLogsItem = new JMenuItem("View Logs");
        JMenuItem helpItem = new JMenuItem("Help");
        JMenuItem aboutItem = new JMenuItem("About");

        medicationMenu.add(addMedicationItem);
        medicationMenu.add(editMedicicationItem);
        medicationMenu.add(removeMedicicationItem);
        logMenu.add(viewLogsItem);
        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);

        applicationMenu.add(homeMenu);
        applicationMenu.add(medicationMenu);
        applicationMenu.add(logMenu);
        applicationMenu.add(helpMenu);

        mainWindow.setJMenuBar(applicationMenu);

        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionPerformed = e.getActionCommand();
                switch (actionPerformed) {
                    case "Home":
                        System.out.println("Home item Clicked");
                }
            }
        };

        //Buttons
        homeMenu.add(mainPageItem);
        mainPageItem.setActionCommand("Home");
        mainPageItem.addActionListener(menuListener);


        //Adding and rendering.
        mainPanel.add(horizontalSpacer1);
        mainPanel.add(applicationTitle);
        mainPanel.add(userInfoTitle);
        mainPanel.add(dateTimeTitle);
        mainPanel.add(currentDateTitle);
        mainPanel.add(submenuTitle);
        mainPanel.add(horizontalSpacer2);


        mainWindow.setVisible(true);      //Somewhat problematic?
        mainPanel.setVisible(true);
        mainWindow.add(mainPanel);

    }
    private void switchWindows(JPanel nextPanel) {
        baseLayerPanel.removeAll();
        baseLayerPanel.add(nextPanel);
        baseLayerPanel.repaint();
        baseLayerPanel.revalidate();


    }


}

