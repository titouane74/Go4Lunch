package com.fleb.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.viewmodel.workmatelist.WorkmateAdapter;
import com.fleb.go4lunch.viewmodel.workmatelist.WorkmateListViewModel;

import java.util.List;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class WorkmateListFragment extends BaseFragment  {

    private RecyclerView mRecyclerView;
    private WorkmateListViewModel mWorkmateListViewModel;
    private WorkmateAdapter mWorkmateAdapter;

    public WorkmateListFragment() {}

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_workmate_list; }

    @Override
    protected void configureFragmentOnCreateView(View pView) {
        mRecyclerView = pView.findViewById(R.id.workmate_list);
        mWorkmateAdapter = new WorkmateAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mWorkmateAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mWorkmateListViewModel = new ViewModelProvider(getActivity()).get(WorkmateListViewModel.class);
        mWorkmateListViewModel.getWorkmateList().observe(getViewLifecycleOwner(), new Observer<List<Workmate>>() {
            @Override
            public void onChanged(List<Workmate> pWorkmateLists) {
                mWorkmateAdapter.setWorkmateList(pWorkmateLists);
                mWorkmateAdapter.notifyDataSetChanged();
            }
        });
    }
}