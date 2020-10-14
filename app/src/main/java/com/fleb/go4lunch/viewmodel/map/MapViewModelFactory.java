package com.fleb.go4lunch.viewmodel.map;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fleb.go4lunch.viewmodel.map.MapViewModel;


/**
 * Created by Florence LE BOURNOT on 07/10/2020
 */
public class MapViewModelFactory implements ViewModelProvider.Factory {

    private Context mContext;
    private Double mLat;
    private Double mLng;

    public MapViewModelFactory(Context pContext, Double pLat, Double pLng) {
        mContext = pContext;
        mLat = pLat;
        mLng = pLng;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class))
        {
            return (T) new MapViewModel(mContext,mLat,mLng);
            //return null;
        }
        throw new IllegalArgumentException("Problem with ViewModelFactory");    }


}
