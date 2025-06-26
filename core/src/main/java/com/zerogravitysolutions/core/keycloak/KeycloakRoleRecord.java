package com.zerogravitysolutions.core.keycloak;

public record KeycloakRoleRecord(
        String id,
        String name,
        String description,
        boolean composite,
        boolean clientRole,
        String containerId
) {}
