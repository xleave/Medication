import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class MedicationGUI extends JPanel {

    JScrollPane tableScrollPane;
    JTable medicationsTable;
    String[] tableColumns;
    Object[][] tableData;

    public Object[][] loadMedicationData() {
        try {
            BufferedReader medicationDataBufferedReader = new BufferedReader(new FileReader("/Users/marley/Documents/fuckmenu/src/pizzacunt"));
            ArrayList<String> medicationList = new ArrayList<>();
            String tempString = "";
            while ((tempString = medicationDataBufferedReader.readLine()) != null) {
                medicationList.add(tempString);
                System.out.println(tempString);
            }

            int dataCounter = medicationList.get(0).split(",").length;
            Object[][] medicationDataForTable = new Object[medicationList.size()][dataCounter];
            for (int dataCounter2 = 0; dataCounter2 < medicationList.size(); dataCounter2++) {
                medicationDataForTable[dataCounter2] = medicationList.get(dataCounter2).split(",");
            }
            medicationDataBufferedReader.close();
            return tableData;


        } catch (FileNotFoundException medicationFileNotFound) {
            System.out.println("The Medication Data File Couldn't be found!");
            throw new RuntimeException(medicationFileNotFound);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public MedicationGUI() {

        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/marley/Library/Mobile Documents/com~apple~CloudDocs/Documents/University Work - NAS/Year 3/CE320 Large Scale Software Systems/Group_Project/Medication App/src/EB_Garamond,Roboto_Condensed/Roboto_Condensed/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
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
        addMedicationButton.setBounds(20, 200, 200, 50);
        medicationPanelContents.add(addMedicationButton);

        JButton editMedicationButton = new JButton("Edit Medication");
        editMedicationButton.setBounds(20, 300, 200, 50);
        medicationPanelContents.add(editMedicationButton);

        JButton removeMedicationButton = new JButton("Remove Medication");
        removeMedicationButton.setBounds(20, 400, 200, 50);
        medicationPanelContents.add(removeMedicationButton);

        //Medications Table
        tableColumns = new String[] {"Name", "Dosage", "Quantity", "Take at:", "Daily amount:"};
        medicationsTable = new JTable(tableData, tableColumns);
        tableScrollPane = new JScrollPane();
        tableScrollPane.add(medicationsTable);


        medicationPanelContents.add(tableScrollPane);
        medicationPanel.add(medicationPanelContents);
        add(medicationPanel);

    }
}