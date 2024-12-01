package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import modules.User;

public class MedicationGUI extends JPanel {

    public MedicationGUI() {

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
        medicationPanelContents.setBounds(470, -20, 100, 50);
        medicationSubmenuHeading.setFont(applicationFont);
        medicationPanelContents.add(medicationSubmenuHeading);

        JButton addMedicationButton = new JButton("Add Medication");
        addMedicationButton.setBounds(20, 150, 200, 50);
        addMedicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMedicationPopupGUI addMedicationPopupGUI = new AddMedicationPopupGUI();
            }
        });
        medicationPanelContents.add(addMedicationButton);

        JButton editMedicationButton = new JButton("Edit Medication");
        editMedicationButton.setBounds(20, 250, 200, 50);
        medicationPanelContents.add(editMedicationButton);

        JButton removeMedicationButton = new JButton("Remove Medication");
        removeMedicationButton.setBounds(20, 350, 200, 50);
        medicationPanelContents.add(removeMedicationButton);

        //Table
        String[] medicationTableHeaders = {"Name", "Dosage", "Quantity", "Time", "Take Daily", "Maximum Daily"};
        String[][] medicationTableData = {
                {"Paracetomol", "500mg", "2", "9am", "8", "8"}
                };

        JTable medicationTable = new JTable(medicationTableData, medicationTableHeaders);
        medicationTable.setBounds(400, 400, 600, 350);

        JScrollPane tableScrollPane = new JScrollPane(medicationTable);
        tableScrollPane.setBounds(300, 100, 600, 350);
        medicationPanelContents.add(tableScrollPane);


        medicationPanel.add(medicationPanelContents);
        add(medicationPanel);

    }
}