package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import services.MedicationAlert;
import services.MedicationScheduler;
import services.TextToSpeech;
import services.User;

public class MainGUI {

    private TextToSpeech tts;

    private MedicationAlert medicationAlert;

    private MedicationScheduler medicationScheduler;
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
    private JPanel adminManagePanel; // Added: AdminManageGUI panel
    private User currentUser; // Store current logged-in user
    private SettingGUI settingsPanel;

    public MainGUI(User user) {
        this.currentUser = user;
        this.tts = new TextToSpeech();
        this.medicationScheduler = new MedicationScheduler();
        this.medicationAlert = new MedicationAlert(user.getName(), medicationScheduler, tts);
    }

    public void displayMainGUI() {
        // Set up fonts
        Font applicationFont;
        try {
            applicationFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File("src/main/resources/fonts/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
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
        } catch (IOException e) {
            System.out.println("Application icon not found!");
            applicationImage = null;
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
        JLabel dateTimeTitle = new JLabel("Date:");
        dateTimeTitle.setBounds(800, 0, 200, 15);
        dateTimeTitle.setFont(applicationFont);

        LocalDate currentDate = LocalDate.now();
        JLabel currentDateTitle = new JLabel(currentDate.toString());
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
        adminManagePanelButton.setFont(applicationFont);

        // Button panel
        JPanel buttonPanel = new JPanel();
        if (currentUser.isAdmin()) {
            buttonPanel.setLayout(new GridLayout(1, 7)); // Modify to accommodate new buttons
        } else {
            buttonPanel.setLayout(new GridLayout(1, 6)); // Without admin button
        }
        buttonPanel.setBounds(10, 50, 980, 30);
        buttonPanel.add(homePanelButton);
        buttonPanel.add(medicationsPanelButton);
        buttonPanel.add(historyPanelButton);
        buttonPanel.add(helpPanelButton);
        buttonPanel.add(aboutPanelButton);
        buttonPanel.add(settingsPanelButton);
        // If the current user is an administrator, add an administrator management
        // button
        if (currentUser.isAdmin()) {
            buttonPanel.add(adminManagePanelButton);
        }
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
        homePanel = new HomeGUI(currentUser); // Assume MedicationGUI, LogGUI, HelpGUI, SettingsGUI are defined
                                              // similarly
        medicationPanel = new MedicationGUI(currentUser); // logPanel = new LogGUI();
        logPanel = new MedicationHistoryGUI(currentUser);
        helpPanel = new HelpGUI();
        aboutPanel = new AboutGUI(); // Initialize AboutGUI panel
        settingsPanel = new SettingGUI(currentUser, mainWindow);

        // New: Initialize Administrator Management Panel
        if (currentUser.isAdmin()) {
            adminManagePanel = new AdminManageGUI(currentUser);
        }

        // Add the panels to the content panel
        contentPanel.add(homePanel, "Home");
        contentPanel.add(medicationPanel, "Medications");
        contentPanel.add(logPanel, "Medication History");
        contentPanel.add(helpPanel, "Help");
        contentPanel.add(aboutPanel, "About");
        contentPanel.add(settingsPanel, "Settings");
        // If the current user is an administrator, add an administrator admin panel
        if (currentUser.isAdmin()) {
            contentPanel.add(adminManagePanel, "Admin Manage");
        }

        // Add Action Listeners for Buttons
        homePanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Home");
            }
        });

        medicationsPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Medications");
            }
        });

        historyPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Medication History");
            }
        });

        helpPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Help");
            }
        });

        aboutPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "About");
            }
        });

        settingsPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Settings");
            }
        });

        if (currentUser.isAdmin()) {
            adminManagePanelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, "Admin Manage");
                }
            });
        }

        // Add panels to the main window
        mainWindow.add(staticPanel);
        mainWindow.add(contentPanel);

        // Make the window visible
        mainWindow.setVisible(true);
    }
}