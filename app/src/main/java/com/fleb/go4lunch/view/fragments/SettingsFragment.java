package com.fleb.go4lunch.view.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.viewmodel.SettingsViewModel;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class SettingsFragment extends BaseFragment {

    private SettingsViewModel mSettingsViewModel;

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_settings; }

    @Override
    protected void configureFragmentOnCreateView(View pView) {
        mSettingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        final TextView textView = pView.findViewById(R.id.text_settings);
        mSettingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
    }

}