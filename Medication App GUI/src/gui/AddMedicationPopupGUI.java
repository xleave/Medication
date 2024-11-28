package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddMedicationPopupGUI extends JPanel {

    public AddMedicationPopupGUI() {

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

        JFrame addMedicationFrame = new JFrame("Add Medication");
        addMedicationFrame.setSize(400, 600);

        JPanel addMedicationPanelContents = new JPanel();
        addMedicationPanelContents.setLayout(null);

        JLabel addMedicationTitle = new JLabel("Add New Medication");
        addMedicationTitle.setFont(applicationFont);
        addMedicationTitle.setBounds(125, 0, 150, 50);
        addMedicationPanelContents.add(addMedicationTitle);

        //Logic to add item field headers dynamically to window.
        String[] inputFieldHeaders = {"Medication Name", "Dosage", "Quantity", "Time to be taken at", "How often?", "Maximum daily"};
        int headerCounter = 0;
        int headerYPositionSpacing = 100;

        while (headerCounter < inputFieldHeaders.length) {

            JLabel inputFieldHeader = new JLabel(inputFieldHeaders[headerCounter]);
            inputFieldHeader.setBounds(20,headerYPositionSpacing, 250, 20);
            addMedicationPanelContents.add(inputFieldHeader);
            headerYPositionSpacing += 75;
            headerCounter++;

        }
        //Adding text boxes
        JTextField medicationNameTextBox = new JTextField();
        medicationNameTextBox.setBounds(20, 120, 250, 20);
        addMedicationPanelContents.add(medicationNameTextBox);

        JTextField dosageTextBox = new JTextField();
        dosageTextBox.setBounds(20, 195, 50, 20);
        addMedicationPanelContents.add(dosageTextBox);

        JTextField quantityNameTextBox = new JTextField();
        quantityNameTextBox.setBounds(20, 270, 50, 20);
        addMedicationPanelContents.add(quantityNameTextBox);

        JTextField timeTakenTextBox = new JTextField();
        timeTakenTextBox.setBounds(20, 345, 250, 20);
        addMedicationPanelContents.add(timeTakenTextBox);

        JTextField howOftenTextBox = new JTextField();
        howOftenTextBox.setBounds(20, 420, 50, 20);
        addMedicationPanelContents.add(howOftenTextBox);

        JTextField maximumDailyTextBox = new JTextField();
        maximumDailyTextBox.setBounds(20, 495, 50, 20);
        addMedicationPanelContents.add(maximumDailyTextBox);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(applicationFont);
        cancelButton.setBounds(100, 520, 100, 50);
        addMedicationPanelContents.add(cancelButton);

        JButton acceptButton = new JButton("Add Medicine");
        acceptButton.setFont(applicationFont);
        acceptButton.setBounds(205, 520, 100, 50);
        addMedicationPanelContents.add(acceptButton);

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(acceptButton, "Medication has been added to database.", "MEDICATION ADDED", JOptionPane.INFORMATION_MESSAGE);
                addMedicationFrame.dispose();

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMedicationFrame.dispose();
            }
        });


        addMedicationFrame.add(addMedicationPanelContents);
        addMedicationFrame.setVisible(true);


    }
}
