package com.eventmanager.eventmanagercrudapi.service;

import com.eventmanager.eventmanagercrudapi.exceptions.EventNotFoundException;
import com.eventmanager.eventmanagercrudapi.model.Event;
import com.eventmanager.eventmanagercrudapi.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ReminderService reminderService;

    @Autowired
    public EventService(EventRepository eventRepository, ReminderService reminderService) {
        this.eventRepository = eventRepository;
        this.reminderService = reminderService;
    }

    @Transactional
    public void addEvent(Event event) {
        eventRepository.save(event);
        reminderService.scheduleReminder(event);
    }

    @Transactional
    // Any exception thrown within this method will rollback the entire transaction
    public void addEvents(List<Event> events) {
        eventRepository.saveAll(events);
        batchScheduleReminder(events);
    }

    public List<Event> getAllEvents(String location,String sortBy, String sortDirection) {
        List<Event> events;
        boolean isLocationEmpty = location == null || location.isEmpty();
        boolean isSortByEmpty = sortDirection == null || sortDirection.isEmpty();

        if (!isLocationEmpty && !isSortByEmpty) {
            // find by location and by given sort
            events = eventRepository.findByLocation(location, getSortBy(sortBy, sortDirection));
        } else if (!isLocationEmpty) {
            // find by location only
            events = eventRepository.findByLocation(location);
        } else if (isSortByEmpty){
            // find by given sort only
            events = eventRepository.findAll(getSortBy(sortBy, sortDirection));
        } else {
            // find all without specific location or sort
            events = eventRepository.findAll();
        }

        return events;
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    @Transactional
    public Event updateEvent(Event updatedEvent) {
        Long eventId = updatedEvent.getId();
        Optional<Event> optionalCurrentEvent = eventRepository.findById(eventId);

        if(optionalCurrentEvent.isPresent()) {
            Event currentEvent = optionalCurrentEvent.get();
            currentEvent.setName(updatedEvent.getName());
            currentEvent.setInformation(updatedEvent.getInformation());
            currentEvent.setLocation(updatedEvent.getLocation());
            currentEvent.setDate(updatedEvent.getDate());
            currentEvent.setPopularity(updatedEvent.getPopularity());
            if (!currentEvent.getDate().equals(updatedEvent.getDate())) {
                currentEvent.setDate(updatedEvent.getDate());
                updateReminderSchedule(currentEvent);
            }

            return eventRepository.save(currentEvent);
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

    @Transactional
    public List<Event> updateEvents(List<Event> updatedEvents) {
        ArrayList<Event> eventsThatNeedToReschedule = new ArrayList<>();

        for (Event updatedEvent : updatedEvents) {
            Optional<Event> existingEventOptional = eventRepository.findById(updatedEvent.getId());
            existingEventOptional.ifPresentOrElse(existingEvent -> {
                existingEvent.setName(updatedEvent.getName());
                existingEvent.setInformation(updatedEvent.getInformation());
                existingEvent.setLocation(updatedEvent.getLocation());
                existingEvent.setDate(updatedEvent.getDate());
                existingEvent.setPopularity(updatedEvent.getPopularity());
                if (!existingEvent.getDate().equals(updatedEvent.getDate())) {
                    existingEvent.setDate(updatedEvent.getDate());
                    eventsThatNeedToReschedule.add(existingEvent);
                }
            }, () -> { throw new EventNotFoundException(updatedEvent.getId()); });
        }

        eventRepository.saveAll(updatedEvents);
        eventReminderReschedule(eventsThatNeedToReschedule);

        return updatedEvents;
    }

    @Transactional
    public boolean deleteEventById(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalEvent.isPresent()) {
            Event eventToDelete = optionalEvent.get();
            eventRepository.delete(eventToDelete);
            reminderService.cancelEvent(eventId);

            return true;
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

    @Transactional
    public boolean deleteEventBatchByIds(List<Long> eventIds) {
        for (Long eventId : eventIds) {
            Optional<Event> optionalEvent = eventRepository.findById(eventId);
            optionalEvent.ifPresentOrElse(eventRepository::delete,
                    () -> { throw new EventNotFoundException(eventId); });
        }

        eventIds.forEach(reminderService::cancelEvent);
        return true;
    }

    private Sort.Direction getSortDirection(String sortDirection) {
        if (sortDirection == null || sortDirection.isEmpty()) {
            return Sort.Direction.ASC;
        }

        if (sortDirection.equalsIgnoreCase("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    private Sort getSortBy(String sortBy, String sortDirection) {
        if (sortBy == null || sortBy.isEmpty()) {
            return Sort.by(Sort.Direction.ASC, "date");
        }

        Sort.Direction direction = getSortDirection(sortDirection);
        return switch (sortBy.toLowerCase()) {
            case "popularity" -> Sort.by(direction, "popularity");
            case "creationtime" -> Sort.by(direction, "creationTime");
            default -> Sort.by(direction, "date");
        };
    }

    private void eventReminderReschedule(ArrayList<Event> eventsThatNeedToReschedule) {
        for (Event event : eventsThatNeedToReschedule) {
            updateReminderSchedule(event);
        }
    }

    private void batchScheduleReminder(List<Event> events) {
        for (Event event : events) {
            reminderService.scheduleReminder(event);
        }
    }

    private void updateReminderSchedule(Event existingEvent) {
        reminderService.cancelEvent(existingEvent.getId());
        reminderService.scheduleReminder(existingEvent);
    }
}
