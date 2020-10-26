package com.fleb.go4lunch.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.service.Go4LunchApi;
import com.fleb.go4lunch.utils.GsonHelper;
import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.fleb.go4lunch.utils.ActionStatus;
import com.fleb.go4lunch.viewmodel.restaurantdetail.RestaurantDetailViewModel;
import com.fleb.go4lunch.viewmodel.restaurantdetail.RestaurantDetailViweModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RestaurantDetailActivity extends AppCompatActivity {

    private static final String TAG = "TAG_DETAIL_RESTO";
    private Restaurant mRestaurant;
    private TextView mRestoName;
    private TextView mRestoAddress;
    private ImageView mRestoNote1;
    private ImageView mRestoNote2;
    private ImageView mRestoNote3;
    private ImageView mRestoImage;
    private ImageView mRestoImgCall;
    private ImageView mRestoImgWebSite;
    private ImageView mRestoLike;
    private FloatingActionButton mRestoBtnFloatChecked;

    private RestaurantDetailViewModel mRestaurantDetailViewModel;
    private Workmate mWorkmate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Go4LunchApi lApi = DI.getGo4LunchApiService();

        mRestoName = findViewById(R.id.text_restaurant_detail_name);
        mRestoNote1 = findViewById(R.id.img_detail_note1);
        mRestoNote2 = findViewById(R.id.img_detail_note2);
        mRestoNote3 = findViewById(R.id.img_detail_note3);
        mRestoAddress = findViewById(R.id.text_restaurant_detail_address);
        mRestoImage = findViewById(R.id.img_restaurant_detail);
        mRestoBtnFloatChecked = findViewById(R.id.btn_restaurant_detail_float_favorite);
        mRestoImgCall = findViewById(R.id.img_call);
        mRestoImgWebSite = findViewById(R.id.img_website);
        mRestoLike = findViewById(R.id.img_like);

        mWorkmate = lApi.getWorkmate();

        getIncomingIntent();
        configureViewModel();

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("placeid") && (getIntent().hasExtra("restaurant"))) {
            mRestaurant = GsonHelper.getGsonRestaurant(getIntent().getStringExtra("restaurant"));

            setInfoRestaurant(mRestaurant);
        }
    }

    private void configureViewModel() {

        RestaurantDetailViweModelFactory lFactory = new RestaurantDetailViweModelFactory(mRestaurant);
        mRestaurantDetailViewModel = new ViewModelProvider(this, lFactory).get(RestaurantDetailViewModel.class);

        mRestaurantDetailViewModel.getWorkmateComingInRestaurant().observe(this,pChoicesList ->
        {
            //TODO envoyé les données de retour à l'adapter
            //TODO mettre en place le recyclerview
            Log.d(TAG, "configureViewModel: call liste workmate coming in restaurant size : " + pChoicesList.size());
        });

    }

    private void setInfoRestaurant(Restaurant pRestaurant) {

        mRestoName.setText(pRestaurant.getRestoName());
        mRestoAddress.setText(pRestaurant.getRestoAddress());

        displayRating(pRestaurant.getRestoRating());

        if (pRestaurant.getRestoPhotoUrl() != null) {
            Glide.with(RestaurantDetailActivity.this)
                    .load(pRestaurant.getRestoPhotoUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .into(mRestoImage);
        }

        mRestoImgCall.setOnClickListener(v -> openDialer(pRestaurant.getRestoPhone()));

        mRestoImgWebSite.setOnClickListener(v -> openWebSite(pRestaurant.getRestoWebSite()));

        mRestoLike.setOnClickListener(v -> saveLikeRestaurant(pRestaurant));

        mRestoBtnFloatChecked.setOnClickListener(v -> saveChooseRestaurant(pRestaurant));
    }

    private void saveChooseRestaurant(Restaurant pRestaurant) {


    }
    private void saveLikeRestaurant(Restaurant pRestaurant) {
        mRestaurantDetailViewModel.saveLikeRestaurant(mWorkmate, pRestaurant)
                .observe(this, pSaveMessage -> {
                    if(pSaveMessage.equals(ActionStatus.ADDED)) {
                        Toast.makeText(RestaurantDetailActivity.this,
                                getString(R.string.text_resto_added_to_favorite), Toast.LENGTH_SHORT).show();
                    } else if (pSaveMessage.equals(ActionStatus.REMOVED)) {
                        Toast.makeText(RestaurantDetailActivity.this,
                                getString(R.string.text_resto_removed_from_favorite), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RestaurantDetailActivity.this, String.valueOf(pSaveMessage), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openWebSite(String pWebSite) {
        if (pWebSite != null) {
            Intent lIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pWebSite));
            startActivity(lIntent);
        } else {
            Toast.makeText(RestaurantDetailActivity.this, getString(R.string.text_no_web_site), Toast.LENGTH_SHORT).show();
        }
    }

    private void openDialer(String pPhone) {
        if((pPhone != null) && (pPhone.trim().length()>0)) {
            Intent lIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ Uri.encode(pPhone)));
            startActivity(lIntent);
        } else {
            Toast.makeText(RestaurantDetailActivity.this, getString(R.string.text_no_phone_number), Toast.LENGTH_SHORT).show();
        }
    }

    private void displayRating(double pRating) {
        int lnbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay((getApplicationContext()),pRating);
        switch (lnbStarToDisplay) {
            case 1:
                mRestoNote2.setVisibility(View.INVISIBLE);
                mRestoNote3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mRestoNote3.setVisibility(View.INVISIBLE);
                break;
            case 3:
                break;
            default:
                mRestoNote1.setVisibility(View.INVISIBLE);
                mRestoNote2.setVisibility(View.INVISIBLE);
                mRestoNote3.setVisibility(View.INVISIBLE);
                break;
        }
    }
}