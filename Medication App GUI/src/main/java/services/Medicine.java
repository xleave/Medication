package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Medicine {
    private String name;
    private String dosage;
    private int quantity;
    private String timeToTake;
    private String userName;
    private int dailyFrequency;
    private int maxDailyDose;
    private List<LocalDateTime> takenTimes;

    public Medicine(String name, String dosage, int quantity, String timeToTake, String userName, int dailyFrequency,
            int maxDailyDose) {
        this.name = name;
        this.dosage = dosage;
        this.quantity = quantity;
        this.timeToTake = timeToTake;
        this.userName = userName;
        this.dailyFrequency = dailyFrequency;
        this.maxDailyDose = maxDailyDose;
        this.takenTimes = new ArrayList<>();
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Dosage getter and setter
    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    // Quantity getter and setter
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Existing getters and setters...

    public String getTimeToTake() {
        return timeToTake;
    }

    public void setTimeToTake(String timeToTake) {
        this.timeToTake = timeToTake;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getDailyFrequency() {
        return dailyFrequency;
    }

    public void setDailyFrequency(int dailyFrequency) {
        this.dailyFrequency = dailyFrequency;
    }

    public int getMaxDailyDose() {
        return maxDailyDose;
    }

    public void setMaxDailyDose(int maxDailyDose) {
        this.maxDailyDose = maxDailyDose;
    }

    public List<LocalDateTime> getTakenTimes() {
        return takenTimes;
    }

    public void addTakenTime(LocalDateTime time) {
        this.takenTimes.add(time);
    }

    // Method to check if the user is overdosing
    public boolean isOverDose() {
        // Implement logic to check if doses taken exceed maxDailyDose
        int dosesToday = 0;
        LocalDateTime now = LocalDateTime.now();
        for (LocalDateTime time : takenTimes) {
            if (time.toLocalDate().equals(now.toLocalDate())) {
                dosesToday++;
            }
        }
        return dosesToday >= maxDailyDose;
    }


    public boolean willOverDose() {

        int dosesToday = 0;
        LocalDateTime now = LocalDateTime.now();
        for (LocalDateTime time : takenTimes) {
            if (time.toLocalDate().equals(now.toLocalDate())) {
                dosesToday++;
            }
        }
        // Determine if the maximum daily dose will be exceeded if another dose is taken
        return (dosesToday + 1) > maxDailyDose;
    }
    
}