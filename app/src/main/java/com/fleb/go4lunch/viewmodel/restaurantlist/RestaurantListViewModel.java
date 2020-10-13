package com.fleb.go4lunch.viewmodel.restaurantlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class RestaurantListViewModel extends ViewModel {

    private MutableLiveData<List<Restaurant>> mLDRestoList = new MutableLiveData<>() ;

    private RestaurantRepository mRepository = new RestaurantRepository();

    public RestaurantListViewModel() {
        mLDRestoList = mRepository.getLDFirestoreRestaurantList();
    }

    public LiveData<List<Restaurant>>  getRestaurantList() {
        return mLDRestoList;
    }

}
