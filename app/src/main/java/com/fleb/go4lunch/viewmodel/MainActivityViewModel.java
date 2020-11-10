package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

/**
 * Created by Florence LE BOURNOT on 26/10/2020
 */
public class MainActivityViewModel extends ViewModel {

    private WorkmateRepository mWorkmateRepo = new WorkmateRepository();
    private RestaurantRepository mRestaurantRepo = new RestaurantRepository();

    public LiveData<Workmate> getWorkmateInfos(String pWorkmateId) {
        return mWorkmateRepo.getWorkmateData(pWorkmateId);
    }

    public LiveData<List<Restaurant>> getAutocompleteRestaurantList(PlacesClient pPlacesClient, String pQuery) {
        return mRestaurantRepo.getLDAutocompleteRestaurantList(pPlacesClient, pQuery);
    }

    public LiveData<List<Restaurant>>  getRestaurantList() {
        return mRestaurantRepo.getLDRestaurantList();
    }
}
