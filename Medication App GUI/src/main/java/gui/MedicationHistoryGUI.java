package gui;

import services.HistoryTracker;
import services.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MedicationHistoryGUI extends JPanel {

    private User currentUser;
    private HistoryTracker historyTracker;

    public JPanel medicationHistoryPanelContents = new JPanel();
    private JScrollPane medicationScrollPane;

    public MedicationHistoryGUI(User user) {
        this.currentUser = user;
        this.historyTracker = new HistoryTracker(user);
        initializeGUI();
    }

    public void initializeGUI() {
        Font applicationFont = historyTracker.loadFont();

        JPanel medicationHistoryPanel = new JPanel();
        medicationHistoryPanel.setPreferredSize(new Dimension(980, 600));
        medicationHistoryPanel.setLayout(new BorderLayout());

        JLabel medicationHistoryGUISubheading = new JLabel("MEDICATION HISTORY", SwingConstants.CENTER);
        medicationHistoryGUISubheading.setFont(applicationFont.deriveFont(Font.BOLD, 20f));
        medicationHistoryPanel.add(medicationHistoryGUISubheading, BorderLayout.NORTH);

        // Create a panel for the table
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());

        // Create a refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(new Dimension(100, 40));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadMedicationList();
            }
        });

        // Place the refresh button on the left side of the form
        tablePanel.add(refreshButton, BorderLayout.WEST);

        JScrollPane medicationListScrollPane = createMedicationListTable();
        tablePanel.add(medicationListScrollPane, BorderLayout.CENTER);

        medicationHistoryPanel.add(tablePanel, BorderLayout.CENTER);

        drawGraph g = new drawGraph();
        medicationHistoryPanel.add(g, BorderLayout.SOUTH);

        add(medicationHistoryPanel);
    }

    private JScrollPane createMedicationListTable() {
        String[] medicationListHeaders = { "Medication", "Take" };

        DefaultTableModel medicationListModel = new DefaultTableModel(medicationListHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Only the "Take" button is editable
            }
        };

        JTable medicationListTable = new JTable(medicationListModel);
        medicationListTable.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
        medicationListTable.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor());
        medicationListTable.setRowHeight(40);

        // Load drug information into the form
        loadMedicationsIntoTable(medicationListModel);

        medicationScrollPane = new JScrollPane(medicationListTable);
        medicationScrollPane.setPreferredSize(new Dimension(860, 400));
        medicationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        medicationScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return medicationScrollPane;
    }

    private void loadMedicationsIntoTable(DefaultTableModel model) {
        // Empty existing data
        model.setRowCount(0);

        // Load drug information and add to form
        for (Map.Entry<String, Integer> entry : historyTracker.getMedicationDailyLimits().entrySet()) {
            String medicationName = entry.getKey();
            model.addRow(new Object[] { medicationName, "Take" });
        }
    }

    private void reloadMedicationList() {
        // Reload medication data
        historyTracker.reloadMedicationData();

        // Update the table model
        DefaultTableModel model = (DefaultTableModel) ((JTable) medicationScrollPane.getViewport().getView()).getModel();
        loadMedicationsIntoTable(model);

        // Refresh the table view
        ((JTable) medicationScrollPane.getViewport().getView()).revalidate();
        ((JTable) medicationScrollPane.getViewport().getView()).repaint();

        JOptionPane.showMessageDialog(this, "Drug information has been refreshed.");
    }

    public class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
            setMargin(new Insets(5, 15, 5, 15));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object obj, boolean selected,
                                                       boolean focused, int row, int column) {
            setText((obj == null) ? "" : obj.toString());
            return this;
        }
    }

    public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

        private JButton takeMedicineButton = new JButton();
        private String currentMedication;

        public ButtonEditor() {
            takeMedicineButton.setOpaque(true);
            takeMedicineButton.setMargin(new Insets(5, 15, 5, 15));

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

            //check if daily limit exceeded
            if (historyTracker.isExceeded(currentMedication)) {
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
            if (historyTracker.isExceeded(currentMedication)) {
                JOptionPane.showMessageDialog(
                        takeMedicineButton,
                        "You cannot take more of this medication as you have already reached the maximum daily dosage for " + currentMedication + ".",
                        "Dosage Limit Reached",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int response = JOptionPane.showConfirmDialog(takeMedicineButton,
                    "Do you want to log medication as taken?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                historyTracker.saveTakenRecord(currentMedication);
                JOptionPane.showMessageDialog(
                        takeMedicineButton,
                        "Medication " + currentMedication + " has been logged as taken.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // check if daily limit exceeded after taking medication
                if (historyTracker.isExceeded(currentMedication)) {
                    JOptionPane.showMessageDialog(
                            takeMedicineButton,
                            "Overdose warning: You have exceeded the daily limit for " + currentMedication + ".",
                            "Overdose Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
            fireEditingStopped();
        }
    }

    // Internal class for charting
    private class drawGraph extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D gtd = (Graphics2D) g;
            gtd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Map<String, Integer> medicationLimits = historyTracker.getMedicationDailyLimits();
            Map<String, Integer> medicationTakenCounts = new HashMap<>();

            for (String medication : medicationLimits.keySet()) {
                medicationTakenCounts.put(medication, historyTracker.getTakenCount(medication));
            }


            int xs = 50;
            int ys = 250;
            int width = 40;
            int space = 5;
            int index = 0;

            for (Map.Entry<String, Integer> entry : medicationLimits.entrySet()) {
                String medication = entry.getKey();
                int dailyLimit = entry.getValue();
                int dosesTaken = medicationTakenCounts.getOrDefault(medication, 0);

                gtd.setColor(dosesTaken > dailyLimit ? Color.RED : Color.BLUE);

                int takenBarHeight = dosesTaken * 10;
                int limitBarHeight = dailyLimit * 10;

                gtd.fillRect(xs + (index * (width + space)), ys - takenBarHeight, width, takenBarHeight);
                gtd.setColor(Color.GRAY);
                gtd.drawRect(xs + (index * (width + space)), ys - limitBarHeight, width, limitBarHeight);

                gtd.setColor(Color.BLACK);
                gtd.drawString(medication, xs + (index * (width + space)) + 5, ys + 15);

                gtd.drawString(String.valueOf(dosesTaken), xs + (index * (width + space)) + 10, ys - takenBarHeight - 5);
                index++;
            }
        }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(500, 300);
        }
    }
}