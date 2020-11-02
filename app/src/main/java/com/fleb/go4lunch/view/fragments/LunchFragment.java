package com.fleb.go4lunch.view.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.service.Go4LunchApi;
import com.fleb.go4lunch.viewmodel.lunch.LunchViewModel;
import com.fleb.go4lunch.viewmodel.lunch.LunchViewModelFactory;
import com.fleb.go4lunch.viewmodel.restaurantlist.RestaurantListViewModel;

import static com.fleb.go4lunch.AppGo4Lunch.sApi;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class LunchFragment extends BaseFragment {

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_lunch; }

    @Override
    protected void configureFragmentOnCreateView(View pView) {
//        Go4LunchApi lApi = DI.getGo4LunchApiService();

//        LunchViewModelFactory lFactory = new LunchViewModelFactory(sApi);
//        LunchViewModel lLunchViewModel = new ViewModelProvider(requireActivity(),lFactory).get(LunchViewModel.class);
        LunchViewModel lLunchViewModel = new ViewModelProvider(requireActivity()).get(LunchViewModel.class);

        final TextView lTextLunch = pView.findViewById(R.id.text_lunch);
        lLunchViewModel.getLunchRestaurant().observe(getViewLifecycleOwner(), pRestaurantName -> {
            if(!pRestaurantName.equals("")) {
                lTextLunch.setText(pRestaurantName);
            } else {
                lTextLunch.setText(getString(R.string.text_lunch_not_choosed));
            }
        });
    }

}