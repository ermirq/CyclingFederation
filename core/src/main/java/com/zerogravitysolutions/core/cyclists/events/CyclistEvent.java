package com.zerogravitysolutions.core.cyclists.events;

import com.zerogravitysolutions.core.cyclists.CyclistDTO;
import com.zerogravitysolutions.core.events.Event;
import com.zerogravitysolutions.core.events.EventType;

public record CyclistEvent(EventType eventType, CyclistDTO cyclistDTO) implements Event {}
