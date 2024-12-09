package gui;

import services.MedicationManage;
import services.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class EditMedicationPopupGUI extends JPanel {

    private User currentUser;

    private Vector medicationVector;

    private DefaultTableModel newMedicationInformationModel;

    private Font applicationFont;


    public EditMedicationPopupGUI(User user, Vector vector) {
        this.currentUser = user;
        this.medicationVector = vector;
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

    private String[] getMedicationChanges(TableModel tableModel) throws IOException {
        //Method to get data entered by user in new table, search for existing medication and replace it.
        String[] changedMedicationInfo = new String[6];
        int newValueIndex;

        for(newValueIndex = 0; newValueIndex < 6; newValueIndex++) {
            changedMedicationInfo[newValueIndex] = (String) newMedicationInformationModel.getValueAt(0, newValueIndex);
        }
        //System.out.println(Arrays.toString(changedMedicationInfo));

        saveMedicationChangesToFile(changedMedicationInfo);
        return changedMedicationInfo;
    }

    //Note: When changing the medication details, please supply the same name and only change other information.
    private void saveMedicationChangesToFile(String[] changedMedicationInfo) throws IOException {
        File inputMedicationFile = new File("src/main/resources/medications/" + currentUser.getName() + "_medications.csv");
        File outputMedicationFile = new File("src/main/resources/medications/temporaryfile.csv");

        BufferedReader medicationFileReader = new BufferedReader(new FileReader(inputMedicationFile));
        BufferedWriter medicationFileWriter = new BufferedWriter(new FileWriter(outputMedicationFile));

        String medicationToEdit = changedMedicationInfo[0];
        String currentReadLine;

        while((currentReadLine = medicationFileReader.readLine()) != null) {
            if(currentReadLine.contains(medicationToEdit)) continue;
            medicationFileWriter.write(currentReadLine + "\n");
        }
        medicationFileWriter.write(Arrays.toString(changedMedicationInfo).replaceAll("\\[|\\]", ""));

        medicationFileWriter.close();
        medicationFileReader.close();
        outputMedicationFile.renameTo(inputMedicationFile);
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

        JLabel currentMedicationHeading = new JLabel("Current Medication Details");
        currentMedicationHeading.setFont(applicationFont);
        currentMedicationHeading.setBounds(20, 50, 600, 50);
        editMedicationPanelContents.add(currentMedicationHeading);

        JLabel newMedicationHeading = new JLabel("New Medication Details");
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
                try {
                    getMedicationChanges(newMedicationInformationModel);

                } catch (FileNotFoundException MedicationFileNotFound) {
                    System.out.println("The medication file for this user could not be found or is not readable!");
                    throw new RuntimeException(MedicationFileNotFound);

                } catch (IOException FileCannotBeReadOrWrittenException) {
                    System.out.println("A problem occurred when reading or writing to the user medication file! Please check the permissions and try again.");
                    throw new RuntimeException(FileCannotBeReadOrWrittenException);
                }

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
        String[] currentMedHeader = {"Name", "Dosage", "Quantity", "Time", "Frequency", "Maximum"};
        Object[][] currentMedData = new Object[1][6];

        //Testing to see if Vector from other page is being shared across calls.
        //System.out.println("Row Selected: " + rowIndex);
        //System.out.println("Row Vector Contents: " + medicationVector);

        DefaultTableModel currentMedicationInformationModel = new DefaultTableModel(currentMedData, currentMedHeader);
        JTable currentMedicationInformation = new JTable(currentMedicationInformationModel);
        currentMedicationInformation.setBounds(20, 100, 500, 50);

        currentMedicationInformationModel.setValueAt(medicationVector.get(1), 0, 0);
        currentMedicationInformationModel.setValueAt(medicationVector.get(2), 0, 1);

        JScrollPane currentMedicationScrollPane = new JScrollPane(currentMedicationInformation);
        currentMedicationScrollPane.setBounds(20, 100, 500, 50);

        for(int vectorIndex = 0; vectorIndex < 6; vectorIndex++) {
            currentMedicationInformationModel.setValueAt(medicationVector.get(vectorIndex), 0, vectorIndex);
        }

        panel.add(currentMedicationScrollPane);
    }

    public void addNewDetailTable(JPanel panel) {
        String[] newMedHeader = {"New Name", "New Dosage", "New Quantity", "New Time", "New Frequency", "New Maximum"};
        Object[][] newMedData = new Object[1][6];

        newMedicationInformationModel = new DefaultTableModel(newMedData, newMedHeader);
        JTable newMedicationInformation = new JTable(newMedicationInformationModel);
        JScrollPane newMedicationScrollPane = new JScrollPane(newMedicationInformation);

        newMedicationInformation.setBounds(20, 100, 500, 50);
        newMedicationScrollPane.setBounds(20, 200, 500, 50);

        panel.add(newMedicationScrollPane);


    }
}
