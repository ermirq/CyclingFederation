package com.zerogravitysolutions.core.users;

import com.zerogravitysolutions.core.commons.BaseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UserDTO extends BaseDTO {

        private String keycloakId;
        @Email(message = "Email should be valid")
        private String email;
        @NotNull
        private String password;

        @NotNull
        private List<String> roles;

        @NotEmpty(message = "First name is required")
        @Size(min = 2, max = 40, message = "Name must be between 2 and 40 characters")
        private String firstName;

        @NotEmpty(message = "Last name is required")
        @Size(min = 2, max = 40, message = "Lastname must be between 2 and 40 characters")
        private String lastName;

        public String getKeycloakId() {
                return keycloakId;
        }

        public void setKeycloakId(final String keycloakId) {
                this.keycloakId = keycloakId;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(final String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(final String password) {
                this.password = password;
        }

        public List<String> getRoles() {
                return roles;
        }

        public void setRoles(final List<String> roles) {
                this.roles = roles;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(final String firstName) {
                this.firstName = firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(final String lastName) {
                this.lastName = lastName;
        }
}


