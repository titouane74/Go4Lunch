package com.fleb.go4lunch.viewmodel.restaurantdetail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Choice;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.ChoiceRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.utils.ActionStatus;

import java.util.List;

/**
 * Created by Florence LE BOURNOT on 25/10/2020
 */
public class RestaurantDetailViewModel extends ViewModel {

    public static final String TAG = "TAG_RESTO_DET_VM";
    private WorkmateRepository mWorkmateRepo = new WorkmateRepository();
    private ChoiceRepository mChoiceRepo = new ChoiceRepository();
    private MutableLiveData<List<Choice>> mLDChoiceList ;

    public RestaurantDetailViewModel(Restaurant pRestaurant) {
        //TODO get de la liste des utlisateurs qui ont choisi ce restaurant
        Log.d(TAG, "RestaurantDetailViewModel: call get liste workmate in this restaurant");
        mLDChoiceList = mChoiceRepo.getWorkmateComingInRestaurant(pRestaurant.getRestoPlaceId());
    }

    /**
     * Access to ChoiceRepository
     */

    //TODO retourne la liste des workmates ayant choisi le resto
    public LiveData<List<Choice>> getWorkmateComingInRestaurant() {
        Log.d(TAG, "getWorkmateComingInRestaurant: return the workmate list to the view");
        return mLDChoiceList;
    }

    public MutableLiveData<ActionStatus> getOrSaveWorkmateChoiceForRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {
       return mChoiceRepo.getOrSaveWorkmateChoiceForRestaurant(pWorkmate, pRestaurant, pActionStatus);
    }

    /**
     * Access to WorkmateRepository
     */
    public MutableLiveData<ActionStatus> getOrSaveWorkmateLikeForRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {
        return mWorkmateRepo.getOrSaveWorkmateLikeForRestaurant(pWorkmate, pRestaurant, pActionStatus);
    }

    public LiveData<Workmate> getWorkmateInfos(String pWorkmateId) {
        return mWorkmateRepo.getWorkmate(pWorkmateId);
    }
}
