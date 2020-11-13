package com.fleb.go4lunch.view.activities;

import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Workmate;

import com.fleb.go4lunch.utils.ActionStatus;
import com.fleb.go4lunch.viewmodel.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser mCurrentUser;

    private SettingsViewModel mSettingsViewModel;
    private Workmate mWorkmate;

    private ImageView mImgUser;
    private TextView mTxtEmail;
    private EditText mTxtName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FirebaseAuth lAuth = FirebaseAuth.getInstance();
        mCurrentUser = lAuth.getCurrentUser();

        mImgUser = findViewById(R.id.user_img_user);
        mTxtEmail = findViewById(R.id.user_txt_email);
        mTxtName = findViewById(R.id.user_edit_text_username);
        Button lBtnUpdate = findViewById(R.id.user_btn_update);
        TextView lNotificationStatus = findViewById(R.id.txt_setting_notif_status);
        Button lBtnChangeNotifStatus = findViewById(R.id.btn_change_notif_status);

        mSettingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        displayWorkmateData();

        lBtnUpdate.setOnClickListener(this);

        mSettingsViewModel.getNotificationStatus(this).observe(this, lNotificationStatus::setText);

        lBtnChangeNotifStatus.setOnClickListener(v -> openNotificationSettingsForApp());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.user_btn_update) {
            updateWorkmate();
        }
    }

    /**
     * Display the workmate information
     */
    public void displayWorkmateData() {

        mSettingsViewModel.getWorkmateData(mCurrentUser.getUid()).observe(this, pWorkmate -> {
            mWorkmate = pWorkmate;
            if (pWorkmate.getWorkmatePhotoUrl() != null) {
                Glide.with(SettingsActivity.this)
                        .load(pWorkmate.getWorkmatePhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImgUser);
            }
            mTxtName.setText(pWorkmate.getWorkmateName());
            mTxtEmail.setText(pWorkmate.getWorkmateEmail());
        });

    }

    /**
     * Update the workmate in Firestore
     */
    public void updateWorkmate() {

        String lWorkmateName = mTxtName.getText().toString();

        mSettingsViewModel.updateWorkmateUserName(mWorkmate, lWorkmateName) .observe(this, pActionStatus -> {
            if (pActionStatus.equals(ActionStatus.SAVED)) {
                Toast.makeText(this, R.string.user_account_updated, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.error_on_save, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Open the default system window for the notification parameter
     */
    private void openNotificationSettingsForApp() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, this.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", this.getPackageName());
            intent.putExtra("app_uid", this.getApplicationInfo().uid);
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
        }

        startActivity(intent);
    }

}