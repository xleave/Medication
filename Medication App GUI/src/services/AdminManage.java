package services;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class AdminManage {

    private User currentUser;

    public AdminManage(User currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<String[]> getAllUsers() {
        ArrayList<String[]> userList = new ArrayList<>();
        try {
            File userFile = new File("src/resources/users/users.csv");
            BufferedReader br = new BufferedReader(new FileReader(userFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length >= 3) {
                    // Do not show the current administrator himself
                    if (!userDetails[0].equals(currentUser.getName())) {
                        userList.add(userDetails);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void deleteUser(String userName) {
        try {
            // Remove users from users.csv
            File userFile = new File("src/resources/users/users.csv");
            File tempFile = new File("src/resources/users/users_temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(userFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (trimmedLine.startsWith(userName + ",")) {
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();

            // Delete the original file and rename the temporary file
            if (!userFile.delete()) {
                System.out.println("Failed to delete the original user file.");
            }
            if (!tempFile.renameTo(userFile)) {
                System.out.println("Failed to rename the temp user file.");
            }

            // Delete the user's medication file
            File medicationFile = new File("src/resources/medications/" + userName + "_medications.csv");
            if (medicationFile.exists()) {
                medicationFile.delete();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
