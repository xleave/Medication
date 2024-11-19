import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class MainGUI extends JFrame {

    //Public JFrames, JPanels and JComponents
    JFrame mainWindow = new JFrame("Medication Reminder App");
    JPanel staticPanel = new JPanel();
    JPanel contentPanel = new JPanel();
    JPanel homePanel = new JPanel();
    JPanel medicationPanel = new JPanel();
    JPanel logPanel = new JPanel();
    JPanel helpPanel = new JPanel();
    JPanel aboutPanel = new JPanel();
    CardLayout cardLayout = new CardLayout();

    public void reRenderJFrame() {
        mainWindow.revalidate();
        mainWindow.repaint();
    }

    public void loadOtherJPanel(String name) {
        cardLayout.show(contentPanel, name);
    }


    public void displayMainGUI() {
        //Drawing the static elements.
        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/marley/Library/Mobile Documents/com~apple~CloudDocs/Documents/University Work - NAS/Year 3/CE320 Large Scale Software Systems/Group_Project/Medication App/src/EB_Garamond,Roboto_Condensed/Roboto_Condensed/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);

        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            throw new RuntimeException(e);
        }

        //Window
        mainWindow.setSize(1000, 750);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setLayout(null);

        //Separators
        JSeparator horizontalSpacer1 = new JSeparator(SwingConstants.HORIZONTAL);
        JSeparator horizontalSpacer2 = new JSeparator(SwingConstants.HORIZONTAL);

        //User Info
        JLabel userInfoTitle = new JLabel("User: test user :)");
        userInfoTitle.setBounds(0, 0, 1000, 15);
        userInfoTitle.setFont(applicationFont);
        userInfoTitle.setVisible(true);

        //Title and Subtitle
        JLabel applicationTitle = new JLabel("Medication Reminder App");
        applicationTitle.setBounds(425, 0, 1000, 50);
        applicationTitle.setFont(applicationFont);
        applicationTitle.setVisible(true);

        //Date and Time
        JLabel dateTimeTitle = new JLabel("Date and Time:");
        dateTimeTitle.setBounds(900, 0, 1000, 15);
        dateTimeTitle.setFont(applicationFont);
        dateTimeTitle.setVisible(true);

        //Date and Time Formatting
        LocalDate currentDateTime = LocalDate.now();
        JLabel currentDateTitle = new JLabel(currentDateTime.toString());
        currentDateTitle.setBounds(900, 15, 1000, 15);
        currentDateTitle.setVisible(true);

        //Main Panel
        staticPanel.setSize(1000, 80);

        horizontalSpacer1.setBounds(10, 50, 980, 20);
        horizontalSpacer1.setBackground(Color.BLACK);
        horizontalSpacer1.setVisible(true);

        JLabel submenuTitle = new JLabel();
        submenuTitle.setBounds(500, 40, 1000, 50);
        submenuTitle.setFont(applicationFont);
        submenuTitle.setVisible(true);

        horizontalSpacer2.setBounds(10, 70, 980, 20);
        horizontalSpacer2.setBackground(Color.BLACK);
        horizontalSpacer2.setVisible(true);

        //Buttons
        JButton homePanelButton = new JButton("Home");
        JButton medicationsPanelButton = new JButton("Medications");
        JButton historyPanelButton = new JButton("Medication History");
        JButton helpPanelButton = new JButton("Help");
        JButton aboutPanelButton = new JButton("About");

        homePanelButton.setFont(applicationFont);
        medicationsPanelButton.setFont(applicationFont);
        historyPanelButton.setFont(applicationFont);
        helpPanelButton.setFont(applicationFont);
        aboutPanelButton.setFont(applicationFont);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));;
        buttonPanel.setBounds(200, 40, 600, 50);
        buttonPanel.add(homePanelButton);
        buttonPanel.add(medicationsPanelButton);
        buttonPanel.add(historyPanelButton);
        buttonPanel.add(helpPanelButton);
        buttonPanel.add(aboutPanelButton);

        //Content Panel - Where each screen of the app displays.
        contentPanel.setLayout(cardLayout);
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        contentPanel.setBounds(10, 100, 980, 600);

        contentPanel.add(homePanel, "1");
        contentPanel.add(medicationPanel, "2");
        contentPanel.add(logPanel, "3");
        contentPanel.add(helpPanel, "4");
        contentPanel.add(aboutPanel, "5");

        //Button ActionListener
        homePanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent moveToHomePanel) {
                cardLayout.show(contentPanel, "1");
                reRenderJFrame();
            }
        });

        medicationsPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent moveToMedicationPanel) {
                cardLayout.show(contentPanel, "2" );
                JPanel newMedicationPanel = new MedicationGUI();
                contentPanel.add(newMedicationPanel, "medication");
                loadOtherJPanel("medication");
                reRenderJFrame();
            }
        });

        historyPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent moveToLogPanel) {
                cardLayout.show(contentPanel, "3" );
                reRenderJFrame();
            }
        });

        helpPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent moveToHelpPanel) {
                cardLayout.show(contentPanel, "4");
                reRenderJFrame();
            }
        });

        aboutPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel,"5");
                JPanel newAboutGUI = new AboutGUI();
                contentPanel.add(newAboutGUI, "about");
                loadOtherJPanel("about");
                reRenderJFrame();
            }
        });

        //Adding all items to JPanel and JFrame.
        mainWindow.add(horizontalSpacer1);
        mainWindow.add(userInfoTitle);
        mainWindow.add(applicationTitle);
        mainWindow.add(dateTimeTitle);
        mainWindow.add(currentDateTitle);
        mainWindow.add(submenuTitle);
        mainWindow.add(horizontalSpacer2);
        mainWindow.add(buttonPanel);

        mainWindow.add(contentPanel);


        mainWindow.setVisible(true);
        staticPanel.setVisible(true);
        contentPanel.setVisible(true);
        mainWindow.add(staticPanel);
    }
}