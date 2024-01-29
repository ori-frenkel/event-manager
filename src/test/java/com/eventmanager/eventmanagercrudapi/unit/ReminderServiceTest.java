package com.eventmanager.eventmanagercrudapi.unit;

import com.eventmanager.eventmanagercrudapi.service.ReminderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ScheduledExecutorService;
@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

    @Mock
    private ScheduledExecutorService scheduler;

    @InjectMocks
    private ReminderService reminderService;

    @Test
    void testScheduleReminder() throws InterruptedException {
    }

    @Test
    void testCancelEvent() {
    }

    @Test
    void testClearAllReminders() {
    }
}
