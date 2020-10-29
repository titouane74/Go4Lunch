package com.fleb.go4lunch.service;

import android.util.Log;

import com.fleb.go4lunch.model.Workmate;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Florence LE BOURNOT on 26/10/2020
 */
public class Go4LunchApiService implements Go4LunchApi {

    private static final String TAG = "TAG_API_SERVICE";
    private Workmate mWorkmate;
    private String mWorkmateId;

    @Override
    public void saveWorkmateId(FirebaseUser pUser) {
        mWorkmateId = pUser.getUid();
    }

    @Override
    public String getWorkmateId() {
        return mWorkmateId;
    }

    @Override
    public void setWorkmate(Workmate pWorkmate) {
        mWorkmate = pWorkmate;
    }

    @Override
    public Workmate getWorkmate() {
        return mWorkmate;
    }
}