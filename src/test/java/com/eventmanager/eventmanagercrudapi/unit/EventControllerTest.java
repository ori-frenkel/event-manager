package com.eventmanager.eventmanagercrudapi.unit;

import com.eventmanager.eventmanagercrudapi.controller.EventController;
import com.eventmanager.eventmanagercrudapi.model.Event;
import com.eventmanager.eventmanagercrudapi.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private final LocalDateTime constantDate = LocalDateTime.of(2021, 2, 15, 14, 30);

    private Event firstEvent;
    private Event secondEvent;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        // Create some sample events
        firstEvent = new Event("event1", "info1", "location1", constantDate, 10);
        secondEvent = new Event("event2", "info2", "location2", constantDate.plusDays(1), 5);
    }

    @Test
    @DisplayName("Test basic POST request to create event /event/create")
    void testCreateNewEvent() {
        // Arrange
        Event eventToCreate = new Event("3", "3", "3", LocalDateTime.of(2023, 2, 15, 14, 30), 3);

        // Mock the service method
        doNothing().when(eventService).addEvent(eventToCreate);

        // Act
        ResponseEntity<String> response = eventController.create(eventToCreate);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Event created successfully!", response.getBody());

        // Verify that the service method was called
        verify(eventService, times(1)).addEvent(eventToCreate);
    }

    @Test
    @DisplayName("Test basic GET request to /event/{id} - get event by id")
    void testGetEventById() {
        // Arrange
        Long eventId = 1L;

        // Mock the service method
        when(eventService.getEventById(eventId)).thenReturn(firstEvent);

        // Act
        ResponseEntity<Event> response = eventController.getById(eventId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(firstEvent, response.getBody());

        // Verify that the service method was called
        verify(eventService, times(1)).getEventById(eventId);
    }

    @Test
    @DisplayName("Test basic GET request to /event/all with location query parameters and sorting")
    void getAllWithLocationAndSortingTest() {
        // Mock the service method
        when(eventService.getAllEvents("israel", "date", "asc"))
                .thenReturn(Arrays.asList(firstEvent, secondEvent));

        // Act
        List<Event> response = eventController.getAll("israel", "date", "asc").getBody();

        // Assert
        assert response != null;
        assertEquals(2, response.size());

        // Verify that the service method was called
        verify(eventService, times(1)).getAllEvents("israel", "date", "asc");
    }
}
