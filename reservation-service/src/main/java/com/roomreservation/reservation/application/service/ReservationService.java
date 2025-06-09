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

    public Reservation createReservation(Long roomId, Long userId, LocalDateTime dateTime) {
        // Check if there's already a reservation for the room at the same time
        List<Reservation> existingReservations = reservationRepository.findByRoomIdAndDateTimeBetween(
            roomId, dateTime, dateTime.plusHours(1));
        
        if (!existingReservations.isEmpty()) {
            throw new RuntimeException("There is already a reservation for this room at this time");
        }

        Reservation reservation = new Reservation();
        reservation.setRoomId(roomId);
        reservation.setUserId(userId);
        reservation.setDateTime(dateTime);
        
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