package com.fleb.go4lunch.view.restaurantlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fleb.go4lunch.R;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class RestaurantListFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RestaurantListViewModel lRestaurantListViewModel = ViewModelProviders.of(this).get(RestaurantListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

/*
        final TextView textView = root.findViewById(R.id.text_view_list);
        lViewListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
*/
        return root;
    }

}