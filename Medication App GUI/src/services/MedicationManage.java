package services;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MedicationManage {

    public static Font loadFont(String fontPath) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException("Font could not be loaded: " + fontPath, e);
        }
    }

    public static void saveMedicationInfo(String targetUserName, JTextField[] textFields, JButton acceptButton, JFrame frame) {
        String medicationFilePath = "src/resources/medications/" + targetUserName + "_medications.csv";
        File medicationFile = new File(medicationFilePath);
        try {
            if (!medicationFile.exists()) {
                medicationFile.createNewFile();
            }

            try (FileWriter fw = new FileWriter(medicationFile, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(getMedicationInfo(textFields));
            }

            JOptionPane.showMessageDialog(acceptButton, "Medication has been added.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(acceptButton, "An error occurred while saving medication.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private static String getMedicationInfo(JTextField[] textFields) {
        StringBuilder medicationInfo = new StringBuilder();
        for (JTextField textField : textFields) {
            medicationInfo.append(textField.getText().trim()).append(",");
        }
        // Remove trailing comma
        medicationInfo.setLength(medicationInfo.length() - 1);
        return medicationInfo.toString();
    }
}