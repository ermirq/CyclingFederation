package com.zerogravitysolutions.core.volunteers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zerogravitysolutions.core.commons.BaseDocument;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "volunteers")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VolunteersDocument extends BaseDocument {
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

    public VolunteersDocument deepCopy (){
        final VolunteersDocument copy = new VolunteersDocument();

        copy.setId(this.getId());
        copy.setName(this.getName());
        copy.setRole(this.getRole());
        copy.setContactInfo(this.getContactInfo());
        copy.setEmail(this.getEmail());

        return copy;
    }
}

