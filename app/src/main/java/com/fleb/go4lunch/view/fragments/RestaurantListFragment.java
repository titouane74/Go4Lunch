package com.fleb.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.viewmodel.restaurantlist.RestaurantAdapter;
import com.fleb.go4lunch.viewmodel.restaurantlist.RestaurantListViewModel;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class RestaurantListFragment extends BaseFragment {

    private RestaurantAdapter mRestoAdapter;

    public RestaurantListFragment() {}

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_restaurant_list; }

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
        RestaurantListViewModel lRestaurantListViewModel = new ViewModelProvider(requireActivity()).get(RestaurantListViewModel.class);
        lRestaurantListViewModel.getRestoList().observe(getViewLifecycleOwner(),pRestaurants -> {
            mRestoAdapter.setRestoList(pRestaurants);
            mRestoAdapter.notifyDataSetChanged();
        });
    }
}