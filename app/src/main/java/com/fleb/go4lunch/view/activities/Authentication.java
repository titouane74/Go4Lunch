package com.fleb.go4lunch.view.activities;

import android.os.Bundle;
import android.view.View;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.base.BaseActivity;

public class Authentication extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getFragmentLayout());
    }

    @Override
    public int getFragmentLayout() { return R.layout.activity_authentication; }

    /**
     * Called when a view has been clicked.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}