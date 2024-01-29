package com.eventmanager.eventmanagercrudapi.integ;

import com.eventmanager.eventmanagercrudapi.controller.EventController;
import com.eventmanager.eventmanagercrudapi.model.Event;
import com.eventmanager.eventmanagercrudapi.service.EventService;
import com.eventmanager.eventmanagercrudapi.utils.EncodersUtils;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventControllerTest {

    @Autowired
    private EventController eventController;

    @Autowired
    private EventService eventService;

    @Autowired
    private MockMvc mockMvc;

    private final LocalDateTime constantDate = LocalDateTime.of(2021, 2, 15, 14, 30);

    private Event firstEvent;
    private Event secondEvent;


    @BeforeEach
    public void init() {
        // Clear events in the service before each test
        eventService.clearEvents();

        // Create some sample events
        firstEvent = new Event("event1", "info1", "location1", constantDate, 10);
        secondEvent = new Event("event2", "info2", "location2", constantDate.plusDays(1), 5);

        // Save events to the service
        eventService.addEvent(firstEvent);
        eventService.addEvent(secondEvent);
    }

    @Test
    @DisplayName("Test basic POST request to create event /event/create")
    void testCreateNewEvent() {
        // Arrange
        Event eventToCreate = new Event("3", "3", "3", LocalDateTime.of(2023, 2, 15, 14, 30), 3);

        // Act
        ResponseEntity<String> response = eventController.create(eventToCreate);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Event created successfully!", response.getBody());
    }

    @Test
    @DisplayName("Test basic GET request to /event/all with no query parameters")
    void getAllWithNoQueryParamTest() throws Exception {
        int expectedNumberOfEvents = 2;

        // Perform a GET request to the endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/event/all")
                        .header("Authorization", "Basic " +
                                EncodersUtils.getBase64Credentials("ori", "password")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(expectedNumberOfEvents))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id")
                        .value(hasItems(firstEvent.getId().intValue(), secondEvent.getId().intValue())));
    }

    @Test
    @DisplayName("Test basic GET request to /event/all with location query parameters")
    void getAllWithLocationQueryParamTest() throws Exception {
        int expectedNumberOfEvents = 2;
        String expectedLocation = "israel";
        Event thirdEvent = new Event("event3", "info3", expectedLocation, LocalDateTime.now().plusDays(2), 9);
        Event fourthEvent = new Event("event4", "info4", expectedLocation, LocalDateTime.now().plusDays(3), 1);

        eventService.addEvent(thirdEvent);
        eventService.addEvent(fourthEvent);

        // Perform a GET request to the endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/event/all")
                        .header("Authorization", "Basic " +
                                EncodersUtils.getBase64Credentials("ori", "password"))
                        .param("location", expectedLocation))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(expectedNumberOfEvents))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].location").value(
                        everyItem(equalTo(expectedLocation))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id")
                        .value(hasItems(thirdEvent.getId().intValue(), fourthEvent.getId().intValue())));
    }

    @ParameterizedTest
    @CsvSource({
            "date,asc",
            "date,desc",
            "popularity,asc",
            "popularity,desc",
            "creationtime,asc",
            "creationtime,desc"
    })
    @DisplayName("Test basic GET request to /event/all with location query parameters and sorting")
    void getAllWithLocationAndSortingTest(String sortBy, String sortDirection) throws Exception {
        int expectedNumberOfEvents = 2;
        String expectedLocation = "israel";
        Event thirdEvent = new Event("event3", "info3", expectedLocation, LocalDateTime.now().plusDays(2), 9);
        Event fourthEvent = new Event("event4", "info4", expectedLocation, LocalDateTime.now().plusDays(3), 1);

        eventService.addEvent(thirdEvent);
        eventService.addEvent(fourthEvent);

        // Perform a GET request to the endpoint
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/event/all")
                        .header("Authorization", "Basic " +
                                EncodersUtils.getBase64Credentials("ori", "password"))
                        .param("location", expectedLocation)
                        .param("sortBy", sortBy)
                        .param("sortDirection", sortDirection))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(expectedNumberOfEvents))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].location").value(
                        everyItem(equalTo(expectedLocation))))
                .andReturn();

        // Get the list of IDs from the response, sorted based on the specified property and direction
        List<Integer> actualOrder = JsonPath.read(result.getResponse().getContentAsString(), "$[*].id");
        List<Integer> expectedOrder = sortEvents(sortBy, sortDirection, thirdEvent, fourthEvent);

        // Verify the order of IDs based on sorting
        assertEquals(actualOrder, expectedOrder);
    }

    @Test
    @DisplayName("Test basic GET request to /event/{id} - get event by id")
    void testGetEventById() throws Exception {
        Event exisitngEvent = eventService.getAllEvents(null, null, null).get(0);
        Long eventId = exisitngEvent.getId(); // the id is generated by the DB and cannot be overriden (can be be shouldn't)

        mockMvc.perform(MockMvcRequestBuilders.get("/event/{id}", eventId)
                        .header("Authorization", "Basic " +
                                EncodersUtils.getBase64Credentials("ori", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(eventId.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(exisitngEvent.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.information").value(exisitngEvent.getInformation()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location").value(exisitngEvent.getLocation()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.popularity").value(exisitngEvent.getPopularity()));
    }

    private List<Integer> sortEvents(String sortBy, String sortDirection, Event... events) {
        Comparator<Event> comparator = getComparator(sortBy, sortDirection);

        return Arrays.stream(events)
                .sorted(comparator)
                .map(event -> event.getId().intValue())
                .collect(Collectors.toList());
    }

    private Comparator<Event> getComparator(String sortBy, String sortDirection) {
        Comparator<Event> comparator = switch (sortBy.toLowerCase()) {
            case "date" -> Comparator.comparing(Event::getDate);
            case "popularity" -> Comparator.comparing(Event::getPopularity);
            case "creationtime" -> Comparator.comparing(Event::getCreationTime);
            default -> throw new IllegalArgumentException("Invalid sortBy parameter");
        };

        if ("desc".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    /// and so on, you get the idea, need to add more tests for the other methods
}