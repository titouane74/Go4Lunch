package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.utils.ActionStatus;


/**
 * Created by Florence LE BOURNOT on 25/10/2020
 */
public class RestaurantDetailViewModel extends ViewModel {

    private final WorkmateRepository mWorkmateRepo ;
    private final RestaurantRepository mRestaurantRepo ;

    public RestaurantDetailViewModel(RestaurantRepository pRestaurantRepository, WorkmateRepository pWorkmateRepository) {
        mRestaurantRepo = pRestaurantRepository;
        mWorkmateRepo = pWorkmateRepository;
    }

    /**
     * Get or save the workmate restaurant choice
     * @param pActionStatus : enum : action to do
     * @return : enum : result of the action
     */
    public MutableLiveData<ActionStatus> getOrSaveWorkmateChoiceForRestaurant(ActionStatus pActionStatus) {
        return mWorkmateRepo.getOrSaveLDWorkmateRestaurantChoice(pActionStatus);
    }

    /**
     * Get or save the workmate restaurant like
     * @param pActionStatus : enum : action to do
     * @return : enum : result of the action
     */
    public MutableLiveData<ActionStatus> getOrSaveWorkmateLikeForRestaurant(ActionStatus pActionStatus) {
        return mWorkmateRepo.getOrSaveLDWorkmateLikeForRestaurant(pActionStatus);
    }

    /**
     * Get  workmate information
     * @return : object : workmate
     */
    public MutableLiveData<Workmate> getWorkmateData() {
        return mWorkmateRepo.getLDWorkmateData();
    }

    /**
     * Get detail restaurant
     * @return : object : restaurant
     */
    public MutableLiveData<Restaurant> getRestaurantDetail() {
        return mRestaurantRepo.getLDRestaurantDetail();
    }

}
