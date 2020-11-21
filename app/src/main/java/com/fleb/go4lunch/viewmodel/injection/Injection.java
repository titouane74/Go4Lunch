package com.fleb.go4lunch.viewmodel.injection;

import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.viewmodel.factory.Go4LunchViewModelFactory;

/**
 * Created by Florence LE BOURNOT on 15/11/2020
 */
public class Injection {

    private static RestaurantRepository createRestaurantRepository() {
        return new RestaurantRepository();
    }
    private static WorkmateRepository createWorkmateRepository() {
        return new WorkmateRepository();
    }
    public static Go4LunchViewModelFactory go4LunchViewModelFactory() {
        return new Go4LunchViewModelFactory(createRestaurantRepository(), createWorkmateRepository());
    }
}
