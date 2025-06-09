package com.roomreservation.room.application.service;

import com.roomreservation.room.domain.model.Room;
import com.roomreservation.room.domain.service.RoomDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomApplicationService {

    private final RoomDomainService roomDomainService;

    public RoomApplicationService(RoomDomainService roomDomainService) {
        this.roomDomainService = roomDomainService;
    }

    public List<Room> listRooms() {
        return roomDomainService.listRooms();
    }

    public Optional<Room> findRoom(Long id) {
        return roomDomainService.findRoom(id);
    }

    public Room createRoom(Room room) {
        return roomDomainService.createRoom(room);
    }

    public void updateRoom(Room room) {
        roomDomainService.updateRoom(room);
    }

    public void deleteRoom(Long id) {
        roomDomainService.deleteRoom(id);
    }

    public List<Room> listActiveRooms() {
        return roomDomainService.listActiveRooms();
    }
} 