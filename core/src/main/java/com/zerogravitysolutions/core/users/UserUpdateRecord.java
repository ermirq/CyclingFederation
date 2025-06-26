package com.zerogravitysolutions.core.users;

import java.util.List;

public record UserUpdateRecord(
        String email,
        String password,
        String firstName,
        String lastName,
        List<String> roles
) {
}
