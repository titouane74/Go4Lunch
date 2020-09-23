package com.fleb.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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


public class WorkmateListFragment extends Fragment  {

    private RecyclerView mRecyclerView;
    private WorkmateListViewModel mWorkmateListViewModel;
    private WorkmateAdapter mWorkmateAdapter;

    public WorkmateListFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View lView = inflater.inflate(R.layout.fragment_workmate_list, container, false);

        return lView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.workmate_list);
        mWorkmateAdapter = new WorkmateAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        //mRecyclerView.setHasFixedSize(true);
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