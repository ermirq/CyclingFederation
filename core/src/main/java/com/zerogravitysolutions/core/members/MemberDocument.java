package com.zerogravitysolutions.core.members;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zerogravitysolutions.core.commons.BaseDocument;
import com.zerogravitysolutions.core.commons.Location;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "members")
@JsonInclude(JsonInclude.Include.NON_NULL)

public class MemberDocument extends BaseDocument {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<Location> address;
    private Integer age;
    private Boolean isActive;
    private Date joinDate;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(final Boolean active) {
        isActive = active;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Location> getAddress() {
        return address;
    }

    public void setAddress(final List<Location> address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(final Date joinDate) {
        this.joinDate = joinDate;
    }

    public MemberDocument deepCopy() {
        final MemberDocument copy = new MemberDocument();

        copy.setId(this.getId());
        copy.setFirstName(this.getFirstName());
        copy.setLastName(this.getLastName());
        copy.setEmail(this.getEmail());
        copy.setPhoneNumber(this.getPhoneNumber());
        copy.setAddress(this.getAddress());
        copy.setActive(this.getActive());
        copy.setAge(this.getAge());
        copy.setJoinDate(this.getJoinDate());

        return copy;
    }
}
