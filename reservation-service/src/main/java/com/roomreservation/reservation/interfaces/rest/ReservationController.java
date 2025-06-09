package com.roomreservation.reservation.interfaces.rest;

import com.roomreservation.reservation.application.service.ReservationService;
import com.roomreservation.reservation.domain.model.Reservation;
import com.roomreservation.reservation.interfaces.rest.dto.ReservationRequest;
import com.roomreservation.reservation.interfaces.rest.dto.ReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> listReservations() {
        List<Reservation> reservations = reservationService.listReservations();
        List<ReservationResponse> response = reservations.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> findReservation(@PathVariable Long id) {
        return reservationService.findReservation(id)
            .map(this::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        LocalDateTime dateTime = LocalDateTime.parse(request.getDateTime(), formatter);
        Reservation reservation = reservationService.createReservation(
            request.getRoomId(),
            request.getUserId(),
            dateTime
        );
        return ResponseEntity.ok(toResponse(reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getRoomId(),
            reservation.getUserId(),
            reservation.getDateTime().format(formatter)
        );
    }
} 