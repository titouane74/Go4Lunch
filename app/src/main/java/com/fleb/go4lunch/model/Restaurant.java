package com.fleb.go4lunch.model;

import com.google.firebase.firestore.DocumentId;

/**
 * Created by Florence LE BOURNOT on 16/09/2020
 */
public class Restaurant {

    @DocumentId
    private String restoId;
    private String restoName;
    private String restoAddress;
    private String restoPhone;
    private String restoWebsite;
    private String restoDistance;
    private int restoNbWorkmates;
    private String restoOpening;
    private double restoNote;
    private String restoPhotoUrl;

    public Restaurant() {}

    public Restaurant(String pRestoId, String pRestoName, String pRestoAddress, String pRestoPhone, String pRestoWebsite,
                      String pRestoDistance, int pRestoNbWorkmates, String pRestoOpening, double pRestoNote, String pRestoPhotoUrl) {
        restoId = pRestoId;
        restoName = pRestoName;
        restoAddress = pRestoAddress;
        restoPhone = pRestoPhone;
        restoWebsite = pRestoWebsite;
        restoDistance = pRestoDistance;
        restoNbWorkmates = pRestoNbWorkmates;
        restoOpening = pRestoOpening;
        restoNote = pRestoNote;
        restoPhotoUrl = pRestoPhotoUrl;
    }

    public String getRestoId() { return restoId; }

    public void setRestoId(String pRestoId) { restoId = pRestoId; }

    public String getRestoName() { return restoName; }

    public void setRestoName(String pRestoName) { restoName = pRestoName; }

    public String getRestoAddress() { return restoAddress; }

    public void setRestoAddress(String pRestoAddress) { restoAddress = pRestoAddress; }

    public String getRestoPhone() { return restoPhone; }

    public void setRestoPhone(String pRestoPhone) { restoPhone = pRestoPhone; }

    public String getRestoWebsite() { return restoWebsite; }

    public void setRestoWebsite(String pRestoWebsite) { restoWebsite = pRestoWebsite; }

    public String getRestoDistance() { return restoDistance; }

    public void setRestoDistance(String pRestoDistance) { restoDistance = pRestoDistance; }

    public int getRestoNbWorkmates() { return restoNbWorkmates; }

    public void setRestoNbWorkmates(int pRestoNbWorkmates) { restoNbWorkmates = pRestoNbWorkmates; }

    public String getRestoOpening() { return restoOpening; }

    public void setRestoOpening(String pRestoOpening) { restoOpening = pRestoOpening; }

    public double getRestoNote() { return restoNote; }

    public void setRestoNote(int pRestoNote) { restoNote = pRestoNote; }

    public String getRestoPhotoUrl() { return restoPhotoUrl; }

    public void setRestoPhotoUrl(String pRestoPhotoUrl) { restoPhotoUrl = pRestoPhotoUrl; }
}
