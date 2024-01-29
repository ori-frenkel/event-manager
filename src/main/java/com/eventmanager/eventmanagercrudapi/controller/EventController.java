package com.eventmanager.eventmanagercrudapi.controller;

import com.eventmanager.eventmanagercrudapi.model.Event;
import com.eventmanager.eventmanagercrudapi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiImplicitParam;
import java.util.List;

@RestController
@RequestMapping("/event")
@Api(tags = "Event Controller", description = "APIs for managing events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @ApiOperation("Create a new event")
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Event event) {
        try {
            eventService.addEvent(event);
            return ResponseEntity.status(HttpStatus.CREATED).body("Event created successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create event");
        }
    }
    
    @GetMapping("/all")
    @ApiOperation(
            value = "Get all events with optional filtering and sorting",
            notes = "Example request: /events?location=Tel-Aviv&sortBy=date&sortDirection=asc")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "location", value = "Filter events by location",
                    example = "Tel-Aviv", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortBy", value = "Sort events by a specific property",
                    example = "date", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", value = "Sort direction (asc/desc)",
                    example = "asc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<List<Event>> getAll(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        return ResponseEntity.ok(eventService.getAllEvents(location, sortBy, sortDirection));
    }

    @GetMapping("/{id}")
    @ApiOperation("Get event by id")
    public ResponseEntity<Event> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping()
    @ApiOperation("update event by id")
    public ResponseEntity<Event> update(@RequestBody Event newEventDetails) {
        return ResponseEntity.ok(eventService.updateEvent(newEventDetails));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("delete event by id")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(
                String.format("event %d deleted successfully = %b", id, eventService.deleteEventById(id)));

    }
}
