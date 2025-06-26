package com.zerogravitysolutions.core.configs;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakPropertiesConfig {

    private final KeycloakProperties keycloakProperties;
    private final KeycloakAdminProperties keycloakAdminProperties;

    public KeycloakPropertiesConfig(
            final KeycloakProperties keycloakProperties,
            final KeycloakAdminProperties keycloakAdminProperties
    ) {
        this.keycloakProperties = keycloakProperties;
        this.keycloakAdminProperties = keycloakAdminProperties;
    }

    @Bean(name = "keycloak")
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getAuthServerUrl())
                .realm(keycloakProperties.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(keycloakProperties.getClientId())
                .clientSecret(keycloakProperties.getClientSecret())
                .build();
    }

    @Bean(name = "keycloakAdmin")
    public Keycloak keycloakAdmin() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakAdminProperties.getAuthServerUrl())
                .realm(keycloakAdminProperties.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(keycloakAdminProperties.getClientId())
                .clientSecret(keycloakAdminProperties.getClientSecret())
                .build();
    }
}
