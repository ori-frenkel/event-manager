package com.eventmanager.eventmanagercrudapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private String information;
    @NotBlank
    private String location;
    @NotNull
    private LocalDateTime date;
    @CreationTimestamp
    @NotNull
    private LocalDateTime creationTime;
    private int popularity;


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
