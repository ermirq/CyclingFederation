package com.zerogravitysolutions.core.users.events;

import com.zerogravitysolutions.core.configs.KeycloakAdminProperties;
import com.zerogravitysolutions.core.configs.KeycloakProperties;
import com.zerogravitysolutions.core.events.CustomEventListener;
import com.zerogravitysolutions.core.events.EventType;
import com.zerogravitysolutions.core.keycloak.KeycloakFeignClient;
import com.zerogravitysolutions.core.keycloak.KeycloakRoleRecord;
import com.zerogravitysolutions.core.keycloak.KeycloakUserRequest;
import com.zerogravitysolutions.core.users.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserEventListener {

    private static final Logger logger = LoggerFactory.getLogger(UserEventListener.class);
    private final KeycloakFeignClient keycloakFeignClient;
    private final ApplicationEventPublisher eventPublisher;
    private final KeycloakProperties keycloakProperties;
    private final KeycloakAdminProperties keycloakAdminProperties;

    public UserEventListener(
            @Lazy final KeycloakFeignClient keycloakFeignClient,
            final ApplicationEventPublisher eventPublisher,
            final KeycloakProperties keycloakProperties,
            final KeycloakAdminProperties keycloakAdminProperties
    ) {
        this.keycloakFeignClient = keycloakFeignClient;
        this.eventPublisher = eventPublisher;
        this.keycloakProperties = keycloakProperties;
        this.keycloakAdminProperties = keycloakAdminProperties;
    }

    @CustomEventListener(EventType.USER_CREATED)
    public void handleUserCreated(final UserEvent event) {
        final UserDTO userDto = event.userDto();
        final List<String> roles = event.roles();
        logger.info("Handling user created event: {}", userDto.getEmail());

        try {
            final String keycloakUserId = createUserInKeycloak(userDto);
            assignRolesToUserInKeycloak(keycloakUserId, roles);
            updateUserInMongo(userDto, keycloakUserId);
            publishKeycloakUserCreatedEvent(userDto);
        } catch (Exception ex) {
            logger.error("Failed to handle user created event for: {}", userDto.getEmail(), ex);
        }
    }

    private String createUserInKeycloak(final UserDTO userDto) throws Exception {
        final KeycloakUserRequest keycloakRequest = new KeycloakUserRequest(
                userDto.getEmail(),
                userDto.getEmail(),
                true,
                List.of(new KeycloakUserRequest.Credential("password", "123", true)),
                userDto.getFirstName(),
                userDto.getLastName()
        );

        final ResponseEntity<Void> response = keycloakFeignClient.createUser(keycloakRequest);
        logger.info("User created in Keycloak for email: {}", userDto.getEmail());

        final String locationHeader = response.getHeaders().getLocation().toString();
        if (locationHeader == null) {
            logger.error(
                    "Failed to get the location header for " +
                            "the created user in Keycloak for email: {}",
                    userDto.getEmail()
            );
            throw new RuntimeException(
                    "Failed to get the location header for the created user in Keycloak"
            );
        }

        logger.debug("Location header: {}", locationHeader);
        return extractUserIdFromLocationHeader(locationHeader);
    }

    private void assignRolesToUserInKeycloak(
            final String keycloakUserId,
            final List<String> roles
    ) {
        final List<KeycloakRoleRecord> clientRoles = keycloakFeignClient.getClientRoles(
                keycloakProperties.getRealm(),
                keycloakProperties.getClientId()
        );

        final List<KeycloakRoleRecord> rolesToAssign = roles.stream()
                .map(role -> clientRoles.stream()
                        .filter(clientRole -> clientRole.name().equals(role))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Role not found: " + role)))
                .collect(Collectors.toList());

        keycloakFeignClient.assignRole(
                keycloakProperties.getRealm(),
                keycloakUserId,
                keycloakProperties.getClientId(),
                rolesToAssign
        );
        logger.info("Assigned roles to user in Keycloak for user ID: {}", keycloakUserId);
    }

    private void updateUserInMongo(final UserDTO userDto, final String keycloakUserId) {
        userDto.setKeycloakId(keycloakUserId);
        logger.info("Keycloak ID set and user document updated in MongoDB: {}", keycloakUserId);
    }

    private void publishKeycloakUserCreatedEvent(final UserDTO userDto) {
        logger.info("Publishing event: KEYCLOAK_USER_CREATED for user: {}", userDto.getEmail());
        eventPublisher.publishEvent(new UserEvent(EventType.KEYCLOAK_USER_CREATED, userDto));
        logger.info("Event published: KEYCLOAK_USER_CREATED for user: {}", userDto.getEmail());
    }

    private String extractUserIdFromLocationHeader(final String locationHeader) {
        final String[] parts = locationHeader.split("/");
        return parts[parts.length - 1];
    }

    @CustomEventListener(EventType.USER_UPDATED)
    public void handleUserUpdated(final UserEvent event) {

        final UserDTO userDto = event.userDto();
        logger.info("Handling user updated event: {}", userDto.getEmail());

        final KeycloakUserRequest keycloakRequest = new KeycloakUserRequest(
                userDto.getEmail(),
                userDto.getEmail(),
                true,
                List.of(new KeycloakUserRequest.Credential("password", "123", true)),
                userDto.getFirstName(),
                userDto.getLastName()
        );

        try {
            logger.info(
                    "Updating user information in Keycloak for user with ID: {}",
                    userDto.getKeycloakId()
            );
            keycloakFeignClient.updateUser(
                    keycloakProperties.getRealm(),
                    userDto.getKeycloakId(),
                    keycloakRequest
            );
            logger.info(
                    "User information updated in Keycloak for user with ID: {}",
                    userDto.getKeycloakId()
            );
        } catch (Exception ex) {
            logger.error(
                    "Failed to update the user in Keycloak for ID: {}",
                    userDto.getKeycloakId(), ex
            );
        }
    }

    @CustomEventListener(EventType.USER_ROLES_UPDATED)
    public void handleUserRolesUpdated(final UserEvent event) {

        final UserDTO userDto = event.userDto();
        final List<String> newRoles = event.roles();

        logger.info("Handling user roles updated event: {}", userDto.getEmail());

        logger.info(
                "Retrieving client roles from Keycloak for realm: {}, client ID: {}",
                keycloakProperties.getRealm(), keycloakProperties.getClientId()
        );
        final List<KeycloakRoleRecord> clientRoles =
                keycloakFeignClient.getClientRoles(
                        keycloakProperties.getRealm(),
                        keycloakProperties.getClientId()
                );

        final List<String> currentRoles = userDto.getRoles();

        final List<String> rolesToAdd = newRoles.stream()
                .filter(role -> !currentRoles.contains(role))
                .collect(Collectors.toList());

        final List<String> rolesToRemove = currentRoles.stream()
                .filter(role -> !newRoles.contains(role))
                .collect(Collectors.toList());

        logger.debug("Roles to add: {}", rolesToAdd);
        logger.debug("Roles to remove: {}", rolesToRemove);

        final List<KeycloakRoleRecord> rolesToAddRecords = clientRoles.stream()
                .filter(role -> rolesToAdd.contains(role.name()))
                .collect(Collectors.toList());

        if(!rolesToRemove.isEmpty()){
            final List<KeycloakRoleRecord> rolesToRemoveRecords = clientRoles.stream()
                    .filter(role -> rolesToRemove.contains(role.name()))
                    .collect(Collectors.toList());

            logger.info(
                    "Removing roles from Keycloak for user with ID: {}: {}",
                    userDto.getKeycloakId(), rolesToRemove
            );
            keycloakFeignClient.removeRoles(
                    keycloakProperties.getRealm(),
                    userDto.getKeycloakId(),
                    keycloakProperties.getClientId(),
                    rolesToRemoveRecords
            );
            logger.info(
                    "Removed roles from Keycloak for user with ID: {}",
                    userDto.getKeycloakId()
            );
        }

        if (!rolesToAddRecords.isEmpty()) {
            logger.info(
                    "Adding roles to Keycloak for user with ID: {}: {}",
                    userDto.getKeycloakId(), rolesToAdd
            );
            keycloakFeignClient.assignRole(
                    keycloakProperties.getRealm(),
                    userDto.getKeycloakId(),
                    keycloakProperties.getClientId(),
                    rolesToAddRecords
            );
            logger.info("Added roles to Keycloak for user with ID: {}", userDto.getKeycloakId());
        }
    }

    @CustomEventListener(EventType.USER_DELETED)
    public void handleUserDeleted(final UserEvent event) {
        final UserDTO userDto = event.userDto();
        logger.info("Handling user deleted event for email: {}", userDto.getEmail());

        final String keycloakUserId = userDto.getKeycloakId();

        try {
            final Map<String, Object> updates = new HashMap<>();
            updates.put("enabled", false);

            keycloakFeignClient.deactivateUser(
                    keycloakProperties.getRealm(),
                    keycloakUserId,
                    updates
            );
            logger.info("User deactivated in Keycloak for user ID: {}", keycloakUserId);

        } catch (Exception ex) {
            logger.error("Failed to handle user deleted event for: {}", userDto.getEmail(), ex);
        }
    }
}