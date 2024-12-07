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

        createMedicationListTable(medicationHistoryPanelContents);
        medicationHistoryPanel.add(medicationHistoryPanelContents, BorderLayout.CENTER);

        drawGraph g = new drawGraph();
        medicationHistoryPanel.add(g, BorderLayout.SOUTH);

        add(medicationHistoryPanel);
    }

    private void createMedicationListTable(JPanel panel) {

        String[] medicationListHeaders = { "Medication", "Take" };

        DefaultTableModel medicationListModel = new DefaultTableModel(medicationListHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // 只允许"Take"列可编辑
            }
        };
        JTable medicationListTable = new JTable(medicationListModel);
        medicationListTable.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
        medicationListTable.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor());
        medicationListTable.setRowHeight(40);

        JScrollPane medicationListScrollPane = new JScrollPane(medicationListTable);
        medicationListScrollPane.setPreferredSize(new Dimension(960, 400));
        panel.add(medicationListScrollPane);

        // Load drug information and add to form
        for (Map.Entry<String, Integer> entry : historyTracker.getMedicationDailyLimits().entrySet()) {
            String medicationName = entry.getKey();
            medicationListModel.addRow(new Object[] { medicationName, "Take" });
        }
    }

    // The ButtonRenderer class is used to render buttons.
    public class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
            setMargin(new Insets(5, 15, 5, 15)); // 增加按钮内边距
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object obj, boolean selected,
                boolean focused, int row, int column) {
            setText((obj == null) ? "" : obj.toString());
            return this;
        }
    }

    // The ButtonEditor class is used to handle button click events.
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

            // Check if the daily limit is exceeded
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
                historyTracker.saveTakenRecord(currentMedication);
                JOptionPane.showMessageDialog(takeMedicineButton,
                        "Medication " + currentMedication + " has been logged as taken.");

                // Update the table to reflect the new count
                if (historyTracker.isExceeded(currentMedication)) {
                    takeMedicineButton.setEnabled(false);
                    System.out.println(
                            "Overdose warning: You have exceeded the daily limit for " + currentMedication + ".");
                }
            }
            fireEditingStopped();
        }
    }

    // The drawGraph class is used to draw a bar graph.
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

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(500, 300);
        }
    }
}