package services;

import javax.swing.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MedicationScheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void scheduleAlert(String timeToTake, Runnable task) {
        try {
            LocalTime targetTime = LocalTime.parse(timeToTake, DateTimeFormatter.ofPattern("HH:mm"));

            LocalTime now = LocalTime.now();
            long delay = Duration.between(now, targetTime).getSeconds();
            if (delay < 0) {
                delay += 24 * 60 * 60;
            }

            System.out.println("Scheduling alert for: " + targetTime + " (in " + delay + " seconds)");

            scheduler.schedule(task, delay, TimeUnit.SECONDS);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid time format: " + timeToTake + ". Expected format is HH:mm.");

        } catch (Exception e) {
            System.err.println("An error occurred while scheduling the alert: " + e.getMessage());

        }
    }
    public void shutdown() {
        scheduler.shutdown();
    }
}