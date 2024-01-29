package com.eventmanager.eventmanagercrudapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String information;
    private String location;
    private LocalDateTime date;
    @CreationTimestamp
    private LocalDateTime creationTime;
    private int popularity;

    //generate all args constructor

    public Event(String name, String information, String location, LocalDateTime date, int popularity) {
        this.name = name;
        this.information = information;
        this.location = location;
        this.date = date;
        this.popularity = popularity;
    }


    public Event update(Event otherEvent) {
        if (otherEvent != null) {
            this.setName(otherEvent.getName());
            this.setInformation(otherEvent.getInformation());
            this.setLocation(otherEvent.getLocation());
            this.setDate(otherEvent.getDate());
            this.setPopularity(otherEvent.getPopularity());
        }
        return this;
    }
}
