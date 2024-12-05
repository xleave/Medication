package services;

import java.io.*;
import java.util.ArrayList;

public class ChangePassword {

    private String userFilePath = "src/main/resources/users/users.csv";


    public boolean updatePassword(String username, String newPassword) {
        ArrayList<String> userRecords = new ArrayList<>();
        boolean isUpdated = false;

        try {
            File userFile = new File(userFilePath);
            BufferedReader reader = new BufferedReader(new FileReader(userFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length >= 2 && userDetails[0].equals(username)) {
                    userDetails[1] = newPassword;
                    isUpdated = true;
                }
                userRecords.add(String.join(",", userDetails));
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
            for (String record : userRecords) {
                writer.write(record);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return isUpdated;
    }
}