package com.fleb.go4lunch.view.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.viewmodel.LunchViewModel;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class LunchFragment extends BaseFragment {

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_lunch; }

    @Override
    protected void configureFragmentOnCreateView(View pView) {
        LunchViewModel lLunchViewModel = ViewModelProviders.of(this).get(LunchViewModel.class);

        final TextView textView = pView.findViewById(R.id.text_lunch);
        lLunchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
    }

}