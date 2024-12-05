package gui;

import services.MedicationManage;
import services.User;

import javax.swing.*;
import java.awt.*;

public class EditMedicationPopupGUI extends JPanel {

    private User currentUser;

    private Font applicationFont;

    public EditMedicationPopupGUI(User user) {
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

    public void createAndShowGUI() {

        JFrame editMedicationFrame = new JFrame("Edit Medication");
        editMedicationFrame.setSize(600, 400);

        JPanel editMedicationPanelContents = new JPanel();
        editMedicationPanelContents.setLayout(null);

        JLabel editMedicationTitle = new JLabel("Edit Existing Medications");
        editMedicationTitle.setFont(applicationFont);
        editMedicationTitle.setBounds(225, 10, 250, 50);
        editMedicationPanelContents.add(editMedicationTitle);

        JLabel currentMedicationHeading = new JLabel("Current details for " + "med name here");
        currentMedicationHeading.setFont(applicationFont);
        currentMedicationHeading.setBounds(20, 50, 600, 50);
        editMedicationPanelContents.add(currentMedicationHeading);

        JLabel newMedicationHeading = new JLabel("New details for " + "med name here");
        newMedicationHeading.setFont(applicationFont);
        newMedicationHeading.setBounds(20, 150, 600, 50);
        editMedicationPanelContents.add(newMedicationHeading);

        //Adding all components from other.
        addNewDetailLabels(editMedicationPanelContents);
        addNewDetailFields(editMedicationPanelContents);

        //Final rendering
        editMedicationFrame.add(editMedicationPanelContents);
        editMedicationFrame.setVisible(true);

    }

    public void addNewDetailLabels(JPanel panel) {

        JLabel newMedicationNameLabel = new JLabel("New Name");
        newMedicationNameLabel.setFont(applicationFont);
        newMedicationNameLabel.setBounds(20, 200, 150, 20);
        panel.add(newMedicationNameLabel);

        JLabel newMedicationDosageLabel = new JLabel("New Dosage");
        newMedicationDosageLabel.setFont(applicationFont);
        newMedicationDosageLabel.setBounds(220, 200, 150, 20);
        panel.add(newMedicationDosageLabel);

        JLabel newMedicationQuantityLabel = new JLabel("New Quantity");
        newMedicationQuantityLabel.setFont(applicationFont);
        newMedicationQuantityLabel.setBounds(420, 200, 150, 20);
        panel.add(newMedicationQuantityLabel);

        JLabel newMedicationTimeLabel = new JLabel("New Time");
        newMedicationTimeLabel.setFont(applicationFont);
        newMedicationTimeLabel.setBounds(20, 250, 150, 20);
        panel.add(newMedicationTimeLabel);

        JLabel newMedicationFrequencyLabel = new JLabel("New Frequency");
        newMedicationFrequencyLabel.setFont(applicationFont);
        newMedicationFrequencyLabel.setBounds(220, 250, 150, 20);
        panel.add(newMedicationFrequencyLabel);

        JLabel newMedicationMaximumLabel = new JLabel("New Maximum");
        newMedicationMaximumLabel.setFont(applicationFont);
        newMedicationMaximumLabel.setBounds(420, 250, 150, 20);
        panel.add(newMedicationMaximumLabel);
    }

    public void addNewDetailFields(JPanel panel) {

        JTextField newMedicationNameField = new JTextField();
        newMedicationNameField.setBounds(20, 220, 100, 20);
        panel.add(newMedicationNameField);

        JTextField newMedicationDosageField = new JTextField();
        newMedicationDosageField.setBounds(200, 220, 50, 20);
        panel.add(newMedicationDosageField);

        JTextField newMedicationQuantityField = new JTextField();
        newMedicationQuantityField.setBounds(400, 220, 50, 20);
        panel.add(newMedicationQuantityField);

        JTextField newMedicationTimeField = new JTextField();
        newMedicationTimeField.setBounds(200, 250, 50, 20);
        panel.add(newMedicationTimeField);

        JTextField newMedicationFrequencyField = new JTextField();
        newMedicationFrequencyField.setBounds(200, 250, 50, 20);
        panel.add(newMedicationFrequencyField);

        JTextField newMedicationMaximumField = new JTextField();
        newMedicationMaximumField.setBounds(200, 250, 50, 20);
        panel.add(newMedicationMaximumField);


    }


}
