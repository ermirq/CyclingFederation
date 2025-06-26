package com.zerogravitysolutions.core.volunteers;

import com.zerogravitysolutions.core.commons.BaseDTO;

public class VolunteersDTO extends BaseDTO {
    private String name;
    private String role;
    private String contactInfo;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(final String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
