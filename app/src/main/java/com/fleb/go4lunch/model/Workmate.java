package com.fleb.go4lunch.model;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentId;

import java.util.Objects;

/**
 * Created by Florence LE BOURNOT on 16/09/2020
 */
public class Workmate {


    @DocumentId
    private String workmateId;
    private String workmateName;
    private String workmateEmail;
    @Nullable
    private String workmatePhotoUrl;

    public Workmate(String pWorkmateId, String pWorkmateName, String pWorkmateEmail, @Nullable String pWorkmatePhotoUrl) {
        this.workmateId = pWorkmateId;
        this.workmateName = pWorkmateName;
        this.workmateEmail = pWorkmateEmail;
        this.workmatePhotoUrl = pWorkmatePhotoUrl;
    }

    public Workmate() {}

    public String getWorkmateId() { return workmateId; }

    public void setWorkmateId(String pWorkmateId) { workmateId = pWorkmateId; }

    public String getWorkmateName() { return workmateName; }

    public void setWorkmateName(String pWorkmateName) { workmateName = pWorkmateName; }

    public String getWorkmateEmail() { return workmateEmail; }

    public void setWorkmateEmail(String pWorkmateEmail) { workmateEmail = pWorkmateEmail; }

    @Nullable
    public String getWorkmatePhotoUrl() { return workmatePhotoUrl; }

    public void setWorkmatePhotoUrl(@Nullable String pWorkmatePhotoUrl) { workmatePhotoUrl = pWorkmatePhotoUrl; }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) return true;
        if (pObj == null || getClass() != pObj.getClass()) return false;
        return Objects.equals(this.workmateEmail, ((Workmate) pObj).getWorkmateEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWorkmateEmail());
    }
}
