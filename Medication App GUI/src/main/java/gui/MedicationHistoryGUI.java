package gui;
import services.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

import static java.awt.AWTEventMulticaster.add;
import static services.MedicationManage.loadFont;

public class MedicationHistoryGUI extends JPanel {

    private User currentUser;

    public JPanel medicationHistoryPanelContents = new JPanel();

    private JScrollPane medicationScrollPane;

    public MedicationHistoryGUI(User user) {
        this.currentUser = user;
        initializeGUI();
    }

    public void initializeGUI() {
        Font applicationFont = loadFont();

        JPanel medicationHistoryPanel = new JPanel();
        medicationHistoryPanel.setPreferredSize(new Dimension(980, 600));

        JPanel medicationHistoryPanelContents = new JPanel();
        medicationHistoryPanelContents.setPreferredSize(new Dimension(980, 600));

        JLabel medicationHistoryGUISubheading = new JLabel("MEDICATION HISTORY");
        medicationHistoryGUISubheading.setBounds(10, 300, 100,20);
        medicationHistoryPanel.add(medicationHistoryGUISubheading);

        createMedicationListTable(medicationHistoryPanelContents);

        drawGraph g = new drawGraph();
        medicationHistoryPanel.add(g,BorderLayout.CENTER);

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

    // private void createMedicationListTable(JPanel panel) {

    //     String[] medicationListHeaders = {"Medication", "Take"};
    //     Object[][] medicationListData = new Object[medicationListHeaders.length][2];

    //     DefaultTableModel medicationListModel = new DefaultTableModel(medicationListData, medicationListHeaders);
    //     JTable medicationListTable = new JTable(medicationListModel);
    //     medicationListTable.getColumnModel().getColumn(1).setCellRenderer(new MedicationHistoryGUI.ButtonRenderer());
    //     medicationListTable.getColumnModel().getColumn(1).setCellEditor(new MedicationHistoryGUI.ButtonEditor(new JTextField()));
    //     medicationListTable.setBounds(10, 100, 100, 400);

    //     JScrollPane medicationListScrollPane = new JScrollPane(medicationListTable);
    //     medicationListScrollPane.setBounds(10, 100, 100, 400);
    //     panel.add(medicationListScrollPane);

    //     String userMedicationFilePath = "src/main/resources/medications/" + currentUser.getName() + "_medications.csv";
    //     String temporaryLine = "";
    //     ArrayList<String> medicationNames = new ArrayList<String>();
    //     int medicationNameIndex = 0;
    //     try {
    //         BufferedReader medicationNameReader = new BufferedReader(new FileReader(userMedicationFilePath));
    //         while ((temporaryLine = medicationNameReader.readLine()) != null) {
    //             String[] tempArray =  temporaryLine.split(",");
    //             medicationNames.add(tempArray[0]);
    //             System.out.println(medicationNames);
    //             //Putting medicaiton names into table.
    //             medicationListModel.setValueAt(medicationNames.get(medicationNameIndex),medicationNameIndex, 0);

    //         }

    //     } catch (IOException medicationFileNotReadable) {
    //         System.out.println("The medication file for this user could not be found! Please try again later.");
    //         throw new RuntimeException(medicationFileNotReadable);
    //     }
    // }

    private void createMedicationListTable(JPanel panel) {

        String[] medicationListHeaders = { "Medication", "Take" };

        DefaultTableModel medicationListModel = new DefaultTableModel(medicationListHeaders, 0);
        JTable medicationListTable = new JTable(medicationListModel);
        medicationListTable.getColumnModel().getColumn(1).setCellRenderer(new MedicationHistoryGUI.ButtonRenderer());
        medicationListTable.getColumnModel().getColumn(1)
                .setCellEditor(new MedicationHistoryGUI.ButtonEditor(new JTextField()));
        medicationListTable.setBounds(10, 100, 100, 400);

        JScrollPane medicationListScrollPane = new JScrollPane(medicationListTable);
        medicationListScrollPane.setBounds(10, 100, 100, 400);
        panel.add(medicationListScrollPane);

        String userMedicationFilePath = "src/main/resources/medications/" + currentUser.getName() + "_medications.csv";
        String temporaryLine = "";
        ArrayList<String> medicationNames = new ArrayList<String>();

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
            System.out.println("The medication file for this user could not be found! Please try again later.");
            throw new RuntimeException(medicationFileNotReadable);
        }
    }

    //This class and methods are used to add a JButton into a JTable.
    public class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object obj, boolean selected, boolean focused, int row, int column) {
            setText((obj == null) ? "":obj.toString());
            return this;
        }
    }
    class ButtonEditor extends DefaultCellEditor {
        protected JButton takeMedicineButton;
        private String buttonLabel;
        private Boolean buttonHasBeenClicked;

        public ButtonEditor(JTextField textField) {
            super(textField);

            takeMedicineButton = new JButton();
            takeMedicineButton.setOpaque(true);

            //Action listener to see if user has clicked corresponding medicine take button.
            takeMedicineButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent takeMedicineButtonClicked) {
                    fireEditingStopped();
                }
            });
        }
        //Overriding method
        public Component getTableCellEditorComponent(JTable table, Object obj, boolean selected, int row, int column) {

            buttonLabel = (obj == null) ? "":obj.toString();
            takeMedicineButton.setText(buttonLabel);
            buttonHasBeenClicked = true;

            return takeMedicineButton;
        }

        @Override
        public Object getCellEditorValue() {
            if (buttonHasBeenClicked) {
                JOptionPane.showMessageDialog(takeMedicineButton, "Do you want to log medication as taken?");

            }
            buttonHasBeenClicked = false;
            return new String(buttonLabel);
        }

        public boolean stopCellEditing() {
            buttonHasBeenClicked=false;
            return super.stopCellEditing();
        }
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    private class drawGraph extends JPanel{
    int[] testdata ={5,10,15,20,30};
    String[] testname = {"Para","Ibu","Med3","Med4","Med5"};

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

                Graphics2D gtd = (Graphics2D) g;

                gtd.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                gtd.setColor(Color.blue);

                int xs = 50;
                int ys = 250;
                int width = 40;
                int space = 20;

                for(int i = 0; i<testdata.length;i++){
                    int barheight = testdata[i] * 5;
                    gtd.fillRect(xs + (i*(width+space)),ys-barheight,width,barheight);
                    gtd.setColor(Color.black);
                    gtd.drawString(testname[i],xs+(i*(width+space))+5,ys+15);
                    gtd.setColor(Color.blue);
                }
            }
            public Dimension getPreferredSize() {
            return new Dimension(500, 300);
        }
    }
}



