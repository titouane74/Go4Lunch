package com.fleb.go4lunch.viewmodel.lunch;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.service.Go4LunchApi;

/**
 * Created by Florence LE BOURNOT on 29/10/2020
 */
public class LunchViewModelFactory implements ViewModelProvider.Factory {
    private Go4LunchApi mApi;

    public LunchViewModelFactory(Go4LunchApi pApi) {
        mApi = DI.getGo4LunchApiService();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LunchViewModel(this.mApi);
    }
}
