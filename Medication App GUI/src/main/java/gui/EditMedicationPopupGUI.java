package gui;

import services.MedicationManage;
import services.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class EditMedicationPopupGUI extends JPanel {

    private User currentUser;

    private Font applicationFont;

    public Vector<String> rowValuueVector;


    public EditMedicationPopupGUI(User user, Vector vector) {
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
        editMedicationFrame.setResizable(false);

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

        JButton cancelEditButton = new JButton("Cancel");
        cancelEditButton.setFont(applicationFont);
        cancelEditButton.setBounds(200, 300, 100, 50);

        cancelEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent cancelEditButtonClicked) {
                editMedicationFrame.dispose();
            }
        });

        editMedicationPanelContents.add(cancelEditButton);

        JButton doEditButton = new JButton("Apply");
        doEditButton.setFont(applicationFont);
        doEditButton.setBounds(300, 300, 100, 50);

        doEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent doEditButtonClicked) {
                JOptionPane.showMessageDialog(editMedicationPanelContents, "Medication has been edited.", "Edit Medication",  JOptionPane.INFORMATION_MESSAGE);
                editMedicationFrame.dispose();
            }
        });

        editMedicationPanelContents.add(doEditButton);

        //Adding all components from others.
        showCurrentDataTable(editMedicationPanelContents);
        addNewDetailTable(editMedicationPanelContents);

        //Final rendering
        editMedicationFrame.add(editMedicationPanelContents);
        editMedicationFrame.setVisible(true);

    }

    public void showCurrentDataTable(JPanel panel) {
        MedicationGUI medicationGUI = new MedicationGUI(currentUser);
        Vector<String> medicationRowFromTable = medicationGUI.getSelectedRowFromTable();
        String[] currentMedHeader = {"Name", "Dosage", "Quantity", "Time", "Frequency", "Maximum"};
        Object[][] currentMedData = new Object[1][6];

        //Testing to see if Vector from other page is being shared accross calls.
        System.out.println(medicationRowFromTable);

        DefaultTableModel currentMedicationInformationModel = new DefaultTableModel(currentMedData, currentMedHeader);
        JTable currentMedicationInformation = new JTable(currentMedicationInformationModel);
        currentMedicationInformation.setBounds(20, 100, 500, 50);

        //currentMedicationInformationModel.setValueAt(rowValuueVector.get(i));

        JScrollPane currentMedicationScrollPane = new JScrollPane(currentMedicationInformation);
        currentMedicationScrollPane.setBounds(20, 100, 500, 50);

        //Inserting values from main table into edit table.
        //currentMedicationInformationModel.setValueAt(rowValuueVector.get(0), 0, 0);
        currentMedicationInformationModel.setValueAt(medicationGUI.getSelectedRowFromTable().get(0), 0, 0);
        panel.add(currentMedicationScrollPane);
    }

    public void addNewDetailTable(JPanel panel) {
        String[] newMedHeader = {"New Name", "New Dosage", "New Quantity", "New Time", "New Frequency", "New Maximum"};
        Object[][] newMedData = new Object[1][6];

        JTable newMedicationInformation = new JTable(newMedData, newMedHeader);
        newMedicationInformation.setBounds(20, 100, 500, 50);
        JScrollPane newMedicationScrollPane = new JScrollPane(newMedicationInformation);
        newMedicationScrollPane.setBounds(20, 200, 500, 50);

        panel.add(newMedicationScrollPane);


    }
}
