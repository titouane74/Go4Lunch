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


/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class RestaurantListFragment extends BaseFragment {

    public static final String TAG = "TAG_RESTOLIST";
    private RestaurantAdapter mRestoAdapter;
    private RecyclerView mRecyclerView;
    private RestaurantListViewModel mRestaurantListViewModel;
    private Go4LunchApi mApi;
    private List<Restaurant> mRestaurantList;

    public RestaurantListFragment() {}

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_restaurant_list; }

    @Override
    protected void configureFragmentOnCreateView(View pView) {
        if(mApi == null) {
            mApi = DI.getGo4LunchApiService();
        }

        Log.d(TAG, "configureFragmentOnCreateView: enter");
        mRecyclerView = pView.findViewById(R.id.restaurant_list);

        initRecyclerView();
    }

    private void initRecyclerView()  {
        Log.d(TAG, "initRecyclerView: enter");
        mRestoAdapter = new RestaurantAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        mRestaurantList = mApi.getRestaurantList();
        mRestoAdapter.setRestoList(mRestaurantList);

        mRecyclerView.setAdapter(mRestoAdapter);
        Log.d(TAG, "initRecyclerView: setAdapter=mRestoAdpater");

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated:enter");
        mRestaurantListViewModel = new ViewModelProvider(requireActivity()).get(RestaurantListViewModel.class);

    }

    private void configureViewModel() {
        Log.d(TAG, "configureViewModel: enter");
        mRestaurantListViewModel.getRestaurantList().observe(getViewLifecycleOwner(), pRestaurantList -> {
            mApi.setRestaurantList(pRestaurantList);
            Log.d(TAG, "configureViewModel: recup liste resto");
            mRestoAdapter.setRestoList(pRestaurantList);
            Log.d(TAG, "configureViewModel: send to adpater the new list");
            mRestoAdapter.notifyDataSetChanged();
            Log.d(TAG, "configureViewModel: notify change to adpater");
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: enter");
        configureViewModel();
    }
}