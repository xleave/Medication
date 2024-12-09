package gui;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import services.*;

public class MainGUI {

    // Main window frame
    private JFrame mainWindow = new JFrame("Medication Reminder App");

    // Panels and layouts
    private JPanel staticPanel = new JPanel();
    private JPanel contentPanel = new JPanel();
    private HomeGUI homePanel;

    private JPanel medicationPanel = new JPanel();
    private JPanel logPanel = new JPanel();
    private JPanel helpPanel = new JPanel();
    private AboutGUI aboutPanel; // Changed from JPanel to AboutGUI
    private CardLayout cardLayout = new CardLayout();
    private JPanel adminManagePanel;// Added: AdminManageGUI panel
    private User currentUser; // Store current logged-in user
    private SettingGUI settingsPanel;

    public MainGUI(User user) {
        this.currentUser = user;
    }

    public void displayMainGUI() {
        // Set up fonts
        Font applicationFont;
        try {
            applicationFont = Font
                    .createFont(Font.TRUETYPE_FONT,
                            new File("src/main/resources/fonts/RobotoCondensed-VariableFont_wght.ttf"))
                    .deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);
        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            applicationFont = new Font("Arial", Font.PLAIN, 16);
        }

        // Set up application icon
        BufferedImage applicationImage;
        try {
            applicationImage = ImageIO.read(new File("src/main/resources/icons/App Icon.png"));
            mainWindow.setIconImage(applicationImage);
        } catch (IOException e) {
            System.out.println("Application icon not found!");
        }

        // Set up main window
        mainWindow.setSize(1000, 750);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setLayout(null);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // User info label
        JLabel userInfoTitle = new JLabel("User: " + currentUser.getName());
        userInfoTitle.setBounds(10, 10, 200, 15);
        userInfoTitle.setFont(applicationFont);

        // Application title
        JLabel applicationTitle = new JLabel("Medication Reminder App");
        applicationTitle.setBounds(400, 0, 400, 50);
        applicationTitle.setFont(applicationFont);

        // Date and time labels
        JLabel dateTimeTitle = new JLabel("Date and Time:");
        dateTimeTitle.setBounds(800, 0, 200, 15);
        dateTimeTitle.setFont(applicationFont);

        LocalDate currentDateTime = LocalDate.now();
        JLabel currentDateTitle = new JLabel(currentDateTime.toString());
        currentDateTitle.setBounds(800, 15, 200, 15);
        currentDateTitle.setFont(applicationFont);

        // Static panel setup
        staticPanel.setBounds(0, 0, 1000, 80);
        staticPanel.setLayout(null);

        // Buttons
        JButton homePanelButton = new JButton("Home");
        JButton medicationsPanelButton = new JButton("Medications");
        JButton historyPanelButton = new JButton("Medication History");
        JButton helpPanelButton = new JButton("Help");
        JButton aboutPanelButton = new JButton("About");
        JButton settingsPanelButton = new JButton("Settings");

        // Added: Administrator Management Button
        JButton adminManagePanelButton = new JButton("Admin Manage");

        // Setting the button font
        homePanelButton.setFont(applicationFont);
        medicationsPanelButton.setFont(applicationFont);
        historyPanelButton.setFont(applicationFont);
        helpPanelButton.setFont(applicationFont);
        aboutPanelButton.setFont(applicationFont);
        settingsPanelButton.setFont(applicationFont);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 7)); // Modify to accommodate new buttons
        buttonPanel.setBounds(10, 50, 980, 30);
        buttonPanel.add(homePanelButton);
        buttonPanel.add(medicationsPanelButton);
        buttonPanel.add(historyPanelButton);
        buttonPanel.add(helpPanelButton);
        buttonPanel.add(aboutPanelButton);
        buttonPanel.add(settingsPanelButton);
        // If the current user is an administrator, add an administrator management button
        if (currentUser.isAdmin()) {
            buttonPanel.add(adminManagePanelButton);
        }
        //
        if (currentUser.isAdmin()) {
            buttonPanel.remove(historyPanelButton);
        }
        // Add components to the static panel
        staticPanel.add(userInfoTitle);
        staticPanel.add(applicationTitle);
        staticPanel.add(dateTimeTitle);
        staticPanel.add(currentDateTitle);
        staticPanel.add(buttonPanel);

        // Content panel setup
        contentPanel.setLayout(cardLayout);
        contentPanel.setBounds(10, 100, 980, 600);

        // Initialize panels and pass user object
        homePanel = new HomeGUI(currentUser);// Assume MedicationGUI, LogGUI, HelpGUI, SettingsGUI are defined similarly
        medicationPanel = new MedicationGUI(currentUser);// logPanel = new LogGUI();
        logPanel = new MedicationHistoryGUI(currentUser);
        helpPanel = new HelpGUI();// settingsPanel = new SettingsGUI();
        aboutPanel = new AboutGUI(); // Initialize AboutGUI panel
        settingsPanel = new SettingGUI(currentUser, mainWindow);

        // New: Initialize Administrator Management Panel
        if (currentUser.isAdmin()) {
            adminManagePanel = new AdminManageGUI(currentUser);
        }

        // Add the panel to the content panel
        contentPanel.add(homePanel, "1");
        contentPanel.add(medicationPanel, "2");
        contentPanel.add(logPanel, "3");
        contentPanel.add(helpPanel, "4");
        contentPanel.add(aboutPanel, "5");
        contentPanel.add(settingsPanel, "6");
        // If the current user is an administrator, add an administrator admin panel
        if (currentUser.isAdmin()) {
            contentPanel.add(adminManagePanel, "7");
        }

        // Button Event Listener
        homePanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "1");
            }
        });

        medicationsPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "2");
            }
        });

        historyPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "3");
            }
        });

        helpPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "4");
            }
        });

        aboutPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "5");
            }
        });

        settingsPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "6");
            }
        });
        // Added: Administrator management button event listener
        adminManagePanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "7");
            }
        });
        // Add panels to main window
        mainWindow.add(staticPanel);
        mainWindow.add(contentPanel);

        mainWindow.setVisible(true);
    }
}