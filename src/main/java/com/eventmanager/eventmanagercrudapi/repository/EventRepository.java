package com.eventmanager.eventmanagercrudapi.repository;

import com.eventmanager.eventmanagercrudapi.model.Event;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(int id);
    List<Event> findByLocation(String location);
    List<Event> findByLocation(String location, Sort sort);

    List<Event> findAll(Sort sort);
}
