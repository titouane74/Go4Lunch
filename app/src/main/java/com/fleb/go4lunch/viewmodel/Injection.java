package com.fleb.go4lunch.viewmodel;

import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;

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
