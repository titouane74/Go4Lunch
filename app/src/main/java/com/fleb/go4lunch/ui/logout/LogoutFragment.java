package com.fleb.go4lunch.ui.logout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.main.MainActivity;

import java.util.Arrays;


/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class LogoutFragment extends Fragment {

/*    private LogoutViewModel mLogoutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mLogoutViewModel =
                ViewModelProviders.of(this).get(LogoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        final TextView textView = root.findViewById(R.id.text_logout);
        mLogoutViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }*/


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) this.getActivity()).signOutFromFirebase();
    }


}
