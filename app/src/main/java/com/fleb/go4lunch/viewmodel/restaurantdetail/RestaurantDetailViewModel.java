package com.fleb.go4lunch.viewmodel.restaurantdetail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.ChoiceRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.service.Go4LunchApi;
import com.fleb.go4lunch.utils.ActionStatus;

import java.util.List;

/**
 * Created by Florence LE BOURNOT on 25/10/2020
 */
public class RestaurantDetailViewModel extends ViewModel {

    public static final String TAG = "TAG_RESTO_DET_VM";
    private WorkmateRepository mWorkmateRepo = new WorkmateRepository();
    private ChoiceRepository mChoiceRepo = new ChoiceRepository();
    private MutableLiveData<List<Workmate>> mLDChoiceList ;
    private Go4LunchApi mApi;
    private Restaurant mRestaurant;
    private Workmate mWorkmate;

    public RestaurantDetailViewModel(Go4LunchApi pApi) {
        mApi = pApi;
        mRestaurant = mApi.getRestaurant();
        mWorkmate = mApi.getWorkmate();
    }

    /**
     * Access to ChoiceRepository
     */
    public LiveData<List<Workmate>> getWorkmateComingInRestaurant() {
        return mChoiceRepo.getWorkmateComingInRestaurant(mRestaurant, mApi.getWorkmateId());
    }

    public MutableLiveData<ActionStatus> getOrSaveWorkmateChoiceForRestaurant(ActionStatus pActionStatus) {
       return mChoiceRepo.getOrSaveWorkmateChoiceForRestaurant(mWorkmate, mRestaurant, pActionStatus);
    }

    public LiveData<Boolean> hasAlreadyMadeAChoice() {
        return mChoiceRepo.hasAlreadyMadeAChoice(mWorkmate, mRestaurant);
    }
    /**
     * Access to WorkmateRepository
     */
    public MutableLiveData<ActionStatus> getOrSaveWorkmateLikeForRestaurant(ActionStatus pActionStatus) {

        return mWorkmateRepo.getOrSaveWorkmateLikeForRestaurant(mWorkmate,mRestaurant, pActionStatus);
    }

}
