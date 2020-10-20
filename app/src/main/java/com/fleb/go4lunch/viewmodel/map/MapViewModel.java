package com.fleb.go4lunch.viewmodel.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private RestaurantRepository mRepository = new RestaurantRepository();

    public MapViewModel() { }

    public LiveData<List<Restaurant>> getRestaurantList() {
        Log.d("TAG_VM", "getRestoList: return list");
        return mRepository.getRestaurantList();
    }

    public void saveLocationInSharedPreferences(Location pLocation) {
        mRepository.saveLocationInSharedPreferences(pLocation);
    }
}
