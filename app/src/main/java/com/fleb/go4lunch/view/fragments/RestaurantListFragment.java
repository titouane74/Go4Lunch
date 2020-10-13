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
    private RecyclerView mRecyclerView;

    public RestaurantListFragment() {}

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_restaurant_list; }

    @Override
    protected void configureFragmentOnCreateView(View pView) {
        mRecyclerView = pView.findViewById(R.id.restaurant_list);

        initRecyclerView();
    }

    private void initRecyclerView()  {
        mRestoAdapter = new RestaurantAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mRestoAdapter);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RestaurantListViewModel lRestaurantListViewModel = new ViewModelProvider(requireActivity()).get(RestaurantListViewModel.class);
        lRestaurantListViewModel.getRestaurantList().observe(getViewLifecycleOwner(), pRestaurantList -> {
            mRestoAdapter.setRestoList(pRestaurantList);
            mRestoAdapter.notifyDataSetChanged();
        });
    }
}