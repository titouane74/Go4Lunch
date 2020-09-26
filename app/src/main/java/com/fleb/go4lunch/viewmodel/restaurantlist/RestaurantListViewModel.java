package com.fleb.go4lunch.viewmodel.restaurantlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepo;

import java.util.List;


public class RestaurantListViewModel extends ViewModel implements RestaurantRepo.OnFirestoreTaskComplete {

    private MutableLiveData<List<Restaurant>> mRestoList = new MutableLiveData<>();

    public LiveData<List<Restaurant>> getRestoList() { return mRestoList; }

    public RestaurantListViewModel() {
        RestaurantRepo lRestoRepo = new RestaurantRepo(this);
        lRestoRepo.getRestoData();
    }

    @Override
    public void restoDataLoaded(List<Restaurant> pRestoList) {
        mRestoList.setValue(pRestoList);
    }

    @Override
    public void restoOnError(Exception pE) {

    }
}
