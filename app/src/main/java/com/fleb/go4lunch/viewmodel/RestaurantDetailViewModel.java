package com.fleb.go4lunch.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.utils.ActionStatus;
import com.google.api.LogDescriptor;

import static com.fleb.go4lunch.AppGo4Lunch.sApi;


/**
 * Created by Florence LE BOURNOT on 25/10/2020
 */
public class RestaurantDetailViewModel extends ViewModel {

    private final WorkmateRepository mWorkmateRepo ;
    private final RestaurantRepository mRestaurantRepo ;
    //private final Workmate mWorkmate;

    public RestaurantDetailViewModel(RestaurantRepository pRestaurantRepository, WorkmateRepository pWorkmateRepository) {
        mRestaurantRepo = pRestaurantRepository;
        mWorkmateRepo = pWorkmateRepository;
//        mWorkmate = sApi.getWorkmate();
    }

    /**
     * Get or save the workmate restaurant choice
     * @param pRestaurant: object : restaurant
     * @param pActionStatus : enum : action to do
     * @return : enum : result of the action
     */
    public MutableLiveData<ActionStatus> getOrSaveWorkmateChoiceForRestaurant(Restaurant pRestaurant,ActionStatus pActionStatus) {
        return mWorkmateRepo.getOrSaveWorkmateRestaurantChoice(pRestaurant, pActionStatus);
    }

    /**
     * Get or save the workmate restaurant like
     * @param pRestaurant: object : restaurant
     * @param pActionStatus : enum : action to do
     * @return : enum : result of the action
     */

    public MutableLiveData<ActionStatus> getOrSaveWorkmateLikeForRestaurant(Restaurant pRestaurant,ActionStatus pActionStatus) {
        return mWorkmateRepo.getOrSaveWorkmateLikeForRestaurant(pRestaurant, pActionStatus);
    }

    /**
     * Get  workmate information
     * @return : object : workmate
     */
    public MutableLiveData<Workmate> getWorkmateData(String pWorkmateId) {
        return mWorkmateRepo.getWorkmateData(pWorkmateId);
    }

    /**
     * Get detail restaurant
     * @return : object : restaurant
     */
    public MutableLiveData<Restaurant> getRestaurantDetail(String pRestaurantId) {
        return mRestaurantRepo.getLDRestaurantDetail(pRestaurantId);
    }

}
