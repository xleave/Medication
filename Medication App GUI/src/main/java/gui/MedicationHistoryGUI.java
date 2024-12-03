package gui;
import services.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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

        //createMedicationListTable(medicationHistoryPanelContents);

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

    private void createMedicationListTable(JPanel panel) {

        String[] medicationListHeaders = {"Medication", "Take"};
        Object[][] medicationListData = {
                {"Paracetomol", "Button ?"}
        };

        JTable medicationListTable = new JTable(medicationListData, medicationListHeaders);
        medicationListTable.setBounds(10, 100, 100, 400);

        JScrollPane medicationListScrollPane = new JScrollPane(medicationListTable);
        medicationListScrollPane.setBounds(10, 100, 100, 400);
        panel.add(medicationListScrollPane);
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



