package com.fleb.go4lunch.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.utils.GsonHelper;
import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RestaurantDetailActivity extends AppCompatActivity {

    private static final String TAG_DET_RESTO = "TAG_DET_RESTO";

    private Restaurant mRestaurant;
    private TextView mRestoName;
    private TextView mRestoAddress;
    private ImageView mRestoNote1;
    private ImageView mRestoNote2;
    private ImageView mRestoNote3;
    private ImageView mRestoImage;
    private FloatingActionButton mRestoBtnFloatFavorite;
    private CollapsingToolbarLayout mCollapsingToolbarLayout ;
    private Toolbar mToolbarDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        mRestoName = findViewById(R.id.text_restaurant_detail_name);
        mRestoNote1 = findViewById(R.id.img_detail_note1);
        mRestoNote2 = findViewById(R.id.img_detail_note2);
        mRestoNote3 = findViewById(R.id.img_detail_note3);
        mRestoAddress = findViewById(R.id.text_restaurant_detail_address);
        mRestoImage = findViewById(R.id.img_restaurant_detail);
        mRestoBtnFloatFavorite = findViewById(R.id.btn_restaurant_detail_float_favorite);
        mCollapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
//        mToolbarDetail = findViewById(R.id.toolbar_detail);

        getIncomingIntent();

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("placeid") && (getIntent().hasExtra("restaurant")))
        {
            mRestaurant = GsonHelper.getGsonRestaurant(getIntent().getStringExtra("restaurant"));

            setInfoRestaurant(mRestaurant);
        }
    }

    private void setInfoRestaurant(Restaurant pRestaurant) {

        mRestoName.setText(pRestaurant.getRestoName());
        mRestoAddress.setText(pRestaurant.getRestoAddress());

        int lnbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay((getApplicationContext()),
                pRestaurant.getRestoRating());
        switch (lnbStarToDisplay) {
            case 1:
                Log.d(TAG_DET_RESTO, "setInfoRestaurant: 2 stars to hide : " );
                mRestoNote2.setVisibility(View.INVISIBLE);
                mRestoNote3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                Log.d(TAG_DET_RESTO, "setInfoRestaurant: 1 star to hide : " );
                mRestoNote3.setVisibility(View.INVISIBLE);
                break;
            case 3:
                Log.d(TAG_DET_RESTO, "setInfoRestaurant: no star to hide : " );
                break;
            default:
                Log.d(TAG_DET_RESTO, "setInfoRestaurant: 3 stars to hide : " );
                mRestoNote1.setVisibility(View.INVISIBLE);
                mRestoNote2.setVisibility(View.INVISIBLE);
                mRestoNote3.setVisibility(View.INVISIBLE);
                break;
        }

        if (pRestaurant.getRestoPhotoUrl() != null ) {
            Glide.with(RestaurantDetailActivity.this)
                    .load(pRestaurant.getRestoPhotoUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .into(mRestoImage);
        }

    }
}