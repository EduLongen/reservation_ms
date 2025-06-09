package com.roomreservation.reservation.domain.service;

import com.roomreservation.reservation.domain.event.ReservationEventPublisher;
import com.roomreservation.reservation.domain.event.ReservationUpdatedEvent;
import com.roomreservation.reservation.domain.event.ReservationCreatedEvent;
import com.roomreservation.reservation.domain.event.ReservationDeletedEvent;
import com.roomreservation.reservation.domain.model.Reservation;
import com.roomreservation.reservation.domain.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationDomainService {

    private final ReservationRepository reservationRepository;
    private final ReservationEventPublisher eventPublisher;

    public ReservationDomainService(ReservationRepository reservationRepository, ReservationEventPublisher eventPublisher) {
        this.reservationRepository = reservationRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Reservation> listReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> findReservation(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(Reservation reservation) {
        Reservation savedReservation = reservationRepository.save(reservation);
        eventPublisher.publish(new ReservationCreatedEvent(savedReservation));
        return savedReservation;
    }

    public void updateReservation(Reservation reservation) {
        Reservation updatedReservation = reservationRepository.save(reservation);
        eventPublisher.publish(new ReservationUpdatedEvent(updatedReservation));
    }

    public void deleteReservation(Long id) {
        reservationRepository.findById(id).ifPresent(reservation -> {
            reservationRepository.deleteById(id);
            eventPublisher.publish(new ReservationDeletedEvent(reservation));
        });
    }

    public List<Reservation> listReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> listReservationsByRoom(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }
} 