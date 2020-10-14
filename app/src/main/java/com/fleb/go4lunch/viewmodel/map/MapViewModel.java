package com.fleb.go4lunch.viewmodel.map;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private MutableLiveData<List<Restaurant>> mLDRestoList = new MutableLiveData<>();

    private RestaurantRepository mRepository = new RestaurantRepository();

    public MapViewModel(Context pContext, Double pLat, Double pLng) {
        //TODO temporarely
        //mLDRestoList = mRepository.getLDGoogleRestaurantList(pContext, pLat, pLng);
        mLDRestoList = mRepository.getLDFirestoreRestaurantList();
    }

    public MapViewModel() {
        Log.d("TAG_VM", "MapViewModel: enter call getFirestore");
        mLDRestoList = mRepository.getLDFirestoreRestaurantList();
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        Log.d("TAG_VM", "getRestoList: return list");
        return mLDRestoList;
    }

    public void saveFirestoreRestaurant(Restaurant pRestaurant) {
            mRepository.saveRestaurant(pRestaurant);
    }

    public LiveData<Restaurant> getGoogleRestaurantDetail(Context pContext, Restaurant pRestaurant) {

        return mRepository.getGoogleDetailRestaurant(pContext, pRestaurant);
    }

}
