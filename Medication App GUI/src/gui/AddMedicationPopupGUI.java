package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import modules.User;

public class AddMedicationPopupGUI extends JPanel {

    private User currentUser;
    private JTextField userNameTextBox; // 将 userNameTextBox 声明为成员变量
    public AddMedicationPopupGUI(User user) {

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

        JFrame addMedicationFrame = new JFrame("Add Medication");
        addMedicationFrame.setSize(400, 650);

        JPanel addMedicationPanelContents = new JPanel();
        addMedicationPanelContents.setLayout(null);

        JLabel addMedicationTitle = new JLabel("Add New Medication");
        addMedicationTitle.setFont(applicationFont);
        addMedicationTitle.setBounds(125, 10, 200, 30);
        addMedicationPanelContents.add(addMedicationTitle);

        int yPosition = 60; // 初始 y 坐标

        if (currentUser.isAdmin()) {
            JLabel userNameLabel = new JLabel("Username");
            userNameLabel.setBounds(20, yPosition, 250, 20);
            addMedicationPanelContents.add(userNameLabel);

            yPosition += 25;

            userNameTextBox = new JTextField(); // 初始化成员变量
            userNameTextBox.setBounds(20, yPosition, 250, 20);
            addMedicationPanelContents.add(userNameTextBox);

            yPosition += 35;
        }

        // 输入字段标签和文本框
        String[] inputFieldHeaders = { "Medication Name", "Dosage", "Quantity", "Time to be taken at", "How often?",
                "Maximum daily" };
        JTextField[] textFields = new JTextField[inputFieldHeaders.length];

        for (int i = 0; i < inputFieldHeaders.length; i++) {
            JLabel inputFieldHeader = new JLabel(inputFieldHeaders[i]);
            inputFieldHeader.setBounds(20, yPosition, 250, 20);
            addMedicationPanelContents.add(inputFieldHeader);

            yPosition += 25;

            JTextField textField = new JTextField();
            textField.setBounds(20, yPosition, 250, 20);
            addMedicationPanelContents.add(textField);

            textFields[i] = textField;

            yPosition += 35;
        }

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(applicationFont);
        cancelButton.setBounds(80, yPosition, 100, 30);
        addMedicationPanelContents.add(cancelButton);

        JButton acceptButton = new JButton("Add Medicine");
        acceptButton.setFont(applicationFont);
        acceptButton.setBounds(190, yPosition, 150, 30);
        addMedicationPanelContents.add(acceptButton);

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String targetUserName = currentUser.getName();

                if (currentUser.isAdmin()) {
                    // 获取管理员输入的用户名
                    if (userNameTextBox != null) {
                        targetUserName = userNameTextBox.getText().trim();
                        if (targetUserName.isEmpty()) {
                            JOptionPane.showMessageDialog(acceptButton, "请输入用户名。", "错误",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(acceptButton, "用户名字段未初始化。", "错误",
                        JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                // 获取药品信息
                String medicationName = textFields[0].getText().trim();
                String dosage = textFields[1].getText().trim();
                String quantity = textFields[2].getText().trim();
                String timeToTake = textFields[3].getText().trim();
                String howOften = textFields[4].getText().trim();
                String maximumDaily = textFields[5].getText().trim();

                // 验证输入
                if (medicationName.isEmpty() || dosage.isEmpty() || quantity.isEmpty() || timeToTake.isEmpty()
                        || howOften.isEmpty() || maximumDaily.isEmpty()) {
                    JOptionPane.showMessageDialog(acceptButton, "Please fill in all fields.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 将药品信息保存到对应用户的药品文件中
                String medicationFilePath = "src/resources/medications/" + targetUserName + "_medications.csv";
                File medicationFile = new File(medicationFilePath);
                try {
                    // 检查用户的药品文件是否存在
                    if (!medicationFile.exists()) {
                        medicationFile.createNewFile();
                    }

                    FileWriter fw = new FileWriter(medicationFile, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw);

                    // 写入药品信息
                    out.println(medicationName + "," + dosage + "," + quantity + "," + timeToTake + "," + howOften + ","
                            + maximumDaily);
                    out.close();

                    JOptionPane.showMessageDialog(acceptButton, "Medication has been added.", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    addMedicationFrame.dispose();

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(acceptButton, "An error occurred while saving medication.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
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