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

    public RestaurantDetailViewModel(Restaurant pRestaurant) {
        Log.d(TAG, "RestaurantDetailViewModel: call get liste workmate in this restaurant");
        Go4LunchApi lApi = DI.getGo4LunchApiService();
        mLDChoiceList = mChoiceRepo.getWorkmateComingInRestaurant(pRestaurant, lApi.getWorkmateId());
    }

    /**
     * Access to ChoiceRepository
     */
    public LiveData<List<Workmate>> getWorkmateComingInRestaurant() {
        return mLDChoiceList;
    }

    public MutableLiveData<ActionStatus> getOrSaveWorkmateChoiceForRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {
       return mChoiceRepo.getOrSaveWorkmateChoiceForRestaurant(pWorkmate, pRestaurant, pActionStatus);
    }

    public LiveData<Boolean> hasAlreadyMadeAChoice(Workmate pWorkmate) {
        return mChoiceRepo.hasAlreadyMadeAChoice(pWorkmate);
    }
    /**
     * Access to WorkmateRepository
     */
    public MutableLiveData<ActionStatus> getOrSaveWorkmateLikeForRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {
        return mWorkmateRepo.getOrSaveWorkmateLikeForRestaurant(pWorkmate, pRestaurant, pActionStatus);
    }

}
