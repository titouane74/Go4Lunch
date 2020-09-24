package com.fleb.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.view.activities.MainActivity;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class LogoutFragment extends BaseFragment {

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_maps; }

    @Override
    protected void configureFragmentOnCreateView(View pView) {    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) (this.requireActivity())).signOutFromFirebase();
    }

}
