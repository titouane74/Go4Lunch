package com.fleb.go4lunch.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.viewmodel.AuthenticationViewModel;
import com.fleb.go4lunch.viewmodel.MainActivityViewModel;
import com.fleb.go4lunch.viewmodel.MapViewModel;
import com.fleb.go4lunch.viewmodel.RestaurantDetailViewModel;
import com.fleb.go4lunch.viewmodel.RestaurantListViewModel;
import com.fleb.go4lunch.viewmodel.SettingsViewModel;
import com.fleb.go4lunch.viewmodel.WorkmateListViewModel;

import java.lang.reflect.InvocationTargetException;

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

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthenticationViewModel.class)) {
            return (T) new AuthenticationViewModel(this.mWorkmateRepository);
        }
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(this.mRestaurantRepository, this.mWorkmateRepository);
        }
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(this.mRestaurantRepository);
        }
        if (modelClass.isAssignableFrom(RestaurantDetailViewModel.class)) {
            return (T) new RestaurantDetailViewModel(this.mRestaurantRepository, this.mWorkmateRepository);
        }
        if (modelClass.isAssignableFrom(RestaurantListViewModel.class)) {
            return (T) new RestaurantListViewModel(this.mRestaurantRepository);
        }
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(this.mWorkmateRepository);
        }
        if (modelClass.isAssignableFrom(WorkmateListViewModel.class)) {
            return (T) new WorkmateListViewModel(this.mWorkmateRepository);
        }
        throw new IllegalArgumentException("Problem with ViewModelFactory");
    }

}
