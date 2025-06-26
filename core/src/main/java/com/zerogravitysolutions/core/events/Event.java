package com.zerogravitysolutions.core.events;

/**
 * Interface representing a generic event within the system.
 * 
 * <p>All specific event types should implement this interface to ensure they include 
 * an {@link EventType}. This facilitates consistent handling and processing of 
 * events across the system.</p>
 */
public interface Event {
    EventType eventType();
}