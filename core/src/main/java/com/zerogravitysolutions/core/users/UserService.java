package com.zerogravitysolutions.core.users;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserDTO registerUser(UserDTO userDto);


    UserDTO findById(String id);

    UserDTO findByEmail(String email);

    UserDTO update(String id, UserUpdateRecord record);

    void softDelete(String id);

    UserDTO updateUserRoles(String id, List<String> newRoles);

    Map<String, Object> authWithKeycloak(String email, String password);
}
