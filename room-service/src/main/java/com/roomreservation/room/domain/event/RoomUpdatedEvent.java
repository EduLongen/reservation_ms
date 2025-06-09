package com.roomreservation.room.domain.event;

import com.roomreservation.room.domain.model.Room;

public class RoomUpdatedEvent implements RoomEvent {
    private final Room room;

    public RoomUpdatedEvent(Room room) {
        this.room = room;
    }

    @Override
    public Room getRoom() {
        return room;
    }
} 