package com.roomreservation.reservation.infrastructure.messaging;

import com.roomreservation.reservation.domain.event.ReservationCreatedEvent;
import com.roomreservation.reservation.domain.event.ReservationDeletedEvent;
import com.roomreservation.reservation.domain.model.Reservation;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaReservationEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC_RESERVATION_CREATED = "reservation-created";
    private static final String TOPIC_RESERVATION_DELETED = "reservation-deleted";

    public KafkaReservationEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishReservationCreated(Reservation reservation) {
        ReservationCreatedEvent event = new ReservationCreatedEvent(reservation);
        kafkaTemplate.send(TOPIC_RESERVATION_CREATED, event);
    }

    public void publishReservationDeleted(Reservation reservation) {
        ReservationDeletedEvent event = new ReservationDeletedEvent(reservation);
        kafkaTemplate.send(TOPIC_RESERVATION_DELETED, event);
    }
} 