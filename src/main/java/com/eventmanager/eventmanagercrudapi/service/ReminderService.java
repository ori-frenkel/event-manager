package com.eventmanager.eventmanagercrudapi.service;

import com.eventmanager.eventmanagercrudapi.model.Event;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.*;


@Service
public class ReminderService {
    private static final Logger logger = LoggerFactory.getLogger(ReminderService.class);
    private static final long DEFAULT_REMINDER_DELAY_IN_MINUTES = 30L;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public void scheduleReminder(Event event) {
        scheduleReminder(event, DEFAULT_REMINDER_DELAY_IN_MINUTES);
    }

    public void scheduleReminder(Event event, long minutesBeforeEventStart) {
        // can be done this way as well.
        // Runnable reminderTask = () -> logger.info("event {} start in 30 minutes: ", eventName);

        long delay = calculateTaskDelay(event, minutesBeforeEventStart);
        if (delay <= 0)
            return;

        ScheduledFuture<?> scheduledFuture = scheduler.schedule(new ReminderTask(event), delay, TimeUnit.MILLISECONDS);
        scheduledTasks.put(event.getId(), scheduledFuture);
    }

    public void cancelEvent(long eventId) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.remove(eventId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    @AllArgsConstructor
    private static class ReminderTask implements Runnable {
        private final Event event;

        @Override
        public void run() {
            // For simplicity, let's log a reminder message
            logger.info("Reminder: Event '{}' is scheduled to start in 30 minutes or less.", event.getName());
        }
    }

    private static long calculateTaskDelay(Event event, long minutesBeforeEventStart) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = event.getDate().minusMinutes(minutesBeforeEventStart);

        return Duration.between(now, reminderTime).toMillis();
    }
}
