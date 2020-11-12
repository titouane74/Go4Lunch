package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.utils.ActionStatus;

import static com.fleb.go4lunch.AppGo4Lunch.sApi;


/**
 * Created by Florence LE BOURNOT on 25/10/2020
 */
public class RestaurantDetailViewModel extends ViewModel {

    private WorkmateRepository mWorkmateRepo = new WorkmateRepository();
    private RestaurantRepository mRestaurantRepo = new RestaurantRepository();
    private Workmate mWorkmate;

    public RestaurantDetailViewModel() {
        mWorkmate = sApi.getWorkmate();
    }

//     Access to WorkmateRepository

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
        return mWorkmateRepo.getOrSaveWorkmateLikeForRestaurant(mWorkmate,pRestaurant, pActionStatus);
    }

    /**
     * Get  workmate informations
     * @return : object : workmate
     */
    public MutableLiveData<Workmate> getWorkmateData() {
        return mWorkmateRepo.getWorkmateData(mWorkmate.getWorkmateId());
    }


//     Access to RestaurantRepository

    /**
     * Get detail restaurant
     * @return : object : restaurant
     */
    public MutableLiveData<Restaurant> getRestaurantDetail(String pRestaurantId) {
        return mRestaurantRepo.getLDRestaurantDetail(pRestaurantId);
    }

}
