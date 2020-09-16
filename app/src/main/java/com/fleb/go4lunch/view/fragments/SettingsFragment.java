package com.fleb.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.viewmodel.SettingsViewModel;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class SettingsFragment extends Fragment {

    private SettingsViewModel mSettingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mSettingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final TextView textView = root.findViewById(R.id.text_settings);
        mSettingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}