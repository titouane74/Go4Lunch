package com.fleb.go4lunch.viewmodel.map;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.service.Go4LunchApi;

import java.util.List;

public class MapViewModel extends ViewModel {

    private RestaurantRepository mRepository ;
    private MutableLiveData<List<Restaurant>> mLDRestoList = new MutableLiveData<>() ;
//    private Go4LunchApi mApi = DI.getGo4LunchApiService();

    public MapViewModel() {
        mRepository = new RestaurantRepository();
/*        Location lLocation = mApi.getLocation();
        mApi.saveLocationInSharedPreferences(lLocation);*/
//        mLDRestoList = mRepository.getRestaurantList();
    }

    public LiveData<List<Restaurant>>  getRestaurantList() {
        return mRepository.getRestaurantList();
    }

/*
    public void saveLocationInSharedPreferences(Location pLocation) {
        mRepository.saveLocationInSharedPreferences(pLocation);
    }
*/
}
