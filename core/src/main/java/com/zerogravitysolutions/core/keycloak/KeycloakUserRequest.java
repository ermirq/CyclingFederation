package com.zerogravitysolutions.core.keycloak;

import java.util.List;

public record KeycloakUserRequest(
        String username,
        String email,
        boolean enabled,
        List<Credential> credentials,
        String firstName,
        String lastName
) {
    public record Credential(
            String type,
            String value,
            boolean temporary
    ) {}
}
