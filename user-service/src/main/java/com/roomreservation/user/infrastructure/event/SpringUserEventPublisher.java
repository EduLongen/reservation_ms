package com.roomreservation.user.infrastructure.event;

import com.roomreservation.user.domain.event.UserEvent;
import com.roomreservation.user.domain.event.UserEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringUserEventPublisher implements UserEventPublisher {
    
    private final ApplicationEventPublisher publisher;

    public SpringUserEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(UserEvent event) {
        publisher.publishEvent(event);
    }
} 