package com.fleb.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Florence LE BOURNOT on 18/09/2020
 */
public abstract class BaseFragment extends Fragment {

    protected abstract int getFragmentLayout();
    protected abstract void configureFragmentOnCreateView(View pView);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View lView = inflater.inflate(getFragmentLayout(), container, false);
        this.configureFragmentOnCreateView(lView);
        return(lView);
    }

}
