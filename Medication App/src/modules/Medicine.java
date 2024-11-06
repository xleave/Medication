// Medicine.java
package modules;

public class Medicine {
    private String name;
    private String dosage;
    private int quantity;
    private String timeToTake;
    private String userName;

    public Medicine(String name, String dosage, int quantity, String timeToTake, String userName) {
        this.name = name;
        this.dosage = dosage;
        this.quantity = quantity;
        this.timeToTake = timeToTake;
        this.userName = userName;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
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
}