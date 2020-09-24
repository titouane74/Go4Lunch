package com.fleb.go4lunch.view.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

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
        LunchViewModel lLunchViewModel = new ViewModelProvider(requireActivity()).get(LunchViewModel.class);

        final TextView textView = pView.findViewById(R.id.text_lunch);
        lLunchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
    }

}