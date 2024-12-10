package services;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MedicationScheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void scheduleAlert(String timeToTake, Runnable task) {
        LocalTime targetTime = LocalTime.parse(timeToTake, DateTimeFormatter.ofPattern("HH:mm"));

        LocalTime now = LocalTime.now();
        long delay = Duration.between(now, targetTime).getSeconds();
        if (delay < 0) {
            delay += 24 * 60 * 60;
        }

        System.out.println("Scheduling alert for: " + targetTime + " (in " + delay + " seconds)");

        scheduler.schedule(task, delay, TimeUnit.SECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}