package com.fleb.go4lunch.viewmodel.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;

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
        return mRepository.getLDRestaurantList();
    }

/*
    public void saveLocationInSharedPreferences(Location pLocation) {
        mRepository.saveLocationInSharedPreferences(pLocation);
    }
*/
}
