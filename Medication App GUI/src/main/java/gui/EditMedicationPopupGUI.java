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

        //Final rendering
        editMedicationFrame.add(editMedicationPanelContents);
        editMedicationFrame.setVisible(true);

    }


}
