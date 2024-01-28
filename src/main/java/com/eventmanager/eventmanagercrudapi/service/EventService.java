package com.eventmanager.eventmanagercrudapi.service;

import com.eventmanager.eventmanagercrudapi.exceptions.EventNotFoundException;
import com.eventmanager.eventmanagercrudapi.model.Event;
import com.eventmanager.eventmanagercrudapi.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public void addEvent(Event event) {
        eventRepository.save(event);
    }
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    @Transactional
    public Event updateEvent(Long eventId, Event updatedEvent) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalEvent.isPresent()) {
            Event existingEvent = optionalEvent.get();

            existingEvent.setName(updatedEvent.getName());
            existingEvent.setInformation(updatedEvent.getInformation());
            existingEvent.setLocation(updatedEvent.getLocation());
            existingEvent.setDate(updatedEvent.getDate());
            existingEvent.setPopularity(updatedEvent.getPopularity());

            return eventRepository.save(existingEvent);
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

    @Transactional
    public boolean deleteEventById(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalEvent.isPresent()) {
            Event eventToDelete = optionalEvent.get();
            eventRepository.delete(eventToDelete);
            return true;
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

}
