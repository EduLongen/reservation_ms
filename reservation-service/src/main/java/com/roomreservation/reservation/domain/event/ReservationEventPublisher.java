package com.roomreservation.reservation.domain.event;

public interface ReservationEventPublisher {
    void publish(ReservationEvent event);
} 