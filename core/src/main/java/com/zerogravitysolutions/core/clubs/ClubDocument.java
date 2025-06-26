package com.zerogravitysolutions.core.clubs;

import com.zerogravitysolutions.core.commons.BaseDocument;
import com.zerogravitysolutions.core.commons.Location;
import com.zerogravitysolutions.core.cyclists.CyclistDocument;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "club")
public class ClubDocument extends BaseDocument {
    private String name;
    private String email;
    private String phone;
    private Integer clubSize;
    private Location location;
    private Date registrationDate;

    @DBRef
    private List<CyclistDocument> cyclists = new ArrayList<>();

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

    public List<CyclistDocument> getCyclists() {
        return cyclists;
    }

    public void setCyclists(final List<CyclistDocument> cyclists) {
        this.cyclists = cyclists;
    }

    public ClubDocument deepCopy (){
        final ClubDocument copy = new ClubDocument();

        copy.setId(this.getId());
        copy.setName(this.getName());
        copy.setEmail(this.getEmail());
        copy.setPhone(this.getPhone());
        copy.setClubSize(this.getClubSize());
        copy.setLocation(this.getLocation());
        copy.setRegistrationDate(this.getRegistrationDate());
        copy.setCyclists(this.getCyclists());

        return copy;
    }
}