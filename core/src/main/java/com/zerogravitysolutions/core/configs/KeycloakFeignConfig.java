package com.zerogravitysolutions.core.configs;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakFeignConfig {

    private final Keycloak keycloak;

    public KeycloakFeignConfig(@Qualifier("keycloakAdmin") final Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Bean
    public RequestInterceptor keycloakRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(final RequestTemplate template) {
                final String accessToken = keycloak.tokenManager().getAccessToken().getToken();
                template.header("Authorization", "Bearer " + accessToken);
            }
        };
    }
}
