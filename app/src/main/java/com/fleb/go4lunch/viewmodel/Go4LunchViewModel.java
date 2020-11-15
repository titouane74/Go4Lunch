package com.fleb.go4lunch.viewmodel;

import android.content.Context;

import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.utils.ActionStatus;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import static com.fleb.go4lunch.AppGo4Lunch.sApi;

/**
 * Created by Florence LE BOURNOT on 15/11/2020
 */
public class Go4LunchViewModel extends ViewModel {

    private RestaurantRepository mRestaurantRepository;
    private WorkmateRepository mWorkmateRepository;

    private Workmate mWorkmate;
    private final MutableLiveData<String> mNotificationStatus;

    public Go4LunchViewModel(RestaurantRepository pRestaurantRepository,
                             WorkmateRepository pWorkmateRepository) {
        mRestaurantRepository = pRestaurantRepository;
        mWorkmateRepository = pWorkmateRepository;

        mWorkmate = sApi.getWorkmate();
        mNotificationStatus = new MutableLiveData<>();
    }

    //------------RESTAURANT LIST
    /**
     * Get restaurant list
     * @return : list object : restaurant list
     */
    public LiveData<List<Restaurant>> getRestaurantList() {
        return mRestaurantRepository.getLDRestaurantList();
    }

    //-----------WORKMATE LIST
    /**
     * Get workmate list
     * @return : list object : workmate list
     */
    public LiveData<List<Workmate>> getWorkmateList() {
        return mWorkmateRepository.getLDWorkmateListData();
    }

    //-----------MAIN ACTIVITY
    /**
     * Retrieve workmate information from Firestore
     * @param pWorkmateId : string : workmate id
     * @return : object : workmate
     */
    public LiveData<Workmate> getWorkmateInfos(String pWorkmateId) {
        return mWorkmateRepository.getWorkmateData(pWorkmateId);
    }

    /**
     * Get autocomplete prediction restaurant list
     * @param pPlacesClient : object : PlacesClient
     * @param pQuery : string : query to request
     * @return : list object : restaurant list
     */
    public LiveData<List<Restaurant>> getAutocompleteRestaurantList(PlacesClient pPlacesClient, String pQuery) {
        return mRestaurantRepository.getLDAutocompleteRestaurantList(pPlacesClient, pQuery);
    }


    //--------------RESTAURANT DETAIL

    /**
     * Get or save the workmate restaurant choice
     * @param pRestaurant: object : restaurant
     * @param pActionStatus : enum : action to do
     * @return : enum : result of the action
     */
    public MutableLiveData<ActionStatus> getOrSaveWorkmateChoiceForRestaurant(Restaurant pRestaurant, ActionStatus pActionStatus) {
        return mWorkmateRepository.getOrSaveWorkmateRestaurantChoice(pRestaurant, pActionStatus);
    }

    /**
     * Get or save the workmate restaurant like
     * @param pRestaurant: object : restaurant
     * @param pActionStatus : enum : action to do
     * @return : enum : result of the action
     */

    public MutableLiveData<ActionStatus> getOrSaveWorkmateLikeForRestaurant(Restaurant pRestaurant,ActionStatus pActionStatus) {
        return mWorkmateRepository.getOrSaveWorkmateLikeForRestaurant(mWorkmate,pRestaurant, pActionStatus);
    }

    /**
     * Get  workmate information
     * @return : object : workmate
     */
    public MutableLiveData<Workmate> getWorkmateData() {
        return mWorkmateRepository.getWorkmateData(mWorkmate.getWorkmateId());
    }


    /**
     * Get detail restaurant
     * @return : object : restaurant
     */
    public MutableLiveData<Restaurant> getRestaurantDetail(String pRestaurantId) {
        return mRestaurantRepository.getLDRestaurantDetail(pRestaurantId);
    }


    //-------------SETTINGS
    /**
     * Display the right text for the notification activated or not
     * @param pContext : object : context
     * @return : string : text to display
     */
    public LiveData<String> getNotificationStatus(Context pContext) {
        NotificationManagerCompat lNotificationManager = NotificationManagerCompat.from(pContext);

        boolean lIsNotificationEnable = lNotificationManager.areNotificationsEnabled();

        if (!lIsNotificationEnable) {
            mNotificationStatus.setValue(pContext.getString(R.string.text_setting_notif_deactivated));
        } else {
            mNotificationStatus.setValue(pContext.getString(R.string.text_setting_notif_activated));
        }
        return mNotificationStatus;
    }

    /**
     * Retrieve workmate information from Firestore
     * @param pWorkmateId : string : workmate id
     * @return : object : workmate
     */
    public LiveData<Workmate> getWorkmateData(String pWorkmateId) {
        return mWorkmateRepository.getWorkmateData(pWorkmateId);
    }

    /**
     * Update the workmate user name in Firestore
     * @param pWorkmate : object : workmate
     * @param pNewUserName : string : new user name
     * @return : enum : result of the action
     */
    public LiveData<ActionStatus> updateWorkmateUserName(Workmate pWorkmate, String pNewUserName) {

        return mWorkmateRepository.updateWorkmateUserName(pWorkmate, pNewUserName);
    }

    //------------AUTHENTICATION
    /**
     * Save the firebase user and workmate in Firestore
     * @param pUser : object : FirebaseUser
     * @return : enum : result of the action
     */
    public LiveData<ActionStatus> saveWorkmateFirebaseProfile(FirebaseUser pUser) {

        return mWorkmateRepository.saveWorkmateFirebaseProfile(pUser);
    }

}
