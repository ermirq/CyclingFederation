package com.zerogravitysolutions.core.events;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EventHandlerAspect implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(EventHandlerAspect.class);
    private final ApplicationContext applicationContext;
    private final ConcurrentHashMap<EventType,
            ConcurrentHashMap<Object, Method>> eventHandlers = new ConcurrentHashMap<>();

    public EventHandlerAspect(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        final Map<String, Object> beans = applicationContext.getBeansOfType(Object.class);
        for (Object bean : beans.values()) {
            final Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(CustomEventListener.class)) {
                    final CustomEventListener annotation =
                            method.getAnnotation(CustomEventListener.class);
                    final EventType eventType = annotation.value();
                    eventHandlers.computeIfAbsent(eventType, k -> new ConcurrentHashMap<>())
                            .put(bean, method);
                    logger.info("Registered {} for event type {}", method.getName(), eventType);
                }
            }
        }
        logger.info("Event handlers have been initialized and are ready for action.");
    }

    @EventListener
    public void handleEvent(final Event event) {
        final EventType eventType = event.eventType();
        final Map<Object, Method> handlers = eventHandlers.get(eventType);
        if (handlers != null) {
            handlers.forEach((bean, method) -> {
                try {
                    if (!method.canAccess(bean)) {
                        method.setAccessible(true);  // Only set accessible if necessary
                    }
                    method.invoke(bean, event);
                    logger.debug("Successfully invoked method: {} on bean: {}",
                            method.getName(),
                            bean.getClass().getSimpleName()
                    );
                } catch (Exception e) {
                    logger.error(
                    "Failed to invoke event handler method: {} on bean: {}, due to error: {}",
                            method.getName(),
                            bean.getClass().getSimpleName(),
                            e.getMessage(),
                            e
                    );
                    // Optionally, rethrow or handle the exception based on your application needs
                }
            });
        } else {
            logger.warn("No event handlers registered for event type: {}", eventType);
        }
    }
}
