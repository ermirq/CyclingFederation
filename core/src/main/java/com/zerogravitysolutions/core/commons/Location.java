package com.zerogravitysolutions.core.commons;

import jakarta.validation.constraints.NotNull;

@NotNull
public class Location {

    private String country;
    private String city;
    private String postCode;

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(final String postCode) {
        this.postCode = postCode;
    }
}