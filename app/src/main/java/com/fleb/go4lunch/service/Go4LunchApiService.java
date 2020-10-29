package com.fleb.go4lunch.service;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by Florence LE BOURNOT on 26/10/2020
 */
public class Go4LunchApiService implements Go4LunchApi {

    private static final String TAG = "TAG_API_SERVICE";
    private Workmate mWorkmate;
    private String mWorkmateId;
    private Restaurant mRestaurant;
    private List<Restaurant> mRestaurantList;

    @Override
    public void setWorkmateId(FirebaseUser pUser) {
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

    @Override
    public void setRestaurant(Restaurant prestaurant) {
        mRestaurant = prestaurant;
    }

    @Override
    public Restaurant getRestaurant() {
        return mRestaurant;
    }

    @Override
    public void setRestaurantList(List<Restaurant> pRestaurantList) { mRestaurantList = pRestaurantList; }

    @Override
    public List<Restaurant> getRestaurantList() { return mRestaurantList; }
}
