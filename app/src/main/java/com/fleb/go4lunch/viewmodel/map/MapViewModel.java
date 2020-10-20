package com.fleb.go4lunch.viewmodel.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private MutableLiveData<List<Restaurant>> mLDRestoList = new MutableLiveData<>();

    private RestaurantRepository mRepository = new RestaurantRepository();

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private Double mLat, mLng;

    public MapViewModel(Context pContext, Double pLat, Double pLng) {
        //mLDRestoList = mRepository.getLDGoogleRestaurantList(pContext, pLat, pLng);
        //mLDRestoList = mRepository.getLDFirestoreRestaurantList();
        mContext = pContext;
        mLat = pLat;
        mLng = pLng;
    }

    public MapViewModel() {
        Log.d("TAG_VM", "MapViewModel: enter call getFirestore");
        //mLDRestoList = mRepository.getLDFirestoreRestaurantList();
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        Log.d("TAG_VM", "getRestoList: return list");
        //mRepository.blabla();
        mLDRestoList = mRepository.getRestaurantList(mContext, mLat, mLng);
        return mLDRestoList;
    }

    // methode void SaveLocation -> remonte au repo

}
