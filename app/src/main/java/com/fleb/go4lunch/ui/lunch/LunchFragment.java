package com.fleb.go4lunch.ui.lunch;
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

import com.fleb.go4lunch.R;
/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class LunchFragment extends Fragment {

//    Button mBtnLogout;
//    private OnListenerLogout mCallback;

//    public interface OnListenerLogout {
//        void onButtonLogoutClickLogout();
//    }

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


//        mBtnLogout = root.findViewById(R.id.button_signout);
//        mBtnLogout.setOnClickListener(v -> mCallback.onButtonLogoutClickLogout());

        return root;
    }

/*
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (LunchFragment.OnListenerLogout) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement onButtonLogoutClickLogout");
        }
    }
*/
}