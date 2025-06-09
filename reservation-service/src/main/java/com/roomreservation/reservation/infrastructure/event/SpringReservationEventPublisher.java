package com.roomreservation.reservation.infrastructure.event;

import com.roomreservation.reservation.domain.event.ReservationEvent;
import com.roomreservation.reservation.domain.event.ReservationEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringReservationEventPublisher implements ReservationEventPublisher {
    
    private final ApplicationEventPublisher publisher;

    public SpringReservationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(ReservationEvent event) {
        publisher.publishEvent(event);
    }
} 