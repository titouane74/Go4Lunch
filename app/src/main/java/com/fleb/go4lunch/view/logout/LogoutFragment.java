package com.fleb.go4lunch.view.logout;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fleb.go4lunch.view.MainActivity;


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
        ((MainActivity) (this.getActivity())).signOutFromFirebase();
    }

}
