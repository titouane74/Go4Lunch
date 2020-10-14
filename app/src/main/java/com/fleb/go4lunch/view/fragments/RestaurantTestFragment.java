package com.fleb.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.viewmodel.map.MapViewModel;
import com.fleb.go4lunch.viewmodel.map.MapViewModelFactory;
import com.fleb.go4lunch.viewmodel.restaurantlist.RestaurantAdapter;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class RestaurantTestFragment extends BaseFragment {

    private RestaurantAdapter mRestoAdapter;

    public RestaurantTestFragment() {}

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_restaurant_list;
    }

    @Override
    protected void configureFragmentOnCreateView(View pView) {

        RecyclerView lRecyclerView = pView.findViewById(R.id.restaurant_list);
        mRestoAdapter = new RestaurantAdapter();

        lRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        lRecyclerView.setAdapter(mRestoAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Double lLatitude = 48.8236549;
        Double lLongitude = 2.4102578;

        MapViewModelFactory lFactory = new MapViewModelFactory(getContext(),lLatitude, lLongitude);

        MapViewModel lMapViewModel = new ViewModelProvider(requireActivity(),lFactory).get(MapViewModel.class);

        lMapViewModel.getRestaurantList().observe(getViewLifecycleOwner(), pRestaurants -> {
            mRestoAdapter.setRestoList(pRestaurants);
            mRestoAdapter.notifyDataSetChanged();
        });

    }

}