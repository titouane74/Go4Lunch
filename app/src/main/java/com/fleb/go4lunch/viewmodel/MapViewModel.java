package com.fleb.go4lunch.viewmodel;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepo;

import java.util.List;

public class MapViewModel extends ViewModel implements RestaurantRepo.OnFirestoreTaskComplete {

    private MutableLiveData<List<Restaurant>> mRestoList = new MutableLiveData<>();

    public LiveData<List<Restaurant>> getRestoList() { return mRestoList; }

    public MapViewModel() {}

    public MapViewModel(Context pContext, Double pLat, Double pLng) {
        RestaurantRepo lRestoRepo = new RestaurantRepo(this);
        lRestoRepo.getRestaurantsPlaces(pContext,pLat,pLng);
    }

    @Override
    public void restoDataLoaded(List<Restaurant> pRestoList) {
        mRestoList.setValue(pRestoList);
    }

    @Override
    public void restoOnError(Exception pE) {

    }

    @Override
    public void restoOnGoogleError(String pErrorGoogle) {

    }
}
