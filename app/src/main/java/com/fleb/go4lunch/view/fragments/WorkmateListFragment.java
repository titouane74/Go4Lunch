package com.fleb.go4lunch.view.fragments;

import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.view.adapters.WorkmateAdapter;
import com.fleb.go4lunch.viewmodel.Go4LunchViewModel;
import com.fleb.go4lunch.viewmodel.Go4LunchViewModelFactory;
import com.fleb.go4lunch.viewmodel.Injection;
import com.fleb.go4lunch.viewmodel.WorkmateListViewModel;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class WorkmateListFragment extends BaseFragment  {

    private WorkmateAdapter mWorkmateAdapter;
    private RecyclerView mRecyclerView;

    private Go4LunchViewModel mGo4LunchViewModel;

    public WorkmateListFragment() {}

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_workmate_list; }

    @Override
    protected void configureFragmentOnCreateView(View pView) {
        mRecyclerView = pView.findViewById(R.id.workmate_list);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mWorkmateAdapter = new WorkmateAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mWorkmateAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        configureViewModel();
    }

    private void configureViewModel() {


        Go4LunchViewModelFactory lFactory = Injection.go4LunchViewModelFactory();
        mGo4LunchViewModel = new ViewModelProvider(requireActivity(),lFactory).get(Go4LunchViewModel.class);

//        WorkmateListViewModel lWorkmateListViewModel = new ViewModelProvider(requireActivity()).get(WorkmateListViewModel.class);
//        lWorkmateListViewModel.getWorkmateList().observe(getViewLifecycleOwner(), pWorkmateList -> {
            mGo4LunchViewModel.getWorkmateList().observe(getViewLifecycleOwner(), pWorkmateList -> {
            mWorkmateAdapter.setWorkmateList(pWorkmateList);
            mWorkmateAdapter.notifyDataSetChanged();
        });
    }
}