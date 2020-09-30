package com.fleb.go4lunch.model;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentId;

import java.util.Objects;

/**
 * Created by Florence LE BOURNOT on 16/09/2020
 */
public class Workmate {

    private String workmateName;
    private String workmateEmail;
    @Nullable
    private String workmatePhotoUrl;
    private String workmateRestoChoosed;
    private String workmateRestoId;

    public Workmate( String pWorkmateName, String pWorkmateEmail, @Nullable String pWorkmatePhotoUrl,
                String pWorkmateRestoChoosed, String pWorkmateRestoId) {
        this.workmateName = pWorkmateName;
        this.workmateEmail = pWorkmateEmail;
        this.workmatePhotoUrl = pWorkmatePhotoUrl;
        this.workmateRestoChoosed = pWorkmateRestoChoosed;
        this.workmateRestoId = pWorkmateRestoId;
    }

    public Workmate() {}

    public String getWorkmateName() { return workmateName; }

    public void setWorkmateName(String pWorkmateName) { workmateName = pWorkmateName; }

    public String getWorkmateEmail() { return workmateEmail; }

    public void setWorkmateEmail(String pWorkmateEmail) { workmateEmail = pWorkmateEmail; }

    @Nullable
    public String getWorkmatePhotoUrl() { return workmatePhotoUrl; }

    public void setWorkmatePhotoUrl(@Nullable String pWorkmatePhotoUrl) { workmatePhotoUrl = pWorkmatePhotoUrl; }

    public String getWorkmateRestoChoosed() { return workmateRestoChoosed; }

    public void setWorkmateRestoChoosed(String pWorkmateRestoChoosed) { workmateRestoChoosed = pWorkmateRestoChoosed; }

    public String getWorkmateRestoId() { return workmateRestoId; }

    public void setWorkmateRestoId(String pWorkmateRestoId) { workmateRestoId = pWorkmateRestoId; }

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
