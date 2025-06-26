package com.zerogravitysolutions.core.users;

import com.zerogravitysolutions.core.configs.KeycloakProperties;
import com.zerogravitysolutions.core.events.CustomEventListener;
import com.zerogravitysolutions.core.events.EventType;
import com.zerogravitysolutions.core.keycloak.KeycloakFeignClient;
import com.zerogravitysolutions.core.users.commons.UserMapper;
import com.zerogravitysolutions.core.users.events.UserEvent;
import com.zerogravitysolutions.core.users.exceptions.UserDeletionException;
import com.zerogravitysolutions.core.users.exceptions.UserNotFoundException;
import com.zerogravitysolutions.core.users.exceptions.UserUpdateException;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserMapper userMapper;
    private final KeycloakFeignClient keycloakFeignClient;
    private final KeycloakProperties keycloakProperties;

    public UserServiceImpl(
            final UserRepository userRepository,
            final ApplicationEventPublisher applicationEventPublisher,
            final UserMapper userMapper,
            final KeycloakFeignClient keycloakFeignClient,
            final KeycloakProperties keycloakProperties
    ) {
        this.userRepository = userRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.userMapper = userMapper;
        this.keycloakFeignClient = keycloakFeignClient;
        this.keycloakProperties = keycloakProperties;
    }


    @Override
    public UserDTO registerUser(final UserDTO userDto) {
        logger.info("Registering new user with email: {}", userDto.getEmail());
        final UserDocument userDocument = userMapper.toUserDocument(userDto);

        userDocument.setRoles(userDto.getRoles());
        userDocument.setFirstName(userDto.getFirstName());
        userDocument.setLastName(userDto.getLastName());
        userDocument.setEmail(userDto.getEmail());
        try {

            userRepository.save(userDocument);
            logger.info("User document saved in MongoDB for user ID: {}", userDocument.getId());

            logger.info("Publishing event: USER_CREATED for user: {}", userDto.getEmail());
            applicationEventPublisher.publishEvent(
                    new UserEvent(
                            EventType.USER_CREATED,
                            userMapper.toUserDTO(userDocument),
                            userDto.getRoles()
                    )
            );
            logger.info("Event published: USER_CREATED for user: {}", userDto.getEmail());

            return userMapper.toUserDTO(userDocument);
        } catch (Exception e) {
            logger.error(
                    "Error occurred while registering user with email {}:",
                    userDto.getEmail(), e)
            ;
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User with this email already exists"
            );
        }
    }

    @Override
    public UserDTO findById(final String id) {
        logger.info("Fetching user with ID {}", id);
        final UserDocument user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with this ID " + id + " is not found")
        );
        logger.info("Fetched user: {}", user);
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO findByEmail(final String email) {

        logger.info("Finding user by email: {}", email);
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        final UserDocument userDocument = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found with email: " + email)
                );

        return userMapper.toUserDTO(userDocument);
    }

    @Override
    public UserDTO update(final String id, final UserUpdateRecord record) {
        logger.info("Attempting to update user with keycloak ID: {}", id);

        final Optional<UserDocument> optionalUserDocument = userRepository.findByKeycloakId(id);
        if (optionalUserDocument.isEmpty()) {
            logger.error("User not found with keycloak ID: {}", id);
            throw new UserNotFoundException("User not found with this id");
        }

        final UserDocument userDocument = optionalUserDocument.get();

        if (userDocument.getDeletedAt() != null) {
            logger.error("Attempted to update an already deleted user with ID: {}", id);
            throw new UserUpdateException("You are trying to update a deleted user");
        }

        logger.info("Updating user document in MongoDB for user with keycloak ID: {}", id);
        userDocument.setEmail(record.email());
        userDocument.setFirstName(record.firstName());
        userDocument.setLastName(record.lastName());
        userRepository.save(userDocument);
        logger.info("User document updated in MongoDB for user with keycloak ID: {}", id);

        logger.info("Publishing event: USER_UPDATED for user: {}", userDocument.getEmail());
        applicationEventPublisher.publishEvent(
                new UserEvent(
                        EventType.USER_UPDATED,
                        userMapper.toUserDTO(userDocument),
                        null
                )
        );
        logger.info("Event published: USER_UPDATED for user: {}", userDocument.getEmail());

        return userMapper.toUserDTO(userDocument);
    }


    @Override
    public void softDelete(final String id) {
        logger.info("Deleting user with ID: {}", id);
        final UserDocument user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with this id" + id + " is not found")
        );

        if (user.getDeletedAt() != null) {
            logger.error("Attempted to delete an already deleted user with ID: {}", id);
            throw new UserDeletionException("This user has already been deleted!");
        }

        user.setDeletedAt(LocalDateTime.now());

        logger.info("Publishing event: USER_DELETED");
        applicationEventPublisher.publishEvent(
                new UserEvent(
                        EventType.USER_DELETED,
                        userMapper.toUserDTO(user)
                )
        );
        logger.info("Event published: USER_DELETED");

        userRepository.save(user);
        logger.info("User soft deleted successfully: {}", user);
    }

    @Override
    public UserDTO updateUserRoles(final String id, final List<String> newRoles) {
        logger.info("Attempting to update roles for user with keycloak ID: {}", id);

        final Optional<UserDocument> optionalUserDocument = userRepository.findByKeycloakId(id);
        if (optionalUserDocument.isEmpty()) {
            logger.error("User not found with keycloak ID: {}", id);
            throw new UserNotFoundException("User not found with this id");
        }

        final UserDocument userDocument = optionalUserDocument.get();

        if (userDocument.getDeletedAt() != null) {
            logger.error("Attempted to update roles for an already deleted user with ID: {}", id);
            throw new UserUpdateException("You are trying to update roles for a deleted user");
        }

        logger.info("Publishing event: USER_ROLES_UPDATED for user: {}", userDocument.getEmail());
        applicationEventPublisher.publishEvent(
                new UserEvent(EventType.USER_ROLES_UPDATED,
                        userMapper.toUserDTO(userDocument),
                        newRoles
                )
        );
        logger.info("Event published: USER_ROLES_UPDATED for user: {}", userDocument.getEmail());

        logger.info("Updating user roles in MongoDB for user with ID: {}", id);
        userDocument.setRoles(newRoles);
        userRepository.save(userDocument);
        logger.info("User roles updated in MongoDB for user with ID: {}", id);

        return userMapper.toUserDTO(userDocument);
    }

    public Map<String,Object> authWithKeycloak(final String username, final String password){
        logger.info("Attempting login for user with email: {}", username);

        final String realm = keycloakProperties.getRealm();

        final Map<String, String> formParams = new HashMap<>();
        formParams.put("username", username);
        formParams.put("password", password);
        formParams.put("client_id", keycloakProperties.getClientName());
        formParams.put("client_secret", keycloakProperties.getClientSecret());
        formParams.put("grant_type", keycloakProperties.getGrantType());

        try {
            final ResponseEntity<Map<String, Object>> response =
                    keycloakFeignClient.authenticateUser(realm, formParams);
            return handleKeycloakResponse(username, response);
        } catch (FeignException fe) {
            handleFeignException(username, fe);
            return null;
        } catch (Exception e) {
            handleGenericException(username, e);
            return null;
        }
    }

    private Map<String, Object> handleKeycloakResponse(
            final String username,
            final ResponseEntity<Map<String, Object>> response
    ) {
        final HttpStatus statusCode = HttpStatus.valueOf(response.getStatusCode().value());
        if (statusCode.is2xxSuccessful()) {
            logger.info("User with email {} logged in successfully", username);
            return response.getBody();
        } else {
            final String errorDescription = response.getBody()
                    .getOrDefault("error_description", "Invalid credentials")
                    .toString();
            logger.error(
                    "Failed login for user with email {}. Status code: {}. Error: {}",
                    username, statusCode, errorDescription
            );
            throw new ResponseStatusException(
                    statusCode,
                    "Authentication failed: " + errorDescription
            );
        }
    }

    private void handleFeignException(final String username, final FeignException fe) {
        final HttpStatus statusCode = HttpStatus.valueOf(fe.status());
        final String errorMessage = fe.getMessage() != null
                ? fe.getMessage()
                : "Authentication failed";
        logger.error(
                "FeignException during login for user with email {}: Status {}, Message: {}",
                username, statusCode, errorMessage
        );
        throw new ResponseStatusException(statusCode, errorMessage, fe);
    }

    private void handleGenericException(final String username, final Exception e) {
        logger.error(
                "Exception during login for user with email {}: {}",
                username, e.getMessage(), e
        );
        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error during authentication", e
        );
    }

    @CustomEventListener(EventType.KEYCLOAK_USER_CREATED)
    public void handleUserCreatedWithKeycloakId(final UserEvent event) {

        final UserDTO userDTO = event.userDto();
        final UserDocument userDocument = userMapper.toUserDocument(userDTO);
        userRepository.save(userDocument);
        logger.info("Keycloak ID set and userDocument updated in database: {}", userDTO.getEmail());
    }
}
