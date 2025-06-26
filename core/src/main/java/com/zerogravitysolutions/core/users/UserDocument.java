package com.zerogravitysolutions.core.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zerogravitysolutions.core.commons.BaseDocument;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDocument extends BaseDocument {

    private String keycloakId;
    private List<String> roles;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    @JsonIgnore
    private String password;

    public UserDocument() {
    }

    public UserDocument(
            final String keycloakId,
            final List<String> roles,
            final String firstName,
            final String lastName,
            final String email,
            final String password
    ) {
        this.keycloakId = keycloakId;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(final String keycloakId) {
        this.keycloakId = keycloakId;
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
}
