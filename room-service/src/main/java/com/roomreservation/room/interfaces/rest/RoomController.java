package com.roomreservation.room.interfaces.rest;

import com.roomreservation.room.application.service.RoomApplicationService;
import com.roomreservation.room.domain.model.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomApplicationService roomApplicationService;

    public RoomController(RoomApplicationService roomApplicationService) {
        this.roomApplicationService = roomApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<Room>> listRooms() {
        return ResponseEntity.ok(roomApplicationService.listRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findRoom(@PathVariable Long id) {
        return roomApplicationService.findRoom(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomApplicationService.createRoom(room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        room.setId(id);
        roomApplicationService.updateRoom(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomApplicationService.deleteRoom(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<Room>> listActiveRooms() {
        return ResponseEntity.ok(roomApplicationService.listActiveRooms());
    }
} 