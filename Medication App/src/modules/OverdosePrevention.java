// OverdosePrevention.java
package modules;

public class OverdosePrevention {
    // Method to check if a medicine dose is safe
    public static boolean isDoseSafe(Medicine medicine) {
        return !medicine.isOverDose();
    }

    // Method to alert user
    public static void alertOverdose(Medicine medicine) {
        System.out.println("Alert: Possible overdose detected for medicine: " + medicine.getName());
        // Additional alert mechanisms can be added here
    }
}