package com.zerogravitysolutions.core.events;

/**
 * Enum representing different types of events that can occur within the system.
 * 
 * <p>This enum should be expanded as new event types are introduced in the system.
 * Each event type should correspond to a specific kind of action or occurrence 
 * that needs to be handled by the event listeners.</p>
 */
public enum EventType {
    USER_CREATED,
    USER_UPDATED,
    USER_ROLES_UPDATED,
    USER_DELETED,
    KEYCLOAK_USER_CREATED,
    RACE_CREATED,
    RACE_UPDATED,
    CLUB_APPROVED,
    CLUB_DETAILS_UPDATED,
    NEW_CLUB_MEMBER,
    CYCLIST_CREATED,
    CYCLIST_UPDATED,
    CYCLIST_DEACTIVATED
    // add as much as needed
}
