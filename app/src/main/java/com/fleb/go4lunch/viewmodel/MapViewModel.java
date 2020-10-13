package com.fleb.go4lunch.viewmodel;


import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.RestaurantPojo;
import com.fleb.go4lunch.repository.RestaurantRepo;
import com.fleb.go4lunch.repository.RestaurantRepository;

import java.util.List;

import static com.fleb.go4lunch.network.JsonRetrofitApi.BASE_URL_GOOGLE;

//V1
 public class MapViewModel extends ViewModel implements RestaurantRepo.OnFirestoreTaskComplete {
//    public class MapViewModel extends ViewModel  {

    private MutableLiveData<List<Restaurant>> mRestoList = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> mLDRestoList = new MutableLiveData<>();
    private RestaurantRepository mRepository = new RestaurantRepository();

    private Double mLat, mLng;

    public MapViewModel() { }

    public void initViewModel(Context pContext, Double pLat, Double pLng) {
        mLat = pLat;
        mLng = pLng;
        mLDRestoList = mRepository.getLDGoogleRestaurantList(pContext, mLat, mLng);
    }

    public LiveData<List<Restaurant>> getRestaurantList() { return mLDRestoList; }


//V1
    public LiveData<List<Restaurant>> getRestoList() { return mRestoList; }

    public MapViewModel(Context pContext, Double pLat, Double pLng) {
        RestaurantRepo lRestoRepo = new RestaurantRepo(this);
        lRestoRepo.getRestaurantsPlaces(pContext,pLat,pLng);
    }

    @Override
    public void restoDataLoaded(List<Restaurant> pRestoList) {
        mRestoList.setValue(pRestoList);
    }

    @Override
    public void restoOnGoogleError(String pErrorGoogle) {

    }

}
