package com.fleb.go4lunch.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;

/**
 * Created by Florence LE BOURNOT on 15/11/2020
 */
public class Go4LunchViewModelFactory implements ViewModelProvider.Factory {

    private RestaurantRepository mRestaurantRepository;
    private WorkmateRepository mWorkmateRepository;

    public Go4LunchViewModelFactory(RestaurantRepository pRestaurantRepository,
                                    WorkmateRepository pWorkmateRepository) {
        this.mRestaurantRepository = pRestaurantRepository;
        this.mWorkmateRepository = pWorkmateRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(Go4LunchViewModel.class))
        {
            return (T) new Go4LunchViewModel(this.mRestaurantRepository, this.mWorkmateRepository);
        }
        throw new IllegalArgumentException("Problem with ViewModelFactory");
    }

}
