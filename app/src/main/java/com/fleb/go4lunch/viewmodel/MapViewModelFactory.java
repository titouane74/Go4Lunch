package com.fleb.go4lunch.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


/**
 * Created by Florence LE BOURNOT on 07/10/2020
 */
public class MapViewModelFactory implements ViewModelProvider.Factory {

    private Context mContext;
    private String mType;
    private Double mLat;
    private Double mLng;
    private String mKey;

    public MapViewModelFactory(Context pContext, String pType, Double pLat, Double pLng, String pKey) {
        mContext = pContext;
        mType = pType;
        mLat = pLat;
        mLng = pLng;
        mKey = pKey;
    }

        @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class))
        {
            return (T) new MapViewModel(mContext,mType,mLat,mLng,mKey);
        }
        throw new IllegalArgumentException("Problem with ViewModelFactory");    }


}
