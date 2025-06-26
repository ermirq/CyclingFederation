package com.zerogravitysolutions.core.keycloak;

import com.zerogravitysolutions.core.configs.KeycloakFeignConfig;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "keycloakFeignClient",
        url = "${keycloak.admin.auth-server-url}",
        configuration = KeycloakFeignConfig.class
)
public interface KeycloakFeignClient {

    @PostMapping("/admin/realms/${keycloak.admin.realm}/users")
    ResponseEntity<Void> createUser(@RequestBody KeycloakUserRequest request);

    @GetMapping("/admin/realms/{realm}/clients/{clientId}/roles")
    List<KeycloakRoleRecord> getClientRoles(
            @PathVariable("realm") String realm,
            @PathVariable("clientId") String clientId
    );

    @GetMapping("/admin/realms/{realm}/users/{userId}/role-mappings/clients/{clientId}")
    @Headers("Content-Type: application/json")
    List<KeycloakRoleRecord> getUserRoles(
            @PathVariable("realm") String realm,
            @PathVariable("userId") String userId,
            @PathVariable("clientId") String clientId
    );

    @DeleteMapping("/admin/realms/{realm}/users/{userId}/role-mappings/clients/{clientId}")
    void removeRoles(
            @PathVariable("realm") String realm,
            @PathVariable("userId") String userId,
            @PathVariable("clientId") String clientId,
            List<KeycloakRoleRecord> rolesToRemove
    );

    @PostMapping("/admin/realms/{realm}/users/{userId}/role-mappings/clients/{clientId}")
    void assignRole(@PathVariable("realm") String realm,
                    @PathVariable("userId") String userId,
                    @PathVariable("clientId") String clientId,
                    @RequestBody List<KeycloakRoleRecord> roles);

    @PutMapping("/admin/realms/{realm}/users/{userId}")
    ResponseEntity<String> updateUser(
            @PathVariable("realm") String realm,
            @PathVariable("userId") String userId,
            @RequestBody KeycloakUserRequest userRequest
    );

    @DeleteMapping("/admin/realms/{realm}/users/{id}")
    void deactivateUser(
            @PathVariable("realm") String realm,
            @PathVariable("id") String id,
            @RequestBody Map<String, Object> updates
    );

    @PostMapping(
            value = "/realms/{realm}/protocol/openid-connect/token",
            consumes = "application/x-www-form-urlencoded"
    )
    ResponseEntity<Map<String, Object>> authenticateUser(
            @PathVariable("realm") String realm,
            @RequestBody Map<String, ?> requestParam
    );

}