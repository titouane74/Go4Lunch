package com.fleb.go4lunch.ui.home;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.fleb.go4lunch.R;
/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button mBtnLogout;
    private OnListenerLogout mCallback;

    public interface OnListenerLogout {
        void onButtonLogoutClickLogout();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
/*
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
*/

        mBtnLogout = root.findViewById(R.id.button_signout);
        mBtnLogout.setOnClickListener(v -> mCallback.onButtonLogoutClickLogout());

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (HomeFragment.OnListenerLogout) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement onButtonLogoutClickLogout");
        }
    }
}