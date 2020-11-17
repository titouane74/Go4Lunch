package com.fleb.go4lunch.workmate;

import com.fleb.go4lunch.model.Workmate;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Florence LE BOURNOT on 13/11/2020
 */
public class WorkmateTest {

    private Workmate mWorkmate;
    private String mNoId;
    private String mNoName;
    private String mNoEmail;
    private String mNoPhotoUrl;
    private Workmate.WorkmateRestoChoice mWorkmateRestoChosen;
    private List<Workmate.Likes> mWorkmateLikesList = new ArrayList<>();
    private Workmate.Likes mWorkmateLikes;

    @Before
    public void setup() {
        initMocks(this);
        Workmate.Likes lLikes;

        mNoId = "no_id";
        mNoName = "no_name";
        mNoEmail = "no_email";
        mNoPhotoUrl = "no_photo_url";
    }

    @Test
    public void setAndGetWorkmateIdWithSuccess() {
        mWorkmate = new Workmate(mNoId,mNoName);
        assertEquals(mNoId,mWorkmate.getWorkmateId());

        String lSetData = "albert_id";
        mWorkmate.setWorkmateId(lSetData);

        String lGetData = mWorkmate.getWorkmateId();
        assertNotNull(lGetData);
        assertEquals(lSetData,lGetData);
    }

    @Test
    public void setAndGetWorkmateNameWithSuccess() {
        mWorkmate = new Workmate(mNoId,mNoName);
        assertEquals(mNoName, mWorkmate.getWorkmateName());

        String lSetData = "albert";
        mWorkmate.setWorkmateName(lSetData);

        String lGetData = mWorkmate.getWorkmateName();
        assertNotNull(lGetData);
        assertEquals(lSetData, lGetData);

    }

    @Test
    public void setAndGetWorkmateEmailWithSuccess() {
        mWorkmate = new Workmate(mNoId,mNoName, mNoEmail, mNoPhotoUrl);
        assertEquals(mNoEmail, mWorkmate.getWorkmateEmail());

        String lSetData = "albert@email.com";
        mWorkmate.setWorkmateEmail(lSetData);

        String lGetData = mWorkmate.getWorkmateEmail();
        assertNotNull(lGetData);
        assertEquals(lSetData, lGetData);
    }

    @Test
    public void setAndGetWorkmatePhotoUrlWithSuccess() {
        mWorkmate = new Workmate(mNoId,mNoName, mNoEmail, mNoPhotoUrl);
        assertEquals(mNoPhotoUrl, mWorkmate.getWorkmatePhotoUrl());

        String lSetData = "photo url";
        mWorkmate.setWorkmatePhotoUrl(lSetData);

        String lGetData = mWorkmate.getWorkmatePhotoUrl();
        assertNotNull(lGetData);
        assertEquals(lSetData, lGetData);
    }

    @Test
    public void setAndGetWorkmateLikesWithSuccess() {
        int lInitialNbLikes = mWorkmateLikesList.size();
        assertEquals(0, mWorkmateLikesList.size());

        mWorkmate = new Workmate(mNoId, mWorkmateLikesList);
        assertNotNull(mWorkmate);
        assertEquals(0,mWorkmate.getWorkmateLikes().size());

        Workmate.Likes lSetLike = new Workmate.Likes("tataid","tataeugenie");
        mWorkmateLikesList.add(lSetLike);
        assertEquals(lInitialNbLikes+1, mWorkmateLikesList.size());

        mWorkmate.setWorkmateLikes(mWorkmateLikesList);
        assertEquals(lInitialNbLikes+1, mWorkmate.getWorkmateLikes().size());

        Workmate.Likes lGetLike = mWorkmate.getWorkmateLikes().get(0);
        assertEquals(lSetLike,lGetLike);

        mWorkmateLikesList.remove(lSetLike);
        mWorkmate.setWorkmateLikes(mWorkmateLikesList);
        assertEquals(lInitialNbLikes,mWorkmate.getWorkmateLikes().size());
    }

    @Test
    public void setAndGetWorkmateRestoChosenWithSuccess() {
        Timestamp lTimestamp = Timestamp.now();

        mWorkmateRestoChosen = new Workmate.WorkmateRestoChoice();
        assertNotNull(mWorkmateRestoChosen);
        assertNull(mWorkmateRestoChosen.getRestoId());
        assertNull(mWorkmateRestoChosen.getRestoName());
        assertNull(mWorkmateRestoChosen.getRestoDateChoice());

        mWorkmate = new Workmate(mNoId,mNoName, mNoEmail, mNoPhotoUrl,mWorkmateRestoChosen);

        Workmate.WorkmateRestoChoice lSetData = new Workmate.WorkmateRestoChoice("tataid", "tata eugenie", lTimestamp);
        mWorkmate.setWorkmateRestoChosen(lSetData);

        Workmate.WorkmateRestoChoice lGetData = mWorkmate.getWorkmateRestoChosen();
        assertNotNull(lGetData);
        assertEquals(lSetData.getRestoId(), lGetData.getRestoId());
        assertEquals(lSetData.getRestoName(), lGetData.getRestoName());
        assertEquals(lSetData.getRestoDateChoice(), lGetData.getRestoDateChoice());
    }

    @Test
    public void setAndGetWorkmateLikesRestoIdWithSuccess() {
        mWorkmateLikes = new Workmate.Likes();
        assertNull(mWorkmateLikes.getRestoId());

        String lSetData = "tataid";
        mWorkmateLikes.setRestoId(lSetData);

        String lGetData = mWorkmateLikes.getRestoId();
        assertNotNull(lGetData);
        assertEquals(lSetData, lGetData);
    }

    @Test
    public void setAndGetWorkmateLikesRestoNameWithSuccess() {
        mWorkmateLikes = new Workmate.Likes();
        assertNull(mWorkmateLikes.getRestoName());

        String lSetData = "tata eugénie";
        mWorkmateLikes.setRestoName(lSetData);

        String lGetData = mWorkmateLikes.getRestoName();
        assertNotNull(lGetData);
        assertEquals(lSetData, lGetData);

    }

    @Test
    public void setAndGetWorkmateRestoChosenRestoIdWithSuccess() {
        mWorkmateRestoChosen = new Workmate.WorkmateRestoChoice();
        assertNotNull(mWorkmateRestoChosen);
        assertNull(mWorkmateRestoChosen.getRestoId());

        String lSetData = "tataid";
        mWorkmateRestoChosen.setRestoId(lSetData);

        String lGetData = mWorkmateRestoChosen.getRestoId();
        assertNotNull(lGetData);
        assertEquals(lSetData, lGetData);

    }

    @Test
    public void setAndGetWorkmateRestoChosenRestoNameWithSuccess() {
        mWorkmateRestoChosen = new Workmate.WorkmateRestoChoice();
        assertNotNull(mWorkmateRestoChosen);
        assertNull(mWorkmateRestoChosen.getRestoName());

        String lSetData = "tata eugénie";
        mWorkmateRestoChosen.setRestoName(lSetData);

        String lGetData = mWorkmateRestoChosen.getRestoName();
        assertNotNull(lGetData);
        assertEquals(lSetData, lGetData);
    }

    @Test
    public void setAndGetWorkmateRestoChosenRestoDateChoiceWithSuccess() {
        mWorkmateRestoChosen = new Workmate.WorkmateRestoChoice();
        assertNotNull(mWorkmateRestoChosen);
        assertNull(mWorkmateRestoChosen.getRestoDateChoice());


        Timestamp lSetData = Timestamp.now();
        mWorkmateRestoChosen.setRestoDateChoice(lSetData);

        Timestamp lGetData = mWorkmateRestoChosen.getRestoDateChoice();
        assertNotNull(lGetData);
        assertEquals(lSetData, lGetData);
    }

}
