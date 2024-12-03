package services;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

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
        String medicationFilePath = "src/main/resources/medications/" + targetUserName + "_medications.csv";
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

    public static Object[][] getAdminMedicationData() {
        ArrayList<Object[]> medicationDataList = new ArrayList<>();

        File medicationDir = new File("src/main/resources/medications");
        File[] medicationFiles = medicationDir.listFiles((dir, name) -> name.endsWith("_medications.csv"));

        if (medicationFiles != null) {
            for (File medFile : medicationFiles) {
                String fileName = medFile.getName();
                String userName = fileName.substring(0, fileName.indexOf("_medications.csv"));
                readMedicationFile(medFile, medicationDataList, userName);
            }
        }

        return medicationDataList.toArray(new Object[0][]);
    }

    public static Object[][] getUserMedicationData(User currentUser) {
        String medicationFile = "src/main/resources/medications/" + currentUser.getName() + "_medications.csv";
        ArrayList<String> medicationList = new ArrayList<>();

        try {
            BufferedReader medicationFileReader = new BufferedReader(new FileReader(medicationFile));
            String temporaryString;
            while ((temporaryString = medicationFileReader.readLine()) != null) {
                medicationList.add(temporaryString);
            }
        } catch (FileNotFoundException e) {
            System.out.println("The medication file for this user could not be found! Please check the path and try again.");
        } catch (IOException e) {
            System.out.println("Error reading medication file.");
        }

        Object[][] medicationTableData = new Object[medicationList.size()][6];
        for (int i = 0; i < medicationList.size(); i++) {
            String[] medData = medicationList.get(i).split(",");
            if (medData.length > 6) {
                System.arraycopy(medData, 0, medicationTableData[i], 0, 6);
            } else {
                System.arraycopy(medData, 0, medicationTableData[i], 0, medData.length);
                for (int j = medData.length; j < 6; j++) {
                    medicationTableData[i][j] = "";
                }
            }
        }
        return medicationTableData;
    }

    private static void readMedicationFile(File medFile, ArrayList<Object[]> medicationDataList, String userName) {
        try {
            BufferedReader medicationFileReader = new BufferedReader(new FileReader(medFile));
            String line;
            while ((line = medicationFileReader.readLine()) != null) {
                String[] medData = line.split(",");
                Object[] rowData = new Object[7];
                rowData[0] = userName;
                int copyLength = Math.min(medData.length, 6);
                System.arraycopy(medData, 0, rowData, 1, copyLength);
                for (int i = 1 + copyLength; i < 7; i++) {
                    rowData[i] = "";
                }
                medicationDataList.add(rowData);
            }
            medicationFileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading user " + userName + " 's medication file.");
        }
    }
}