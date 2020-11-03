package com.fleb.go4lunch.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.service.Go4LunchApi;
import com.fleb.go4lunch.viewmodel.restaurantlist.RestaurantAdapter;
import com.fleb.go4lunch.viewmodel.restaurantlist.RestaurantListViewModel;

import java.util.List;

import static com.fleb.go4lunch.AppGo4Lunch.sApi;


/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class RestaurantListFragment extends BaseFragment {

    public static final String TAG = "TAG_RESTOLIST";
    private RestaurantAdapter mRestoAdapter;
    private RecyclerView mRecyclerView;
    private RestaurantListViewModel mRestaurantListViewModel;
//    private Go4LunchApi mApi;
    private List<Restaurant> mRestaurantList;

    public RestaurantListFragment() {}

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_restaurant_list; }

    @Override
    protected void configureFragmentOnCreateView(View pView) {
/*
        if(mApi == null) {
            mApi = DI.getGo4LunchApiService();
        }
*/

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
        mRestaurantListViewModel = new ViewModelProvider(requireActivity()).get(RestaurantListViewModel.class);

    }

    private void configureViewModel() {
        mRestaurantListViewModel.getRestaurantList().observe(getViewLifecycleOwner(), pRestaurantList -> {
            mRestoAdapter.setRestoList(pRestaurantList);
            mRestoAdapter.notifyDataSetChanged();
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        configureViewModel();
    }
}