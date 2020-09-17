package com.fleb.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.viewmodel.LunchViewModel;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class LunchFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LunchViewModel lLunchViewModel = ViewModelProviders.of(this).get(LunchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lunch, container, false);

        final TextView textView = root.findViewById(R.id.text_lunch);
        lLunchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }
}