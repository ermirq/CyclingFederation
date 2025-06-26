package com.zerogravitysolutions.core.utilities;

import org.springframework.stereotype.Component;

@Component
public class UserContext {

    private String authToken;
    private String userId;
    private String userEmail;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(final String authToken) {
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(final String userEmail) {
        this.userEmail = userEmail;
    }
}
