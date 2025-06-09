package com.roomreservation.room.domain.event;

import com.roomreservation.room.domain.model.Room;

public interface RoomEvent {
    Room getRoom();
} 