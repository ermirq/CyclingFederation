package com.zerogravitysolutions.core.users;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody final UserDTO userDto) {
        final UserDTO created = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable final String id) {
        final UserDTO userFound = userService.findById(id);
        return ResponseEntity.ok(userFound);
    }

    @GetMapping(path = "/users", params = "email")
    public ResponseEntity<UserDTO> findByEmail(@RequestParam final String email) {
        final UserDTO userFound = userService.findByEmail(email);
        return ResponseEntity.ok(userFound);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> update(
            @PathVariable final String id,
            @Valid @RequestBody final UserUpdateRecord record
    ) {

        if (id == null || record == null) {
            return ResponseEntity.notFound().build();
        }

        final UserDTO updatedUser = userService.update(id, record);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/users/{id}/roles")
    public ResponseEntity<UserDTO> updateUserRoles(
            @PathVariable final String id,
            @RequestBody final List<String> newRoles
    ) {
        final UserDTO userRolesUpdated = userService.updateUserRoles(id, newRoles);
        return ResponseEntity.ok(userRolesUpdated);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable final String id) {
        userService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/auth/login", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam("email") final String email,
            @RequestParam("password") final String password
    ) {

        if (email == null || password == null) {
            return new ResponseEntity<>(
                    Map.of("error", "Username and password must be provided"),
                    HttpStatus.BAD_REQUEST
            );
        }

        final Map<String, Object> response = userService.authWithKeycloak(email, password);

        return ResponseEntity.ok(response);
    }
}
