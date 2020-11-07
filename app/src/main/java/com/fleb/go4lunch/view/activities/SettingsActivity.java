package com.fleb.go4lunch.view.activities;

import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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

import com.fleb.go4lunch.viewmodel.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG_USER = "TAG_USER";
    private FirebaseUser mCurrentUser;

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mWorkmateRef = mDb.collection(String.valueOf(Workmate.Fields.Workmate));

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

        displayWorkmateData(mCurrentUser);

        Button lBtnUpdate = findViewById(R.id.user_btn_update);

        lBtnUpdate.setOnClickListener(this);

        SettingsViewModel lSettingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        TextView textView = findViewById(R.id.txt_setting_notif_status);
        lSettingsViewModel.getTextNotifStatus(this).observe(this, textView::setText);

        Button lBtnChangeNotifStatus = findViewById(R.id.btn_change_notif_status);

        lBtnChangeNotifStatus.setOnClickListener(v -> openNotificationSettingsForApp());

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.user_btn_update) {
            updateWorkmate(mCurrentUser);
        }
    }

    public void displayWorkmateData(FirebaseUser pCurrentWorkmate) {

        mWorkmateRef.document(pCurrentWorkmate.getUid())
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Log.d(TAG_USER, "onComplete: SUCCESSFUL" );
                        DocumentSnapshot pDocSnap = pTask.getResult();
                        if (Objects.requireNonNull(pDocSnap).exists()) {
                            Log.d(TAG_USER, "onComplete: SUCCESSFUL and EXIST" );
                            Workmate lWorkmate = pDocSnap.toObject(Workmate.class);
                            String lPhotoUrl = Objects.requireNonNull(lWorkmate).getWorkmatePhotoUrl();
                            if (lPhotoUrl != null) {
                                Glide.with(SettingsActivity.this).load(lPhotoUrl).apply(RequestOptions.circleCropTransform()).into(mImgUser);
                            }
                            mTxtName.setText(lWorkmate.getWorkmateName());
                            mTxtEmail.setText(lWorkmate.getWorkmateEmail());
                        } else {
                            Log.d(TAG_USER, "onComplete: SUCCESSFUL and NOT EXIST" );
                        }
                    } else {
                        Log.d(TAG_USER, "onComplete: NOT SUCCESSFUL" );
                    }
                })
                .addOnFailureListener(pE -> Log.d(TAG_USER, "onComplete: FAILURE" ));

    }

    public void updateWorkmate(FirebaseUser pCurrentWorkmate) {

        String lWorkmateName = mTxtName.getText().toString();

        Map<String, Object> lWorkmate = new HashMap<>();
        lWorkmate.put(String.valueOf(Workmate.Fields.workmateName), lWorkmateName);

        mWorkmateRef.document( pCurrentWorkmate.getUid())
                .update(lWorkmate)
                .addOnSuccessListener(pVoid -> Log.d(TAG_USER, "onSuccess: Document updated " ))
                .addOnFailureListener(pE -> Log.d(TAG_USER, "onFailure: Document not updated", pE));
        Toast.makeText(this, R.string.user_account_updated, Toast.LENGTH_SHORT).show();
        finish();
    }

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