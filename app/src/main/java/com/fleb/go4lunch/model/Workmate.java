package com.fleb.go4lunch.model;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;

import java.util.List;
import java.util.Objects;

/**
 * Created by Florence LE BOURNOT on 16/09/2020
 *
 * Model of a workmate
 *
 */
public class Workmate {

    private String workmateId;
    private String workmateName;
    private String workmateEmail;
    private String workmatePhotoUrl;
    private WorkmateRestoChoice workmateRestoChosen;
    private List<Likes> workmateLikes;

    public Workmate() {}

    public Workmate(String pWorkmateId, List<Likes> pWorkmateLikes) {
        this.workmateId = pWorkmateId;
        this.workmateLikes = pWorkmateLikes;
    }

    public Workmate(String pWorkmateId, String pWorkmateName) {
        workmateId = pWorkmateId;
        workmateName = pWorkmateName;
    }


    public Workmate(String pWorkmateId,String pWorkmateName, String pWorkmateEmail, String pWorkmatePhotoUrl) {
        this.workmateId = pWorkmateId;
        this.workmateName = pWorkmateName;
        this.workmateEmail = pWorkmateEmail;
        this.workmatePhotoUrl = pWorkmatePhotoUrl;
    }

    public Workmate(String pWorkmateId, String pWorkmateName, String pWorkmateEmail, String pWorkmatePhotoUrl,
                    WorkmateRestoChoice pWorkmateRestoChosen) {
        this.workmateId = pWorkmateId;
        this.workmateName = pWorkmateName;
        this.workmateEmail = pWorkmateEmail;
        this.workmatePhotoUrl = pWorkmatePhotoUrl;
        this.workmateRestoChosen = pWorkmateRestoChosen;
    }

    public Workmate(String pWorkmateId, String pWorkmateName, String pWorkmateEmail, String pWorkmatePhotoUrl,
                    WorkmateRestoChoice pWorkmateRestoChosen, List<Likes> pWorkmateLikes) {
        workmateId = pWorkmateId;
        workmateName = pWorkmateName;
        workmateEmail = pWorkmateEmail;
        workmatePhotoUrl = pWorkmatePhotoUrl;
        workmateRestoChosen = pWorkmateRestoChosen;
        workmateLikes = pWorkmateLikes;
    }

    public String getWorkmateName() { return workmateName; }

    public void setWorkmateName(String pWorkmateName) { workmateName = pWorkmateName; }

    public String getWorkmateEmail() { return workmateEmail; }

    public void setWorkmateEmail(String pWorkmateEmail) { workmateEmail = pWorkmateEmail; }

    public String getWorkmatePhotoUrl() { return workmatePhotoUrl; }

    public void setWorkmatePhotoUrl(@Nullable String pWorkmatePhotoUrl) { workmatePhotoUrl = pWorkmatePhotoUrl; }

    public WorkmateRestoChoice getWorkmateRestoChosen() { return workmateRestoChosen; }

    public void setWorkmateRestoChosen(WorkmateRestoChoice pWorkmateRestoChosen) { workmateRestoChosen = pWorkmateRestoChosen; }

    public String getWorkmateId() { return workmateId; }

    public void setWorkmateId(String pWorkmateId) { workmateId = pWorkmateId; }

    public List<Likes> getWorkmateLikes() { return workmateLikes; }

    public void setWorkmateLikes(List<Likes> pWorkmateLikes) { workmateLikes = pWorkmateLikes; }

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

    public static class WorkmateRestoChoice {
        public String restoId;
        public String restoName;
        public Timestamp restoDateChoice;

        public WorkmateRestoChoice() {}

        public WorkmateRestoChoice(String pRestoId, String pRestoName, Timestamp pRestoDateChoice) {
            restoId = pRestoId;
            restoName = pRestoName;
            restoDateChoice = pRestoDateChoice;
        }

        public String getRestoId() {
            return restoId;
        }

        public void setRestoId(String pRestoId) {
            restoId = pRestoId;
        }

        public String getRestoName() {
            return restoName;
        }

        public void setRestoName(String pRestoName) {
            restoName = pRestoName;
        }

        public Timestamp getRestoDateChoice() {
            return restoDateChoice;
        }

        public void setRestoDateChoice(Timestamp pRestoDateChoice) {
            restoDateChoice = pRestoDateChoice;
        }
    }

    public enum Fields {
        Workmate,
        workmateId,
        workmateName,
        workmateEmail,
        workmatePhotoUrl,
        workmateRestoChosen,
        workmateLikes
    }

}
