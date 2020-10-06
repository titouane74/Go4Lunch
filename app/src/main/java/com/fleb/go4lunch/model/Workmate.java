package com.fleb.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Created by Florence LE BOURNOT on 16/09/2020
 */
public class Workmate {

    private String workmateName;
    private String workmateEmail;
    private String workmatePhotoUrl;
    private String workmateRestoChoosed;
    private String workmateRestoId;
    private Boolean workmateLike;



    public Workmate( String pWorkmateName, String pWorkmateEmail, String pWorkmatePhotoUrl,
                String pWorkmateRestoChoosed, String pWorkmateRestoId, Boolean pWorkmateLike ) {
        this.workmateName = pWorkmateName;
        this.workmateEmail = pWorkmateEmail;
        this.workmatePhotoUrl = pWorkmatePhotoUrl;
        this.workmateRestoChoosed = pWorkmateRestoChoosed;
        this.workmateRestoId = pWorkmateRestoId;
        this.workmateLike = pWorkmateLike;
    }

    public Workmate( String pWorkmateName, String pWorkmateEmail, String pWorkmatePhotoUrl) {
        this.workmateName = pWorkmateName;
        this.workmateEmail = pWorkmateEmail;
        this.workmatePhotoUrl = pWorkmatePhotoUrl;
    }


    public Workmate() {}

    public String getWorkmateName() { return workmateName; }

    public void setWorkmateName(String pWorkmateName) { workmateName = pWorkmateName; }

    public String getWorkmateEmail() { return workmateEmail; }

    public void setWorkmateEmail(String pWorkmateEmail) { workmateEmail = pWorkmateEmail; }

    public String getWorkmatePhotoUrl() { return workmatePhotoUrl; }

    public void setWorkmatePhotoUrl(@Nullable String pWorkmatePhotoUrl) { workmatePhotoUrl = pWorkmatePhotoUrl; }

    public String getWorkmateRestoChoosed() { return workmateRestoChoosed; }

    public void setWorkmateRestoChoosed(String pWorkmateRestoChoosed) { workmateRestoChoosed = pWorkmateRestoChoosed; }

    public String getWorkmateRestoId() { return workmateRestoId; }

    public void setWorkmateRestoId(String pWorkmateRestoId) { workmateRestoId = pWorkmateRestoId; }

    public Boolean getWorkmateLike() { return workmateLike; }

    public void setWorkmateLike(Boolean pWorkmateLike) { workmateLike = pWorkmateLike; }

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
