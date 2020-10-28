package com.fleb.go4lunch.viewmodel.map;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private RestaurantRepository mRepository = new RestaurantRepository();
    private MutableLiveData<List<Restaurant>> mLDRestoList = new MutableLiveData<>() ;

    public MapViewModel() {
        mLDRestoList = mRepository.getRestaurantList();
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        return mLDRestoList;
    }

    public void saveLocationInSharedPreferences(Location pLocation) {
        mRepository.saveLocationInSharedPreferences(pLocation);
    }
}
