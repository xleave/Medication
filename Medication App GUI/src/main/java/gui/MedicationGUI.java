package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

import services.User;
import services.MedicationManage;

public class MedicationGUI extends JPanel {

    private User currentUser;

    public JTable medicationTable = new JTable();
    public DefaultTableModel medicationTableModel = new DefaultTableModel();
    private JScrollPane tableScrollPane; // Add new member variables

    public Vector<String> rowValueVector = new Vector<>();

    public MedicationGUI(User user) {
        this.currentUser = user;
        initializeGUI();
    }

    private void initializeGUI() {
        Font applicationFont = loadFont();

        JPanel medicationPanel = new JPanel(new BorderLayout());
        medicationPanel.setPreferredSize(new Dimension(980, 600));

        JPanel medicationPanelContents = new JPanel();
        medicationPanelContents.setPreferredSize(new Dimension(980, 600));
        medicationPanelContents.setLayout(null);

        addSubmenuHeading(medicationPanelContents, applicationFont);
        addButtons(medicationPanelContents);
        addUserMedicationLabel(medicationPanelContents, applicationFont);

        if (currentUser.isAdmin()) {
            addAdminMedicationTable(medicationPanelContents);
        } else {
            addUserMedicationTable(medicationPanelContents);
        }

        medicationPanel.add(medicationPanelContents);
        add(medicationPanel);
    }

    private Font loadFont() {
        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/main/resources/fonts/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);
        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            throw new RuntimeException(e);
        }
        return applicationFont;
    }

    private void addSubmenuHeading(JPanel panel, Font font) {
        JLabel medicationSubmenuHeading = new JLabel("MEDICATIONS");
        medicationSubmenuHeading.setBounds(470, -20, 200, 50);
        medicationSubmenuHeading.setFont(font);
        panel.add(medicationSubmenuHeading);
    }

    private void addButtons(JPanel panel) {
        JButton addMedicationButton = new JButton("Add Medication");
        addMedicationButton.setBounds(20, 150, 200, 50);
        addMedicationButton.addActionListener(new AddMedicationActionListener());
        panel.add(addMedicationButton);

        JButton editMedicationButton = new JButton("Edit Medication");
        editMedicationButton.setBounds(20, 250, 200, 50);
        editMedicationButton.addActionListener(new EditMedicationActionListener());
        panel.add(editMedicationButton);

        JButton removeMedicationButton = new JButton("Remove Medication");
        removeMedicationButton.setBounds(20, 350, 200, 50);
        panel.add(removeMedicationButton);
    }

    private void addUserMedicationLabel(JPanel panel, Font font) {
        JLabel userMedicationLabel;
        if (currentUser.isAdmin()) {
            userMedicationLabel = new JLabel("All Users' Medications");
        } else {
            userMedicationLabel = new JLabel(currentUser.getName() + "'s Current Medications");
        }
        userMedicationLabel.setFont(font);
        userMedicationLabel.setBounds(500, 60, 300, 50);
        panel.add(userMedicationLabel);
    }

    private void addAdminMedicationTable(JPanel panel) {
        String[] medicationTableHeaders = {"User", "Name", "Dosage", "Quantity", "Time", "Take Daily", "Maximum Daily"};
        Object[][] medicationTableData = MedicationManage.getAdminMedicationData();
        addTableToPanel(panel, medicationTableData, medicationTableHeaders);
    }

    private void addUserMedicationTable(JPanel panel) {
        String[] medicationTableHeaders = {"Name", "Dosage", "Quantity", "Time", "Take Daily", "Maximum Daily"};
        Object[][] medicationTableData = MedicationManage.getUserMedicationData(currentUser);
        addTableToPanel(panel, medicationTableData, medicationTableHeaders);
    }

    private void addTableToPanel(JPanel panel, Object[][] tableData, String[] tableHeaders) {
        medicationTableModel = new DefaultTableModel(tableData, tableHeaders);
        medicationTable = new JTable(medicationTableModel);
        medicationTable.setBounds(400, 400, 600, 350);

        JScrollPane tableScrollPane = new JScrollPane(medicationTable);
        tableScrollPane.setBounds(300, 100, 600, 350);

        getSelectedRowFromTable();

        panel.add(tableScrollPane);
    }

    private void addEmptyTable(JPanel panel, String[] tableHeaders) {
        Object[][] medicationTableData = new Object[0][6];
        addTableToPanel(panel, medicationTableData, tableHeaders);
    }

    public Vector<String> getSelectedRowFromTable() {
        int rowIndex = medicationTable.getSelectedRow() + 1;
        medicationTableModel = (DefaultTableModel)medicationTable.getModel();
        //Now get value at each column of given row.
        rowValueVector = new Vector<>();
        rowValueVector.add((String) medicationTableModel.getValueAt(rowIndex, 0));
        rowValueVector.add((String) medicationTableModel.getValueAt(rowIndex, 1));
        rowValueVector.add((String) medicationTableModel.getValueAt(rowIndex, 2));
        rowValueVector.add((String) medicationTableModel.getValueAt(rowIndex, 3));
        rowValueVector.add((String) medicationTableModel.getValueAt(rowIndex, 4));
        rowValueVector.add((String) medicationTableModel.getValueAt(rowIndex, 5));

        //Testing to see if I can get the value of the index when clicked.
        System.out.println("Row clicked on " + rowIndex);


        return rowValueVector;
    }

    private class AddMedicationActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddMedicationPopupGUI addMedicationPopupGUI = new AddMedicationPopupGUI(currentUser);
        }

    }

private class EditMedicationActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent editButtonClicked) {
        EditMedicationPopupGUI editMedicationPopupGUI = new EditMedicationPopupGUI(currentUser, rowValueVector);
    }

}
}