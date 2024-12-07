package gui;

import services.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.DefaultCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MedicationHistoryGUI extends JPanel {

    private User currentUser;

    public JPanel medicationHistoryPanelContents = new JPanel();

    private JScrollPane medicationScrollPane;

    // Map to store daily dosage limits for each medication
    private Map<String, Integer> medicationDailyLimits = new HashMap<>();

    // Map to store taken counts for each medication today
    private Map<String, Integer> medicationTakenCounts = new HashMap<>();

    public MedicationHistoryGUI(User user) {
        this.currentUser = user;
        loadMedicationLimits();
        initializeGUI();
        loadTakenRecords();
    }

    public void initializeGUI() {
        Font applicationFont = loadFont();

        JPanel medicationHistoryPanel = new JPanel();
        medicationHistoryPanel.setPreferredSize(new Dimension(980, 600));

        JPanel medicationHistoryPanelContents = new JPanel();
        medicationHistoryPanelContents.setPreferredSize(new Dimension(980, 600));

        JLabel medicationHistoryGUISubheading = new JLabel("MEDICATION HISTORY");
        medicationHistoryGUISubheading.setBounds(10, 300, 200, 20);
        medicationHistoryPanel.add(medicationHistoryGUISubheading);

        createMedicationListTable(medicationHistoryPanelContents);

        drawGraph g = new drawGraph();
        medicationHistoryPanel.add(g, BorderLayout.CENTER);

        medicationHistoryPanel.add(medicationHistoryPanelContents);
        add(medicationHistoryPanel);
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

    private void loadMedicationLimits() {
        // Read medication limits from user's medication file
        String userMedicationFilePath = "src/main/resources/medications/" + currentUser.getName() + "_medications.csv";
        File medicationFile = new File(userMedicationFilePath);
        if (!medicationFile.exists()) {
            System.out.println("Medication file not found for user: " + currentUser.getName());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(medicationFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] medInfo = line.split(",");
                if (medInfo.length >= 2) {
                    String medName = medInfo[0];
                    int dailyLimit = Integer.parseInt(medInfo[1]); // Assuming the daily limit is the second field
                    medicationDailyLimits.put(medName, dailyLimit);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read medication file!");
            throw new RuntimeException(e);
        }
    }

    private void loadTakenRecords() {
        String takenRecordsFilePath = "src/main/resources/taken_records/" + currentUser.getName()
                + "_taken_records.csv";
        File file = new File(takenRecordsFilePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create taken records file!");
                throw new RuntimeException(e);
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(takenRecordsFilePath))) {
            String line;
            LocalDate today = LocalDate.now();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String medName = parts[0];
                    String date = parts[1];
                    int count = Integer.parseInt(parts[2]);
                    if (date.equals(today.toString())) {
                        medicationTakenCounts.put(medName, count);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read taken records file!");
            throw new RuntimeException(e);
        }
    }

    private void saveTakenRecord(String medicationName) {
        String takenRecordsFilePath = "src/main/resources/taken_records/" + currentUser.getName()
                + "_taken_records.csv";
        LocalDate today = LocalDate.now();
        int currentCount = medicationTakenCounts.getOrDefault(medicationName, 0) + 1;
        medicationTakenCounts.put(medicationName, currentCount);

        // Rewrite the entire file with updated counts
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(takenRecordsFilePath))) {
            for (Map.Entry<String, Integer> entry : medicationTakenCounts.entrySet()) {
                writer.write(entry.getKey() + "," + today.toString() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Could not write to taken records file!");
            throw new RuntimeException(e);
        }
    }

    private void createMedicationListTable(JPanel panel) {

        String[] medicationListHeaders = { "Medication", "Take" };

        DefaultTableModel medicationListModel = new DefaultTableModel(medicationListHeaders, 0);
        JTable medicationListTable = new JTable(medicationListModel);
        medicationListTable.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
        medicationListTable.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor());
        medicationListTable.setRowHeight(30); // Set row height to make button easier to click

        JScrollPane medicationListScrollPane = new JScrollPane(medicationListTable);
        medicationListScrollPane.setPreferredSize(new Dimension(960, 400));
        panel.add(medicationListScrollPane);

        String userMedicationFilePath = "src/main/resources/medications/" + currentUser.getName()
                + "_medications.csv";
        String temporaryLine = "";

        try {
            BufferedReader medicationNameReader = new BufferedReader(new FileReader(userMedicationFilePath));
            while ((temporaryLine = medicationNameReader.readLine()) != null) {
                String[] tempArray = temporaryLine.split(",");
                if (tempArray.length > 0) {
                    String medicationName = tempArray[0];
                    medicationListModel.addRow(new Object[] { medicationName, "Take" });
                }
            }
            medicationNameReader.close();
        } catch (IOException medicationFileNotReadable) {
            System.out.println(
                    "The medication file for this user could not be found! Please try again later.");
            throw new RuntimeException(medicationFileNotReadable);
        }
    }

    // ButtonRenderer class
    public class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object obj, boolean selected,
                boolean focused, int row, int column) {
            setText((obj == null) ? "" : obj.toString());
            return this;
        }
    }

    // ButtonEditor class
    public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

        private JButton takeMedicineButton = new JButton();
        private String currentMedication;

        public ButtonEditor() {
            takeMedicineButton.setOpaque(true);

            takeMedicineButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleTakeMedicine();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentMedication = table.getValueAt(row, 0).toString();
            takeMedicineButton.setText((value == null) ? "" : value.toString());

            // Check if daily limit is reached
            int takenCount = medicationTakenCounts.getOrDefault(currentMedication, 0);
            int dailyLimit = medicationDailyLimits.getOrDefault(currentMedication, Integer.MAX_VALUE);
            if (takenCount >= dailyLimit) {
                takeMedicineButton.setEnabled(false);
            } else {
                takeMedicineButton.setEnabled(true);
            }

            return takeMedicineButton;
        }

        @Override
        public Object getCellEditorValue() {
            return "Take";
        }

        private void handleTakeMedicine() {
            int takenCount = medicationTakenCounts.getOrDefault(currentMedication, 0);
            int dailyLimit = medicationDailyLimits.getOrDefault(currentMedication, Integer.MAX_VALUE);
            if (takenCount >= dailyLimit) {
                System.out
                        .println("Overdose warning: You have exceeded the daily limit for " + currentMedication + ".");
                JOptionPane.showMessageDialog(takeMedicineButton,
                        "You have reached the daily limit for " + currentMedication + ".");
                return;
            }

            int response = JOptionPane.showConfirmDialog(takeMedicineButton,
                    "Do you want to log medication as taken?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                saveTakenRecord(currentMedication);
                JOptionPane.showMessageDialog(takeMedicineButton,
                        "Medication " + currentMedication + " has been logged as taken.");

                // Disable the button if limit is reached
                takenCount = medicationTakenCounts.getOrDefault(currentMedication, 0);
                if (takenCount >= dailyLimit) {
                    takeMedicineButton.setEnabled(false);
                    // Output overdose warning to console
                    if (takenCount > dailyLimit) {
                        System.out.println(
                                "Overdose warning: You have exceeded the daily limit for " + currentMedication + ".");
                    }
                }
            }
            fireEditingStopped();
        }
    }

    private class drawGraph extends JPanel {
        int[] testdata = { 5, 10, 15, 20, 30 };
        String[] testname = { "Para", "Ibu", "Med3", "Med4", "Med5" };

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D gtd = (Graphics2D) g;

            gtd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gtd.setColor(Color.blue);

            int xs = 50;
            int ys = 250;
            int width = 40;
            int space = 20;

            for (int i = 0; i < testdata.length; i++) {
                int barheight = testdata[i] * 5;
                gtd.fillRect(xs + (i * (width + space)), ys - barheight, width, barheight);
                gtd.setColor(Color.black);
                gtd.drawString(testname[i], xs + (i * (width + space)) + 5, ys + 15);
                gtd.setColor(Color.blue);
            }
        }

        public Dimension getPreferredSize() {
            return new Dimension(500, 300);
        }
    }
}