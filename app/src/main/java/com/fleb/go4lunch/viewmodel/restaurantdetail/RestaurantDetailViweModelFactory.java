package com.fleb.go4lunch.viewmodel.restaurantdetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.service.Go4LunchApi;

/**
 * Created by Florence LE BOURNOT on 26/10/2020
 */
public class RestaurantDetailViweModelFactory implements ViewModelProvider.Factory {

    private Restaurant mRestaurant;
    private Go4LunchApi mApi;

    public RestaurantDetailViweModelFactory(Go4LunchApi pApi) {
        mApi = pApi;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RestaurantDetailViewModel(this.mApi);
    }
}
