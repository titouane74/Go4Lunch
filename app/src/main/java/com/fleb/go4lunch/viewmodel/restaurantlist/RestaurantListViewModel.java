package com.fleb.go4lunch.viewmodel.restaurantlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class RestaurantListViewModel extends ViewModel {
    public static final String TAG = "TAG_VM";
    private MutableLiveData<List<Restaurant>> mLDRestoList = new MutableLiveData<>() ;
    private RestaurantRepository mRepository;

    public RestaurantListViewModel() {
        mRepository = new RestaurantRepository();
/*        Log.d(TAG, "RestaurantListViewModel: VM : init repository");
        mLDRestoList = mRepository.getRestaurantList();
        Log.d(TAG, "RestaurantListViewModel: VM : get list restaurant from repository");*/
    }

    public LiveData<List<Restaurant>>  getRestaurantList() {
//        Log.d(TAG, "getRestaurantList: VM getter seulement");
        //La liste ne sera jamais mise à jour s'il y a un changement de données
        return mRepository.getLDRestaurantList();
    }

}
