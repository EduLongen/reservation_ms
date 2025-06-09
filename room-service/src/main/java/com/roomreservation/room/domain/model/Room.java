package com.roomreservation.room.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Room {
    private Long id;
    private String name;
    private Integer capacity;
    private String location;
    private Boolean active;

    public Room(Long id, String name, Integer capacity, String location, Boolean active) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.active = active;
    }
} 