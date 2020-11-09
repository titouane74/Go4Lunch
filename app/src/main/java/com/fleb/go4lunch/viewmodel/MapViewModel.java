package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private RestaurantRepository mRepository ;

    public MapViewModel() {
        mRepository = new RestaurantRepository();
    }

    public LiveData<List<Restaurant>>  getRestaurantList() {
        return mRepository.getLDRestaurantList();
    }
}
