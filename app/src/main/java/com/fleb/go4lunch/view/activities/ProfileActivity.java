package com.fleb.go4lunch.view.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.fleb.go4lunch.R;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by Florence LE BOURNOT on 04/07/2020
 */
public class ProfileActivity extends BaseActivity {

    private static final int SIGN_OUT_TASK = 10;
    private static final int DELETE_USER_TASK = 20;
    private static final int UPDATE_USERNAME = 30;

    private Toast mToast;

    @Override
    public int getActivityLayout() {return R.layout.activity_profile;}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        Button btnUpdate;
        Button btnSignOut;
        Button btnDelete;


        btnUpdate = findViewById(R.id.profile_activity_button_update);
        btnSignOut = findViewById(R.id.profile_activity_button_sign_out);
        btnDelete = findViewById(R.id.profile_activity_button_delete);

        //TODO : to implement
        btnUpdate.setOnClickListener(view -> {
            mToast = Toast.makeText(this,R.string.to_do,Toast.LENGTH_SHORT);
            mToast.show();
        });
        //TODO : to implement
        btnSignOut.setOnClickListener(view -> {
            mToast = Toast.makeText(this,R.string.to_do,Toast.LENGTH_SHORT);
            mToast.show();
        });
        //TODO : to implement
        btnDelete.setOnClickListener(view -> {
            mToast = Toast.makeText(this,R.string.to_do,Toast.LENGTH_SHORT);
            mToast.show();
        });
    }

    // --------------------
    // REST REQUESTS
    // --------------------

    private void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    //TODO : progressBar : see if the username update is allowed
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return aVoid -> {
//                ProgressBar progressBar;
//                progressBar = findViewById(R.id.profile_activity_progress_bar);
            switch (origin){
//                    case UPDATE_USERNAME:
//                        progressBar.setVisibility(View.INVISIBLE);
//                        break;
                case SIGN_OUT_TASK:
                    finish();
                    break;
                case DELETE_USER_TASK:
                    finish();
                    break;
                default:
                    break;
            }
        };
    }

}
