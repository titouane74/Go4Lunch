package com.fleb.go4lunch.viewmodel.restaurantlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.RestaurantPojo;
import com.fleb.go4lunch.repository.RestaurantRepo;

import java.util.List;


public class RestaurantListViewModel extends ViewModel implements RestaurantRepo.OnFirestoreTaskComplete, RestaurantRepo.OnGoogleResponse {

    private MutableLiveData<List<Restaurant>> mRestoList = new MutableLiveData<>();

    public LiveData<List<Restaurant>> getRestoList() { return mRestoList; }

    public RestaurantListViewModel() {
        RestaurantRepo lRestoRepo = new RestaurantRepo(this);
        lRestoRepo.getRestoData();
    }
/*    public RestaurantListViewModel(double pLat, double pLong) {
        RestaurantRepo lRestoRepo = new RestaurantRepo(this);
        lRestoRepo.getRestaurantsPlaces(this, "restaurant", pLat,pLong);
    }*/

    @Override
    public void restoDataLoaded(List<Restaurant> pRestoList) {
        mRestoList.setValue(pRestoList);
    }

    @Override
    public void restoOnError(Exception pE) {

    }

    @Override
    public void restoDataGoogle(RestaurantPojo pRestoListGoogle) {

    }

    @Override
    public void restoOnErrrorGoogle(String pErrorBody) {

    }
}
