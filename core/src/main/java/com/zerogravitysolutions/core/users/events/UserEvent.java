package com.zerogravitysolutions.core.users.events;

import com.zerogravitysolutions.core.events.Event;
import com.zerogravitysolutions.core.events.EventType;
import com.zerogravitysolutions.core.users.UserDTO;

import java.util.List;

public record UserEvent(EventType eventType, UserDTO userDto, List<String> roles) implements Event {

    public UserEvent(final EventType eventType, final UserDTO userDto){
            this(eventType, userDto, null);
        }
    }
