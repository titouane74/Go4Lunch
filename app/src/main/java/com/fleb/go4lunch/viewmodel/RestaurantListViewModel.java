package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class RestaurantListViewModel extends ViewModel {
    public static final String TAG = "TAG_VM";
    private RestaurantRepository mRepository;

    public RestaurantListViewModel() {
        mRepository = new RestaurantRepository();
    }

    /**
     * Get restaurant list
     * @return : list object : restaurant list
     */
    public LiveData<List<Restaurant>>  getRestaurantList() {
        return mRepository.getLDRestaurantList();
    }

}
