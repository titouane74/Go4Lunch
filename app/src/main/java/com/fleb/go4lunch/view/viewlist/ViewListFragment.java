package com.fleb.go4lunch.view.viewlist;

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

import com.fleb.go4lunch.R;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class ViewListFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewListViewModel lViewListViewModel = ViewModelProviders.of(this).get(ViewListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_view_list, container, false);

        final TextView textView = root.findViewById(R.id.text_view_list);
        lViewListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}