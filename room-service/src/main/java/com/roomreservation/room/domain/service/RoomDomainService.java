package com.roomreservation.room.domain.service;

import com.roomreservation.room.domain.event.RoomEventPublisher;
import com.roomreservation.room.domain.event.RoomUpdatedEvent;
import com.roomreservation.room.domain.event.RoomCreatedEvent;
import com.roomreservation.room.domain.event.RoomDeletedEvent;
import com.roomreservation.room.domain.model.Room;
import com.roomreservation.room.domain.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomDomainService {

    private final RoomRepository roomRepository;
    private final RoomEventPublisher eventPublisher;

    public RoomDomainService(RoomRepository roomRepository, RoomEventPublisher eventPublisher) {
        this.roomRepository = roomRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Room> listRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> findRoom(Long id) {
        return roomRepository.findById(id);
    }

    public Room createRoom(Room room) {
        Room savedRoom = roomRepository.save(room);
        eventPublisher.publish(new RoomCreatedEvent(savedRoom));
        return savedRoom;
    }

    public void updateRoom(Room room) {
        Room updatedRoom = roomRepository.save(room);
        eventPublisher.publish(new RoomUpdatedEvent(updatedRoom));
    }

    public void deleteRoom(Long id) {
        roomRepository.findById(id).ifPresent(room -> {
            roomRepository.deleteById(id);
            eventPublisher.publish(new RoomDeletedEvent(room));
        });
    }

    public List<Room> listActiveRooms() {
        return roomRepository.findByActiveTrue();
    }
} 