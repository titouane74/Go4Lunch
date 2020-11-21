package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class RestaurantListViewModel extends ViewModel {

    private final RestaurantRepository mRepository;

    public RestaurantListViewModel(RestaurantRepository pRestaurantRepository) {
        mRepository = pRestaurantRepository;
    }

    /**
     * Get restaurant list
     * @return : list object : restaurant list
     */
    public LiveData<List<Restaurant>>  getRestaurantList() {
        return mRepository.getLDRestaurantList();
    }

}
