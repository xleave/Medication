package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MedicationAlert {

    private String csvFilePath;
    private final MedicationScheduler scheduler;
    private final TextToSpeech tts;

    public MedicationAlert(String username, MedicationScheduler scheduler, TextToSpeech tts) {
        this.csvFilePath = "src/main/resources/medications/" + username + "_medications.csv";
        this.scheduler = scheduler;
        this.tts = tts;

        loadMedicationsAndSchedule();
    }

    public void loadMedicationsAndSchedule() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(csvFilePath));
            for (String line : lines) {
                String[] parts = line.split(",");
                String timeToTake = parts[3].trim();
                String medicationName = parts[0].trim();
                String dosage = parts[1].trim();

                scheduler.scheduleAlert(timeToTake, () -> {
                    String message = String.format(
                            "This is the voice notification to take your medication : %s, Dosage: %s.",
                            medicationName, dosage
                    );
                    System.out.println(message);
                    tts.speak(message);
                });
            }
        } catch (IOException e) {
            System.err.println("Error reading medication file: " + e.getMessage());
        }
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }
}