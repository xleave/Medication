package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import modules.User;

public class MedicationGUI extends JPanel {

    private User currentUser;
    private JScrollPane tableScrollPane; // Add new member variables

    public MedicationGUI(User user) {
        this.currentUser = user;

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
        medicationSubmenuHeading.setBounds(470, -20, 200, 50);
        medicationSubmenuHeading.setFont(applicationFont);
        medicationPanelContents.add(medicationSubmenuHeading);

        // 按钮设置
        JButton addMedicationButton = new JButton("Add Medication");
        addMedicationButton.setBounds(20, 150, 200, 50);
        medicationPanelContents.add(addMedicationButton);

        JButton editMedicationButton = new JButton("Edit Medication");
        editMedicationButton.setBounds(20, 250, 200, 50);
        medicationPanelContents.add(editMedicationButton);

        JButton removeMedicationButton = new JButton("Remove Medication");
        removeMedicationButton.setBounds(20, 350, 200, 50);
        medicationPanelContents.add(removeMedicationButton);

        JLabel userMedicationLabel;
        if (currentUser.isAdmin()) {
            userMedicationLabel = new JLabel("All Users' Medications");
        } else {
            userMedicationLabel = new JLabel(currentUser.getName() + "'s Current Medications");
        }
        userMedicationLabel.setFont(applicationFont);
        userMedicationLabel.setBounds(500, 60, 300, 50);
        medicationPanelContents.add(userMedicationLabel);

        addMedicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMedicationPopupGUI addMedicationPopupGUI = new AddMedicationPopupGUI(currentUser);
            }
        });

        // 判断当前用户是否是管理员
        if (currentUser.isAdmin()) {
            // 显示所有用户的用药信息
            String[] medicationTableHeaders = { "User", "Name", "Dosage", "Quantity", "Time", "Take Daily",
                    "Maximum Daily" };
            ArrayList<Object[]> medicationDataList = new ArrayList<>();

            // 遍历所有用户
            File medicationDir = new File("src/resources/medications");
            File[] medicationFiles = medicationDir.listFiles((dir, name) -> name.endsWith("_medications.csv"));

            if (medicationFiles != null) {
                for (File medFile : medicationFiles) {
                    String fileName = medFile.getName();
                    String userName = fileName.substring(0, fileName.indexOf("_medications.csv"));
                    try {
                        BufferedReader medicationFileReader = new BufferedReader(new FileReader(medFile));
                        String line;
                        while ((line = medicationFileReader.readLine()) != null) {
                            String[] medData = line.split(",");
                            // 在数据前面加上用户名
                            Object[] rowData = new Object[7];
                            rowData[0] = userName;

                            // 确保不会超出 rowData 的长度
                            int copyLength = Math.min(medData.length, 6);
                            System.arraycopy(medData, 0, rowData, 1, copyLength);

                            // 如果 medData.length < 6，填充剩余的位置为空字符串
                            for (int i = 1 + copyLength; i < 7; i++) {
                                rowData[i] = "";
                            }

                            medicationDataList.add(rowData);
                        }
                        medicationFileReader.close();
                    } catch (IOException e) {
                        System.out.println("读取用户 " + userName + " 的用药文件时出错。");
                    }
                }
            }

            Object[][] medicationTableData = medicationDataList.toArray(new Object[0][]);

            JTable medicationTable = new JTable(medicationTableData, medicationTableHeaders);
            medicationTable.setBounds(400, 400, 600, 350);

            JScrollPane tableScrollPane = new JScrollPane(medicationTable);
            tableScrollPane.setBounds(300, 100, 600, 350);
            medicationPanelContents.add(tableScrollPane);

        } else {
            // 原有逻辑，显示当前用户的用药信息
            String medicationFile = "src/resources/medications/" + currentUser.getName() + "_medications.csv";
            String[] medicationTableHeaders = { "Name", "Dosage", "Quantity", "Time", "Take Daily",
                    "Maximum Daily" };

            try {
                BufferedReader medicationFileReader = new BufferedReader(new FileReader(medicationFile));
                ArrayList<String> medicationList = new ArrayList<>();
                String temporaryString;
                while ((temporaryString = medicationFileReader.readLine()) != null) {
                    medicationList.add(temporaryString);
                }

                Object[][] medicationTableData = new Object[medicationList.size()][6];
                for (int medicationListIndex = 0; medicationListIndex < medicationList.size(); medicationListIndex++) {
                    String[] medData = medicationList.get(medicationListIndex).split(",");
                    // 确保不会超出 medicationTableData 的长度
                    if (medData.length > 6) {
                        System.arraycopy(medData, 0, medicationTableData[medicationListIndex], 0, 6);
                    } else {
                        System.arraycopy(medData, 0, medicationTableData[medicationListIndex], 0, medData.length);
                        // 填充剩余的位置为空字符串
                        for (int i = medData.length; i < 6; i++) {
                            medicationTableData[medicationListIndex][i] = "";
                        }
                    }
                }

                JTable medicationTable = new JTable(medicationTableData, medicationTableHeaders);
                medicationTable.setBounds(400, 400, 600, 350);

                JScrollPane tableScrollPane = new JScrollPane(medicationTable);
                tableScrollPane.setBounds(300, 100, 600, 350);
                medicationPanelContents.add(tableScrollPane);

            } catch (FileNotFoundException medicationFileNotFound) {
                System.out.println(
                        "The medication file for this user could not be found! Please check the path and try again.");
                // 创建一个空的表格，避免程序崩溃
                Object[][] medicationTableData = new Object[0][6];
                JTable medicationTable = new JTable(medicationTableData, medicationTableHeaders);
                JScrollPane tableScrollPane = new JScrollPane(medicationTable);
                tableScrollPane.setBounds(300, 100, 600, 350);
                medicationPanelContents.add(tableScrollPane);
            } catch (IOException medicationFileCannotBeRead) {
                System.out.println("读取用药文件时出错。");
            }
        }

        medicationPanel.add(medicationPanelContents);
        add(medicationPanel);

        // 测试用户功能
        System.out.println(currentUser.getName());
    }
}