package com.eventmanager.eventmanagercrudapi.unit;

import com.eventmanager.eventmanagercrudapi.model.Event;
import com.eventmanager.eventmanagercrudapi.repository.EventRepository;
import com.eventmanager.eventmanagercrudapi.service.EventService;
import com.eventmanager.eventmanagercrudapi.service.ReminderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ReminderService reminderService;

    @InjectMocks
    private EventService eventService;

    private final LocalDateTime constantDate = LocalDateTime.of(2021, 2, 15, 14, 30);

    private Event sampleEvent;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Sample Event for testing
        sampleEvent = new Event(1L, "event1", "info1", "location1", constantDate, constantDate.plusDays(1), 10);
    }

    @Test
    @DisplayName("Test addEvent function - service to add an event")
    void testAddEvent() {
        // Arrange
        when(eventRepository.save(any(Event.class))).thenReturn(sampleEvent);

        // Act
        eventService.addEvent(sampleEvent);

        // Assert
        verify(eventRepository, times(1)).save(sampleEvent);
        verify(reminderService, times(1)).scheduleReminder(sampleEvent);
    }

    @Test
    @DisplayName("Test addEvents function - adding multiple events at once")
    void testAddEvents() {
        // Arrange
        List<Event> events = Arrays.asList(sampleEvent, new Event("Event 2", "Info 2", "Location 2",
                LocalDateTime.now().plusDays(1), 15));

        when(eventRepository.saveAll(anyIterable())).thenReturn(events);

        // Act
        eventService.addEvents(events);

        // Assert
        verify(eventRepository, times(1)).saveAll(events);
        verify(reminderService, times(1)).scheduleReminder(sampleEvent);
        verify(reminderService, times(1)).scheduleReminder(events.get(1));
    }

    @Test
    @DisplayName("Test getAllEvents")
    void testGetAllEvents() {
        // Arrange
        List<Event> expectedEvents = Arrays.asList(sampleEvent, new Event("Event 2", "Info 2", "Location 2",
                LocalDateTime.now().plusDays(1), 15));

        when(eventRepository.findByLocation(eq("Sample Location"), any(Sort.class))).thenReturn(expectedEvents);

        // Act
        List<Event> result = eventService.getAllEvents("Sample Location", "date", "asc");

        // Assert
        assertEquals(expectedEvents, result);
    }

    @Test
    @DisplayName("Test getEventById")
    void testGetEventById() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.of(sampleEvent));

        // Act
        Event result = eventService.getEventById(1L);

        // Assert
        assertEquals(sampleEvent, result);
    }

    @Test
    @DisplayName("Test updateEvent")
    void testUpdateEvent() {
        // Arrange
        Event updatedEvent = new Event(1L, "Updated Event", "Updated Info", "Updated Location",
                constantDate.plusDays(5), constantDate.plusDays(8), 20);
        Event expectedEvent = new Event(sampleEvent.getId(), "Updated Event", "Updated Info", "Updated Location",
                constantDate.plusDays(5), sampleEvent.getCreationTime(), 20);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(sampleEvent));
        when(eventRepository.save(sampleEvent)).thenReturn(expectedEvent);

        // Act
        Event result = eventService.updateEvent(updatedEvent);

        // Assert
        verify(eventRepository, times(1)).save(expectedEvent);
        verify(reminderService, times(1)).cancelEvent(1L);
        verify(reminderService, times(1)).scheduleReminder(expectedEvent);
        assertEquals(result, expectedEvent);
    }

    @Test
    @DisplayName("Test updateEvents")
    void testUpdateEvents() {
    }

    @Test
    void testDeleteEventById() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.of(sampleEvent));

        // Act
        boolean result = eventService.deleteEventById(1L);

        // Assert
        assertTrue(result);
        verify(eventRepository, times(1)).delete(sampleEvent);
        verify(reminderService, times(1)).cancelEvent(1L);
    }

    @Test
    void testDeleteEventBatchByIds() {
        // Arrange
        List<Long> eventIds = Arrays.asList(1L, 2L);
        Event secondSampleEvent = new Event(2L, "Event 2", "Info 2", "Location 2",
                LocalDateTime.now().plusDays(1), constantDate, 15);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(sampleEvent));
        when(eventRepository.findById(2L)).thenReturn(Optional.of(secondSampleEvent));

        // Act
        boolean result = eventService.deleteEventBatchByIds(eventIds);

        // Assert
        assertTrue(result);
        verify(eventRepository, times(1)).delete(sampleEvent);
        verify(eventRepository, times(1)).delete(secondSampleEvent);
        verify(reminderService, times(2)).cancelEvent(anyLong());
    }

    @Test
    void testClearEvents() {
        // Act
        eventService.clearEvents();

        // Assert
        verify(eventRepository, times(1)).deleteAll();
        verify(reminderService, times(1)).clearAllReminders();
    }
}
