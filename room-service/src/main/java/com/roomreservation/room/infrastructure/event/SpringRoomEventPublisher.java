package com.roomreservation.room.infrastructure.event;

import com.roomreservation.room.domain.event.RoomEvent;
import com.roomreservation.room.domain.event.RoomEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringRoomEventPublisher implements RoomEventPublisher {
    
    private final ApplicationEventPublisher publisher;

    public SpringRoomEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(RoomEvent event) {
        publisher.publishEvent(event);
    }
} 