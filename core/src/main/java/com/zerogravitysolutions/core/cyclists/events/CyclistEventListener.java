package com.zerogravitysolutions.core.cyclists.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zerogravitysolutions.core.events.CustomEventListener;
import com.zerogravitysolutions.core.events.EventType;

/**
 * Component for listening to cyclist-related events.
 * 
 * <p>This component handles different types of cyclist events such as creation,
 * updates, and deletion by implementing specific methods for each event type.</p>
 */
@Component
public class CyclistEventListener {

    private static final Logger logger = LoggerFactory.getLogger(CyclistEventListener.class);

    @CustomEventListener(EventType.CYCLIST_CREATED)
    public void handleCyclistCreated(final CyclistEvent event) {
        logger.info("Handling cyclist created event: {}", event.cyclistDTO());

    }

    @CustomEventListener(EventType.CYCLIST_UPDATED)
    public void handleCyclistUpdated(final CyclistEvent event) {
        logger.info("Handling cyclist updated event: {}", event.cyclistDTO());

    }

    @CustomEventListener(EventType.CYCLIST_DEACTIVATED)
    public void handleCyclistDeleted(final CyclistEvent event) {
        logger.info("Handling cyclist deactivated event: {}", event.cyclistDTO());

    }
}
