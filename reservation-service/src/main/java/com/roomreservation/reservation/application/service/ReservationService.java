package com.roomreservation.reservation.application.service;

import com.roomreservation.reservation.domain.model.Reservation;
import com.roomreservation.reservation.domain.repository.ReservationRepository;
import com.roomreservation.reservation.infrastructure.messaging.KafkaReservationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final KafkaReservationEventPublisher eventPublisher;

    public ReservationService(ReservationRepository reservationRepository, KafkaReservationEventPublisher eventPublisher) {
        this.reservationRepository = reservationRepository;
        this.eventPublisher = eventPublisher;
    }

    public Reservation createReservation(Long roomId, Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Check if there's already a reservation for the room that overlaps with the requested period
        List<Reservation> existingReservations = reservationRepository.findByRoomId(roomId);
        boolean conflict = existingReservations.stream().anyMatch(r ->
            (startDateTime.isBefore(r.getEndDateTime()) && endDateTime.isAfter(r.getStartDateTime()))
        );
        if (conflict) {
            throw new RuntimeException("There is already a reservation for this room in the selected period");
        }

        Reservation reservation = new Reservation();
        reservation.setRoomId(roomId);
        reservation.setUserId(userId);
        reservation.setStartDateTime(startDateTime);
        reservation.setEndDateTime(endDateTime);
        
        reservation = reservationRepository.save(reservation);
        eventPublisher.publishReservationCreated(reservation);
        
        return reservation;
    }

    public void cancelReservation(Long id) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservationRepository.delete(reservation);
            eventPublisher.publishReservationDeleted(reservation);
        }
    }

    public List<Reservation> listReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> findReservation(Long id) {
        return reservationRepository.findById(id);
    }
} 