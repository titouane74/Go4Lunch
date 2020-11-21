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
     * @param pRestaurant: object : restaurant
     * @param pActionStatus : enum : action to do
     * @return : enum : result of the action
     */
    public MutableLiveData<ActionStatus> getOrSaveWorkmateChoiceForRestaurant(Restaurant pRestaurant,ActionStatus pActionStatus) {
        return mWorkmateRepo.getOrSaveLDWorkmateRestaurantChoice(pRestaurant, pActionStatus);
    }

    /**
     * Get or save the workmate restaurant like
     * @param pRestaurant: object : restaurant
     * @param pActionStatus : enum : action to do
     * @return : enum : result of the action
     */

    public MutableLiveData<ActionStatus> getOrSaveWorkmateLikeForRestaurant(Restaurant pRestaurant,ActionStatus pActionStatus) {
        return mWorkmateRepo.getOrSaveLDWorkmateLikeForRestaurant(pRestaurant, pActionStatus);
    }

    /**
     * Get  workmate information
     * @return : object : workmate
     */
    public MutableLiveData<Workmate> getWorkmateData(String pWorkmateId) {
        return mWorkmateRepo.getLDWorkmateData(pWorkmateId);
    }

    /**
     * Get detail restaurant
     * @return : object : restaurant
     */
    public MutableLiveData<Restaurant> getRestaurantDetail(String pRestaurantId) {
        return mRestaurantRepo.getLDRestaurantDetail(pRestaurantId);
    }

}
