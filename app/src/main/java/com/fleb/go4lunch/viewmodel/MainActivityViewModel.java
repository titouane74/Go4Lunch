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

    /**
     * Retrieve workmate informations from Firestore
     * @param pWorkmateId : string : workmate id
     * @return : object : workmate
     */
    public LiveData<Workmate> getWorkmateInfos(String pWorkmateId) {
        return mWorkmateRepo.getWorkmateData(pWorkmateId);
    }

    /**
     * Get autocomplete prediction restaurant list
     * @param pPlacesClient : object : PlacesClient
     * @param pQuery : string : query to request
     * @return : list object : restaurant list
     */
    public LiveData<List<Restaurant>> getAutocompleteRestaurantList(PlacesClient pPlacesClient, String pQuery) {
        return mRestaurantRepo.getLDAutocompleteRestaurantList(pPlacesClient, pQuery);
    }

    /**
     * Get restaurant list
     * @return : list object : restaurant list
     */
    public LiveData<List<Restaurant>>  getRestaurantList() {
        return mRestaurantRepo.getLDRestaurantList();
    }
}
