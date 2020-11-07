package com.fleb.go4lunch.viewmodel.restaurantdetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fleb.go4lunch.service.Go4LunchApi;

/**
 * Created by Florence LE BOURNOT on 26/10/2020
 */
public class RestaurantDetailViewModelFactory implements ViewModelProvider.Factory {

    private Go4LunchApi mApi;

    public RestaurantDetailViewModelFactory(Go4LunchApi pApi) {
        mApi = pApi;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RestaurantDetailViewModel(this.mApi);
    }
}
