package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import services.User;

public class MedicationGUI extends JPanel {

    private User currentUser;
    private JScrollPane tableScrollPane; // Add new member variables

    public MedicationGUI(User user) {
        this.currentUser = user;

        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/resources/fonts/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);

        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            throw new RuntimeException(e);
        }

        JPanel medicationPanel = new JPanel(new BorderLayout());
        medicationPanel.setPreferredSize(new Dimension(980, 600));

        JPanel medicationPanelContents = new JPanel();
        medicationPanelContents.setPreferredSize(new Dimension(980, 600));
        medicationPanelContents.setLayout(null);

        JLabel medicationSubmenuHeading = new JLabel("MEDICATIONS");
        medicationSubmenuHeading.setBounds(470, -20, 200, 50);
        medicationSubmenuHeading.setFont(applicationFont);
        medicationPanelContents.add(medicationSubmenuHeading);

        // Button Settings
        JButton addMedicationButton = new JButton("Add Medication");
        addMedicationButton.setBounds(20, 150, 200, 50);
        medicationPanelContents.add(addMedicationButton);

        JButton editMedicationButton = new JButton("Edit Medication");
        editMedicationButton.setBounds(20, 250, 200, 50);
        medicationPanelContents.add(editMedicationButton);

        JButton removeMedicationButton = new JButton("Remove Medication");
        removeMedicationButton.setBounds(20, 350, 200, 50);
        medicationPanelContents.add(removeMedicationButton);

        JLabel userMedicationLabel;
        if (currentUser.isAdmin()) {
            userMedicationLabel = new JLabel("All Users' Medications");
        } else {
            userMedicationLabel = new JLabel(currentUser.getName() + "'s Current Medications");
        }
        userMedicationLabel.setFont(applicationFont);
        userMedicationLabel.setBounds(500, 60, 300, 50);
        medicationPanelContents.add(userMedicationLabel);

        addMedicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMedicationPopupGUI addMedicationPopupGUI = new AddMedicationPopupGUI(currentUser);
            }
        });

        // Determine if the current user is an administrator
        if (currentUser.isAdmin()) {
            // Display medication information for all users
            String[] medicationTableHeaders = { "User", "Name", "Dosage", "Quantity", "Time", "Take Daily",
                    "Maximum Daily" };
            ArrayList<Object[]> medicationDataList = new ArrayList<>();

            // Iterate over all users
            File medicationDir = new File("src/resources/medications");
            File[] medicationFiles = medicationDir.listFiles((dir, name) -> name.endsWith("_medications.csv"));

            if (medicationFiles != null) {
                for (File medFile : medicationFiles) {
                    String fileName = medFile.getName();
                    String userName = fileName.substring(0, fileName.indexOf("_medications.csv"));
                    try {
                        BufferedReader medicationFileReader = new BufferedReader(new FileReader(medFile));
                        String line;
                        while ((line = medicationFileReader.readLine()) != null) {
                            String[] medData = line.split(",");
                            // Prefix the data with the username
                            Object[] rowData = new Object[7];
                            rowData[0] = userName;

                            // Make sure don't exceed the length of rowData
                            int copyLength = Math.min(medData.length, 6);
                            System.arraycopy(medData, 0, rowData, 1, copyLength);

                            // If medData.length < 6, fill remaining positions with empty string
                            for (int i = 1 + copyLength; i < 7; i++) {
                                rowData[i] = "";
                            }

                            medicationDataList.add(rowData);
                        }
                        medicationFileReader.close();
                    } catch (IOException e) {
                        System.out.println("Error reading user " + userName + " 's medication file.");
                    }
                }
            }

            Object[][] medicationTableData = medicationDataList.toArray(new Object[0][]);

            JTable medicationTable = new JTable(medicationTableData, medicationTableHeaders);
            medicationTable.setBounds(400, 400, 600, 350);

            JScrollPane tableScrollPane = new JScrollPane(medicationTable);
            tableScrollPane.setBounds(300, 100, 600, 350);
            medicationPanelContents.add(tableScrollPane);

        } else {
            // Original logic to display current user's medication information
            String medicationFile = "src/resources/medications/" + currentUser.getName() + "_medications.csv";
            String[] medicationTableHeaders = { "Name", "Dosage", "Quantity", "Time", "Take Daily",
                    "Maximum Daily" };

            try {
                BufferedReader medicationFileReader = new BufferedReader(new FileReader(medicationFile));
                ArrayList<String> medicationList = new ArrayList<>();
                String temporaryString;
                while ((temporaryString = medicationFileReader.readLine()) != null) {
                    medicationList.add(temporaryString);
                }

                Object[][] medicationTableData = new Object[medicationList.size()][6];
                for (int medicationListIndex = 0; medicationListIndex < medicationList.size(); medicationListIndex++) {
                    String[] medData = medicationList.get(medicationListIndex).split(",");
                    // Ensure that the length of the medicationTableData is not exceeded.
                    if (medData.length > 6) {
                        System.arraycopy(medData, 0, medicationTableData[medicationListIndex], 0, 6);
                    } else {
                        System.arraycopy(medData, 0, medicationTableData[medicationListIndex], 0, medData.length);
                        // Fill the remaining positions with empty strings
                        for (int i = medData.length; i < 6; i++) {
                            medicationTableData[medicationListIndex][i] = "";
                        }
                    }
                }

                JTable medicationTable = new JTable(medicationTableData, medicationTableHeaders);
                medicationTable.setBounds(400, 400, 600, 350);

                JScrollPane tableScrollPane = new JScrollPane(medicationTable);
                tableScrollPane.setBounds(300, 100, 600, 350);
                medicationPanelContents.add(tableScrollPane);

            } catch (FileNotFoundException medicationFileNotFound) {
                System.out.println(
                        "The medication file for this user could not be found! Please check the path and try again.");
                // Create an empty form to avoid the program crashing
                Object[][] medicationTableData = new Object[0][6];
                JTable medicationTable = new JTable(medicationTableData, medicationTableHeaders);
                JScrollPane tableScrollPane = new JScrollPane(medicationTable);
                tableScrollPane.setBounds(300, 100, 600, 350);
                medicationPanelContents.add(tableScrollPane);
            } catch (IOException medicationFileCannotBeRead) {
                System.out.println("Error reading medication file.");
            }
        }

        medicationPanel.add(medicationPanelContents);
        add(medicationPanel);

        // Test user functions
        System.out.println(currentUser.getName());
    }
}