package com.fleb.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.viewmodel.workmatelist.WorkmateAdapter;
import com.fleb.go4lunch.viewmodel.workmatelist.WorkmateListViewModel;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class WorkmateListFragment extends BaseFragment  {

    private WorkmateAdapter mWorkmateAdapter;

    public WorkmateListFragment() {}

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_workmate_list; }

    @Override
    protected void configureFragmentOnCreateView(View pView) {
        RecyclerView lRecyclerView = pView.findViewById(R.id.workmate_list);
        mWorkmateAdapter = new WorkmateAdapter();

        lRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        lRecyclerView.setAdapter(mWorkmateAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WorkmateListViewModel lWorkmateListViewModel = new ViewModelProvider(requireActivity()).get(WorkmateListViewModel.class);
        lWorkmateListViewModel.getWorkmateList().observe(getViewLifecycleOwner(), pWorkmateLists -> {
            mWorkmateAdapter.setWorkmateList(pWorkmateLists);
            mWorkmateAdapter.notifyDataSetChanged();
        });
    }
}