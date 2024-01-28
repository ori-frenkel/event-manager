package com.eventmanager.eventmanagercrudapi.repository;

import com.eventmanager.eventmanagercrudapi.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(int id);
}
