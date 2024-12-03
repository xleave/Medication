package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import services.User;
import services.MedicationManage;

public class AddMedicationPopupGUI extends JPanel {

    private User currentUser;
    private JTextField userNameTextBox; // Declare userNameTextBox as a member variable.
    private JTextField[] textFields;
    private Font applicationFont;

    public AddMedicationPopupGUI(User user) {

        this.currentUser = user;
        initializeFont();
        createAndShowGUI();
    }

    private void initializeFont() {
        try {
            applicationFont = MedicationManage.loadFont("src/main/resources/fonts/RobotoCondensed-VariableFont_wght.ttf");
        } catch (RuntimeException e) {
            System.out.println("Font could not be found!");
            throw e;
        }
    }

//        Font applicationFont;
//        try {
//            applicationFont = Font.createFont(Font.TRUETYPE_FONT,
//                    new File("src/resources/fonts/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
//            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//            ge.registerFont(applicationFont);
//
//        } catch (IOException | FontFormatException e) {
//            System.out.println("Font could not be found!");
//            throw new RuntimeException(e);
//        }

    private void createAndShowGUI() {
        JFrame addMedicationFrame = new JFrame("Add Medication");
        addMedicationFrame.setSize(400, 650);

        JPanel addMedicationPanelContents = new JPanel();
        addMedicationPanelContents.setLayout(null);

        addTitleLabel(addMedicationPanelContents);
        int yPosition = 60;

        if (currentUser.isAdmin()) {
            yPosition = addUserNameField(addMedicationPanelContents, yPosition);
        }

        yPosition = addMedicationFields(addMedicationPanelContents, yPosition);
        addButtons(addMedicationPanelContents, yPosition, addMedicationFrame);

        addMedicationFrame.add(addMedicationPanelContents);
        addMedicationFrame.setVisible(true);
    }

    private void addTitleLabel(JPanel panel) {
        JLabel addMedicationTitle = new JLabel("Add New Medication");
        addMedicationTitle.setFont(applicationFont);
        addMedicationTitle.setBounds(125, 10, 200, 30);
        panel.add(addMedicationTitle);
    }

    private int addUserNameField(JPanel panel, int yPosition) {
        JLabel userNameLabel = new JLabel("Username");
        userNameLabel.setBounds(20, yPosition, 250, 20);
        panel.add(userNameLabel);

        yPosition += 25;

        userNameTextBox = new JTextField();
        userNameTextBox.setBounds(20, yPosition, 250, 20);
        panel.add(userNameTextBox);

        return yPosition + 35;
    }

    private int addMedicationFields(JPanel panel, int yPosition) {
        String[] inputFieldHeaders = {"Medication Name", "Dosage", "Quantity", "Time to be taken at", "How often?",
                "Maximum daily"};
        textFields = new JTextField[inputFieldHeaders.length];

        for (int i = 0; i < inputFieldHeaders.length; i++) {
            JLabel inputFieldHeader = new JLabel(inputFieldHeaders[i]);
            inputFieldHeader.setBounds(20, yPosition, 250, 20);
            panel.add(inputFieldHeader);

            yPosition += 25;

            JTextField textField = new JTextField();
            textField.setBounds(20, yPosition, 250, 20);
            panel.add(textField);

            textFields[i] = textField;

            yPosition += 35;
        }
        return yPosition;
    }

    private void addButtons(JPanel panel, int yPosition, JFrame frame) {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(applicationFont);
        cancelButton.setBounds(80, yPosition, 100, 30);
        panel.add(cancelButton);

        JButton acceptButton = new JButton("Add Medicine");
        acceptButton.setFont(applicationFont);
        acceptButton.setBounds(190, yPosition, 150, 30);
        panel.add(acceptButton);

        addActionListeners(acceptButton, cancelButton, frame);
    }

    private void addActionListeners(JButton acceptButton, JButton cancelButton, JFrame frame) {
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddMedication(acceptButton, frame);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    private void handleAddMedication(JButton acceptButton, JFrame frame) {
        String targetUserName = currentUser.getName();

        if (currentUser.isAdmin()) {
            if (userNameTextBox != null) {
                targetUserName = userNameTextBox.getText().trim();
                if (targetUserName.isEmpty()) {
                    JOptionPane.showMessageDialog(acceptButton, "Please enter a user name.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(acceptButton, "User name field not initialized", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (!validateFields(acceptButton)) {
            return;
        }

        MedicationManage.saveMedicationInfo(targetUserName, textFields, acceptButton, frame);
    }

    private boolean validateFields(JButton acceptButton) {
        for (JTextField textField : textFields) {
            if (textField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(acceptButton, "Please fill in all fields.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
}



//        JFrame addMedicationFrame = new JFrame("Add Medication");
//        addMedicationFrame.setSize(400, 650);
//
//        JPanel addMedicationPanelContents = new JPanel();
//        addMedicationPanelContents.setLayout(null);
//
//        JLabel addMedicationTitle = new JLabel("Add New Medication");
//        addMedicationTitle.setFont(applicationFont);
//        addMedicationTitle.setBounds(125, 10, 200, 30);
//        addMedicationPanelContents.add(addMedicationTitle);
//
//        int yPosition = 60; // Initial y-coordinate
//
//        if (currentUser.isAdmin()) {
//            JLabel userNameLabel = new JLabel("Username");
//            userNameLabel.setBounds(20, yPosition, 250, 20);
//            addMedicationPanelContents.add(userNameLabel);
//
//            yPosition += 25;
//
//            userNameTextBox = new JTextField(); // Initialize the userNameTextBox
//            userNameTextBox.setBounds(20, yPosition, 250, 20);
//            addMedicationPanelContents.add(userNameTextBox);
//
//            yPosition += 35;
//        }
//
//        // Enter field labels and text boxes
//        String[] inputFieldHeaders = { "Medication Name", "Dosage", "Quantity", "Time to be taken at", "How often?",
//                "Maximum daily" };
//        JTextField[] textFields = new JTextField[inputFieldHeaders.length];
//
//        for (int i = 0; i < inputFieldHeaders.length; i++) {
//            JLabel inputFieldHeader = new JLabel(inputFieldHeaders[i]);
//            inputFieldHeader.setBounds(20, yPosition, 250, 20);
//            addMedicationPanelContents.add(inputFieldHeader);
//
//            yPosition += 25;
//
//            JTextField textField = new JTextField();
//            textField.setBounds(20, yPosition, 250, 20);
//            addMedicationPanelContents.add(textField);
//
//            textFields[i] = textField;
//
//            yPosition += 35;
//        }
//
//        JButton cancelButton = new JButton("Cancel");
//        cancelButton.setFont(applicationFont);
//        cancelButton.setBounds(80, yPosition, 100, 30);
//        addMedicationPanelContents.add(cancelButton);
//
//        JButton acceptButton = new JButton("Add Medicine");
//        acceptButton.setFont(applicationFont);
//        acceptButton.setBounds(190, yPosition, 150, 30);
//        addMedicationPanelContents.add(acceptButton);
//
//        acceptButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String targetUserName = currentUser.getName();
//
//                if (currentUser.isAdmin()) {
//                    // Get the username entered by the administrator
//                    if (userNameTextBox != null) {
//                        targetUserName = userNameTextBox.getText().trim();
//                        if (targetUserName.isEmpty()) {
//                            JOptionPane.showMessageDialog(acceptButton, "Please enter a user name.", "incorrect",
//                                    JOptionPane.ERROR_MESSAGE);
//                            return;
//                            }
//                        } else {
//                            JOptionPane.showMessageDialog(acceptButton, "User name field not initialized", "incorrect",
//                        JOptionPane.ERROR_MESSAGE);
//                            return;
//                        }
//                    }
//
//                // Get drug information
//                String medicationName = textFields[0].getText().trim();
//                String dosage = textFields[1].getText().trim();
//                String quantity = textFields[2].getText().trim();
//                String timeToTake = textFields[3].getText().trim();
//                String howOften = textFields[4].getText().trim();
//                String maximumDaily = textFields[5].getText().trim();
//
//                // Get drug information
//                if (medicationName.isEmpty() || dosage.isEmpty() || quantity.isEmpty() || timeToTake.isEmpty()
//                        || howOften.isEmpty() || maximumDaily.isEmpty()) {
//                    JOptionPane.showMessageDialog(acceptButton, "Please fill in all fields.", "Error",
//                            JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//
//                // Save medication information to the corresponding user's medication file
//                String medicationFilePath = "src/resources/medications/" + targetUserName + "_medications.csv";
//                File medicationFile = new File(medicationFilePath);
//                try {
//                    // Check if the user's medication file exists
//                    if (!medicationFile.exists()) {
//                        medicationFile.createNewFile();
//                    }
//
//                    FileWriter fw = new FileWriter(medicationFile, true);
//                    BufferedWriter bw = new BufferedWriter(fw);
//                    PrintWriter out = new PrintWriter(bw);
//
//                    // Write drug information
//                    out.println(medicationName + "," + dosage + "," + quantity + "," + timeToTake + "," + howOften + ","
//                            + maximumDaily);
//                    out.close();
//
//                    JOptionPane.showMessageDialog(acceptButton, "Medication has been added.", "Success",
//                            JOptionPane.INFORMATION_MESSAGE);
//                    addMedicationFrame.dispose();
//
//                } catch (IOException ex) {
//                    JOptionPane.showMessageDialog(acceptButton, "An error occurred while saving medication.", "Error",
//                            JOptionPane.ERROR_MESSAGE);
//                    ex.printStackTrace();
//                }
//            }
//        });
//
//        cancelButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addMedicationFrame.dispose();
//            }
//        });
//
//        addMedicationFrame.add(addMedicationPanelContents);
//        addMedicationFrame.setVisible(true);
//
//    }
//}