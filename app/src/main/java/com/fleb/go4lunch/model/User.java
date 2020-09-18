package com.fleb.go4lunch.model;

import java.util.Objects;

/**
 * Created by Florence LE BOURNOT on 16/09/2020
 */
public class User {

    private String userName;
    private String userEmail;
    private String userPhotoUrl;

    public User(String pUserName, String pUserEmail, String pUserPhotoUrl) {
        this.userName = pUserName;
        this.userEmail = pUserEmail;
        this.userPhotoUrl = pUserPhotoUrl;
    }

    public User() {}

    public String getUserName() { return userName; }

    public void setUserName(String pUserName) { userName = pUserName; }

    public String getUserEmail() { return userEmail; }

    public void setUserEmail(String pUserEmail) { userEmail = pUserEmail; }

    public String getUserPhotoUrl() { return userPhotoUrl; }

    public void setUserPhotoUrl(String pUserPhotoUrl) { userPhotoUrl = pUserPhotoUrl; }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) return true;
        if (pObj == null || getClass() != pObj.getClass()) return false;
        return Objects.equals(this.userEmail, ((User) pObj).getUserEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserEmail());
    }
}
