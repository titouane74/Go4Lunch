package com.fleb.go4lunch.ui.map;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fleb.go4lunch.R;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class MapFragment extends Fragment {

    Button mBtnLogout;
    private OnListenerLogout mCallback;

    public interface OnListenerLogout {
        void onButtonLogoutClickLogout();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MapViewModel lLunchViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
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
            mCallback = (MapFragment.OnListenerLogout) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement onButtonLogoutClickLogout");
        }
    }
}