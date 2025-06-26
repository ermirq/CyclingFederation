package com.zerogravitysolutions.core.members;

import com.zerogravitysolutions.core.commons.BaseDTO;
import com.zerogravitysolutions.core.commons.Location;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

public class MemberDTO extends BaseDTO {

    @NotEmpty(message = "First name is required")
    @Size(min = 2, max = 40, message = "Name must be between 2 and 40 characters")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Phone number should be added")
    private String phoneNumber;

    @NotEmpty(message = "Address is needed")
    private List<Location> address;

    @NotNull(message = "Age is required")
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
}
