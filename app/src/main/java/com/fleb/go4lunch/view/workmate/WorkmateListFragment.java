package com.fleb.go4lunch.view.workmate;

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


public class WorkmateListFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkmateListViewModel lWorkmateListViewModel = ViewModelProviders.of(this).get(WorkmateListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_workmate_list, container, false);

/*
        final TextView textView = root.findViewById(R.id.text_workmates);
        lWorkmateViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) { textView.setText(s); }
        });
*/
        return root;
    }

}