package com.fleb.go4lunch.viewmodel.lunch;


import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.service.Go4LunchApi;

public class LunchViewModel extends ViewModel {

    private WorkmateRepository mWorkmateRepo = new WorkmateRepository();
    private Workmate mWorkmate;

    public LunchViewModel(Go4LunchApi pApi) {
        mWorkmate = pApi.getWorkmate();
    }

    public LiveData<String> getLunchRestaurant() {
        return mWorkmateRepo.getWorkmateRestaurantChoice(mWorkmate);
    }
}
