package com.eventmanager.eventmanagercrudapi.controller;

import com.eventmanager.eventmanagercrudapi.model.Event;
import com.eventmanager.eventmanagercrudapi.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batch/events")
@Api(tags = "Event Controller for batch operations", description = "APIs for managing batch of events")
public class BatchEventController {
    private final EventService eventService;

    @Autowired
    public BatchEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @ApiOperation("Create multiple events in a batch")
    @PostMapping()
    public ResponseEntity<String> createBatch(@RequestBody List<Event> events) {
        try {
            eventService.addEvents(events);
            return ResponseEntity.status(HttpStatus.CREATED).body("Events created successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create events");
        }
    }

    @PutMapping()
    public List<Event> updateBatch(@RequestBody List<Event> updatedEvents) {
        return eventService.updateEvents(updatedEvents);
    }

    @DeleteMapping()
    @ApiOperation("delete batch of events by id")
    public ResponseEntity<String> deleteBatch(@RequestBody List<Long> eventIds) {
        return ResponseEntity.ok(
                String.format("event %s deleted successfully = %b",
                        eventIds,
                        eventService.deleteEventBatchByIds(eventIds)));

    }
}
