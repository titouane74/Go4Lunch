package com.fleb.go4lunch.model;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;

/**
 * Created by Florence LE BOURNOT on 25/10/2020
 */
public class Choice {

    private String chChoiceId;
    private String chWorkmateId;
    private String chWorkmateName;
    private String chRestoPlaceId;
    private String chRestoName;
    private Timestamp chCreateDate;
    private Timestamp chChoiceDate;

    public Choice () {}

    public Choice(String pChChoiceId, String pChWorkmateId, @Nullable String pChWorkmateName, String pChRestoPlaceId,
                  @Nullable String pChRestoName, Timestamp pChCreateDate, Timestamp pChChoiceDate) {
        chChoiceId =pChChoiceId;
        chWorkmateId = pChWorkmateId;
        chWorkmateName = pChWorkmateName;
        chRestoPlaceId = pChRestoPlaceId;
        chRestoName = pChRestoName;
        chCreateDate = pChCreateDate;
        chChoiceDate = pChChoiceDate;
    }

    public String getChChoiceId() { return chChoiceId; }

    public void setChChoiceId(String pChChoiceId) { chChoiceId = pChChoiceId; }

    public String getChWorkmateId() { return chWorkmateId; }

    public void setChWorkmateId(String pChWorkmateId) { chWorkmateId = pChWorkmateId; }

    public String getChWorkmateName() { return chWorkmateName; }

    public void setChWorkmateName(String pChWorkmateName) { chWorkmateName = pChWorkmateName; }

    public void setChRestoPlaceId(String pChRestoPlaceId) { chRestoPlaceId = pChRestoPlaceId; }

    public String getChRestoName() { return chRestoName; }

    public String getChRestoPlaceId() { return chRestoPlaceId; }

    public void setChRestoName(String pChRestoName) { chRestoName = pChRestoName; }

    public Timestamp getChCreateDate() { return chCreateDate; }

    public void setChCreateDate(Timestamp pChCreateDate) { chCreateDate = pChCreateDate; }

    public Timestamp getChChoiceDate() { return chChoiceDate; }

    public void setChChoiceDate(Timestamp pChChoiceDate) { chChoiceDate = pChChoiceDate; }

    public enum Fields {
        Choice,
        chChoiceId,
        chWorkmateId,
        chWorkmateName,
        chRestoPlaceId,
        chRestoName,
        chCreateDate,
        chChoiceDate;
    }
}
