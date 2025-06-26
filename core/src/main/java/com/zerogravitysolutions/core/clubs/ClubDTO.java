package com.zerogravitysolutions.core.clubs;

import com.zerogravitysolutions.core.commons.BaseDTO;
import com.zerogravitysolutions.core.commons.Location;
import com.zerogravitysolutions.core.cyclists.CyclistDTO;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClubDTO extends BaseDTO {

    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 40, message = "Name must be between 2 and 40 characters")
    private String name;
    @Email(message = "Email should be valid")
    private String email;
    @NotEmpty(message = "Phone number is required")
    private String phone;
    @NotNull(message = "Club size is required")
    private Integer clubSize;
    @NotNull(message = "Location is required")
    private Location location;
    @PastOrPresent(message = "Registration date cannot be in future")
    private Date registrationDate;
    private List<CyclistDTO> cyclists = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public Integer getClubSize() {
        return clubSize;
    }

    public void setClubSize(final Integer clubSize) {
        this.clubSize = clubSize;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(final Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<CyclistDTO> getCyclists() {
        return cyclists;
    }

    public void setCyclists(final List<CyclistDTO> cyclists) {
        this.cyclists = cyclists;
    }
}
