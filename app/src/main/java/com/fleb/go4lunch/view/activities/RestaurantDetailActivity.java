package com.fleb.go4lunch.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.fleb.go4lunch.utils.ActionStatus;
import com.fleb.go4lunch.viewmodel.RestaurantDetailViewModel;
import com.fleb.go4lunch.viewmodel.factory.Go4LunchViewModelFactory;
import com.fleb.go4lunch.viewmodel.injection.Injection;
import com.fleb.go4lunch.view.adapters.RestaurantDetailWorkmateAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.fleb.go4lunch.AppGo4Lunch.sApi;

public class RestaurantDetailActivity extends AppCompatActivity {

    public static final String RESTO_PLACE_ID = "placeid";

    private Restaurant mRestaurant;
    private String mRestaurantId;
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
    private RecyclerView mRecyclerView;

    private RestaurantDetailViewModel mRestaurantDetailViewModel;

    private RestaurantDetailWorkmateAdapter mWorkmateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

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
        mRecyclerView = findViewById(R.id.restaurant_detail_workmate_list);

        getIncomingIntent();

        configureViewModel();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra(RESTO_PLACE_ID)) {
            mRestaurantId = getIntent().getStringExtra(RESTO_PLACE_ID);
            sApi.setRestaurantId(mRestaurantId);
        }
    }

    private void configureViewModel() {
        initializeViewModel();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mWorkmateAdapter = new RestaurantDetailWorkmateAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mWorkmateAdapter);
    }

    private void initializeViewModel() {

        Go4LunchViewModelFactory lFactory = Injection.go4LunchViewModelFactory();
        mRestaurantDetailViewModel = new ViewModelProvider(this, lFactory).get(RestaurantDetailViewModel.class);

        if (mRestaurantId == null) {
            mRestaurantDetailViewModel.getWorkmateData().observe(this, pWorkmate -> {
                mRestaurantId = pWorkmate.getWorkmateRestoChosen().getRestoId();
                getRestaurantDetail();
            });
        } else {
            getRestaurantDetail();
        }
    }

    /**
     * Ask the restaurant detail to Firestore
     */
    private void getRestaurantDetail() {

        mRestaurantDetailViewModel.getRestaurantDetail().observe(this, pRestaurant -> {
            mWorkmateAdapter.setWorkmateList(pRestaurant.getRestoWkList());
            mWorkmateAdapter.notifyDataSetChanged();
            mRestaurant = pRestaurant;
            setInfoRestaurant();
            displayChoiceStatus();
            displayLikeStatus();
        });
    }

    /**
     * Display the restaurant information
     */
    private void setInfoRestaurant() {
        mRestoName.setText(mRestaurant.getRestoName());
        mRestoAddress.setText(mRestaurant.getRestoAddress());

        displayRating(mRestaurant.getRestoRating());

        if (mRestaurant.getRestoPhotoUrl() != null) {
            Glide.with(RestaurantDetailActivity.this)
                    .load(mRestaurant.getRestoPhotoUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .into(mRestoImage);
        }

        displayChoiceStatus();

        mRestoImgCall.setOnClickListener(v -> openDialer(mRestaurant.getRestoPhone()));

        mRestoImgWebSite.setOnClickListener(v -> openWebSite(mRestaurant.getRestoWebSite()));

        mRestoLike.setOnClickListener(v -> saveLikeRestaurant());

        mRestoBtnFloatChecked.setOnClickListener(v -> saveChoiceRestaurant());
    }

    /**
     * Save the workmate restaurant choice
     */
    private void saveChoiceRestaurant() {
        mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(ActionStatus.SAVED)
                .observe(this, pActionStatus -> {
                    switch (pActionStatus) {
                        case ADDED:
                            changeChoiceStatus(true);
                            getRestaurantDetail();
                            break;
                        case REMOVED:
                            changeChoiceStatus(false);
                            getRestaurantDetail();
                            break;
                        case ERROR:
                            Toast.makeText(RestaurantDetailActivity.this, getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                            break;
                        case SAVED_FAILED:
                            Toast.makeText(RestaurantDetailActivity.this, getString(R.string.error_saved_failed), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                    }
                });
    }

    /**
     * Display the status of the workmate choice
     */
    private void displayChoiceStatus() {
        if (mRestaurantDetailViewModel == null) {
            initializeViewModel();
        }
        mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(ActionStatus.TO_SEARCH)
                .observe(this, pActionStation -> {
                    if (pActionStation.equals(ActionStatus.IS_CHOSEN)) {
                        changeChoiceStatus(true);
                    } else {
                        changeChoiceStatus(false);
                    }
                });

    }

    /**
     * Chane the status of the workmate choice
     *
     * @param pIsChosen : boolean : the restaurant is chosen or not
     */
    private void changeChoiceStatus(boolean pIsChosen) {
        if (pIsChosen) {
            mRestoBtnFloatChecked.setImageResource(R.drawable.ic_check_circle);
            mRestoBtnFloatChecked.setTag(ActionStatus.IS_CHOSEN);
        } else {
            mRestoBtnFloatChecked.setImageResource(R.drawable.ic_uncheck_circle);
            mRestoBtnFloatChecked.setTag(ActionStatus.NOT_CHOSEN);
        }
    }

    /**
     * Display if the restaurant is liked by the workmate
     */
    private void displayLikeStatus() {
        if (mRestaurantDetailViewModel == null) {
            initializeViewModel();
        }
        mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(ActionStatus.TO_SEARCH)
                .observe(this, pActionStation -> {
                    if (pActionStation.equals(ActionStatus.IS_CHOSEN)) {
                        changeLikeStatus(true);
                    } else {
                        changeLikeStatus(false);
                    }
                });
    }

    /**
     * Save the workmate choice if he likes or not the restaurant
     */
    private void saveLikeRestaurant() {
        mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(ActionStatus.TO_SAVE)
                .observe(this, pActionStatus -> {

                    switch (pActionStatus) {
                        case ADDED:
                            changeLikeStatus(true);
                            break;
                        case REMOVED:
                            changeLikeStatus(false);
                            break;
                        case ERROR:
                            Toast.makeText(RestaurantDetailActivity.this, getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                            break;
                        case SAVED_FAILED:
                            Toast.makeText(RestaurantDetailActivity.this, getString(R.string.error_saved_failed), Toast.LENGTH_SHORT).show();
                            break;
                        default:

                    }
                });

    }

    /**
     * Change the display like status
     *
     * @param pIsChosen : boolean : is liked or not by the workmate
     */
    private void changeLikeStatus(boolean pIsChosen) {
        if (pIsChosen) {
            mRestoLike.setImageResource(R.drawable.ic_like);
            mRestoLike.setTag(ActionStatus.IS_CHOSEN);
        } else {
            mRestoLike.setImageResource(R.drawable.ic_not_like);
            mRestoLike.setTag(ActionStatus.NOT_CHOSEN);
        }
    }

    /**
     * Open the website
     *
     * @param pWebSite : string : url to open
     */
    private void openWebSite(String pWebSite) {
        if (pWebSite != null) {
            Intent lIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pWebSite));
            startActivity(lIntent);
        } else {
            Toast.makeText(RestaurantDetailActivity.this, getString(R.string.text_no_web_site), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Open the dialer
     *
     * @param pPhone : string : phone number to display
     */
    private void openDialer(String pPhone) {
        if ((pPhone != null) && (pPhone.trim().length() > 0)) {
            Intent lIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(pPhone)));
            startActivity(lIntent);
        } else {
            Toast.makeText(RestaurantDetailActivity.this, getString(R.string.text_no_phone_number), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Display the rating with stars
     *
     * @param pRating : double : rating of the restaurant
     */
    private void displayRating(double pRating) {
        int lnbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay(pRating);
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