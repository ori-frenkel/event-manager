package com.eventmanager.eventmanagercrudapi.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        // calling the constructor of parent Exception
        super("Event not found with ID: " + id);
    }
}
