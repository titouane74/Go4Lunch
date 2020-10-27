package com.fleb.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Created by Florence LE BOURNOT on 16/09/2020
 */
public class Workmate {

    private String workmateId;
    private String workmateName;
    private String workmateEmail;
    private String workmatePhotoUrl;
    private String workmateRestoChoosed;
    private List<Likes> workmateLikes;


    public Workmate(String pWorkmateId, String pWorkmateName, String pWorkmateEmail, String pWorkmatePhotoUrl,
                String pWorkmateRestoChoosed ) {
        this.workmateId = pWorkmateId;
        this.workmateName = pWorkmateName;
        this.workmateEmail = pWorkmateEmail;
        this.workmatePhotoUrl = pWorkmatePhotoUrl;
        this.workmateRestoChoosed = pWorkmateRestoChoosed;
    }

    public Workmate(String pWorkmateId, List<Likes> pWorkmateLikes) {
        workmateId = pWorkmateId;
        workmateLikes = pWorkmateLikes;
    }

    public Workmate(String pWorkmateName, String pWorkmateEmail, String pWorkmatePhotoUrl) {
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

    public String getWorkmateId() { return workmateId; }

    public void setWorkmateId(String pWorkmateId) { workmateId = pWorkmateId; }

    public List<Likes> getWorkmateLikes() { return workmateLikes; }

    public void setWorkmateLikes(List<Likes> pWorkmateLikes) { workmateLikes = pWorkmateLikes; }

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


    public static class Likes {
        public String restoId;
        public String restoName;

        public Likes() {}

        public Likes(String pRestoId, String pRestoName) {
            restoId = pRestoId;
            restoName = pRestoName;
        }

        public String getRestoId() { return restoId; }

        public void setRestoId(String pRestoId) { restoId = pRestoId; }

        public String getRestoName() { return restoName; }

        public void setRestoName(String pRestoName) { restoName = pRestoName; }
    }
    public enum Fields {
        Workmate,
        workmateId,
        workmateName,
        workmateEmail,
        workmatePhotoUrl,
        workmateRestoChoosed,
        workmateLikes;
    }

}
