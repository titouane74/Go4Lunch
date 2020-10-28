package com.fleb.go4lunch.repository;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.BuildConfig;
import com.fleb.go4lunch.model.Choice;
import com.fleb.go4lunch.model.FirestoreUpdateFields;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.RestaurantDetailPojo;
import com.fleb.go4lunch.model.RestaurantPojo;
import com.fleb.go4lunch.network.ApiClient;
import com.fleb.go4lunch.network.JsonRetrofitApi;
import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.fleb.go4lunch.utils.PreferencesHelper;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fleb.go4lunch.network.JsonRetrofitApi.BASE_URL_GOOGLE;
import static com.fleb.go4lunch.utils.PreferencesHelper.mPreferences;
import static com.fleb.go4lunch.view.activities.MainActivity.PREF_KEY_PLACE_DETAIL_FIELDS;
import static com.fleb.go4lunch.view.activities.MainActivity.PREF_KEY_RADIUS;
import static com.fleb.go4lunch.view.activities.MainActivity.PREF_KEY_TYPE_GOOGLE_SEARCH;

/**
 * Created by Florence LE BOURNOT on 13/10/2020
 */
public class RestaurantRepository {
    //public static final String TAG = "TAG4_REPO";

    public static final String PREF_KEY_LATITUDE = "PREF_KEY_LATITUDE";
    public static final String PREF_KEY_LONGITUDE = "PREF_KEY_LONGITUDE";

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mRestoRef = mDb.collection(String.valueOf(Restaurant.Fields.Restaurant));
    private CollectionReference mRestoLastUpdRef = mDb.collection(String.valueOf(FirestoreUpdateFields.RestaurantLastUpdate));
    private CollectionReference mChoiceRef = mDb.collection(String.valueOf(Choice.Fields.Choice));
    /**
     * Google  / Retrofit declarations
     */
    private String mKey = BuildConfig.MAPS_API_KEY;
    private JsonRetrofitApi mJsonRetrofitApi;

    /**
     * MutableLiveData Declarations
     */
    private MutableLiveData<List<Restaurant>> mLDRestoList = new MutableLiveData<>();

    private Location mFusedLocationProvider;
    private Date mFirestoreLastUpdate = new Date();
    private List<Restaurant> mRestoListDetail = new ArrayList<>();

    private Double mLatitude;
    private Double mLongitude;

    private Date mDate = new Date();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyyMMdd");
    private String mDateChoice = mSdf.format(mDate);

    private List<Restaurant> mRestaurantList = new ArrayList<>();

    public void saveLocationInSharedPreferences(Location pLocation) {
        PreferencesHelper.saveStringPreferences(PREF_KEY_LATITUDE, String.valueOf(pLocation.getLatitude()));
        PreferencesHelper.saveStringPreferences(PREF_KEY_LONGITUDE, String.valueOf(pLocation.getLongitude()));
    }

    public MutableLiveData<List<Restaurant>> getRestaurantList() {

        mLatitude = Double.valueOf(Objects.requireNonNull(mPreferences.getString(PREF_KEY_LATITUDE, null)));
        mLongitude = Double.parseDouble(Objects.requireNonNull(mPreferences.getString(PREF_KEY_LONGITUDE, null)));
        mFusedLocationProvider = Go4LunchHelper.setCurrentLocation(mLatitude, mLongitude);

        mRestoLastUpdRef.document(String.valueOf(FirestoreUpdateFields.dateLastUpdateListResto))
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        DocumentSnapshot lResult = pTask.getResult();
                        Timestamp lTimestamp;
                        if (lResult.getData() != null) {
                            lTimestamp = (Timestamp) lResult.getData().get(String.valueOf(FirestoreUpdateFields.restoLastUpdateList));
                            if (lTimestamp != null) {
                                Date lDate = new Date();
                                //TODO just for the test at day + 1
                                Calendar lCal = Calendar.getInstance();
                                lCal.setTime(lDate);
                                //lCal.add(Calendar.DATE, 1);
                                lDate = lCal.getTime();

                                mFirestoreLastUpdate = lTimestamp.toDate();

                                Log.d("TAG4_GET_RESTO_LIST", "getRestaurantList: mFirestoreLastUpdate " + mFirestoreLastUpdate);

                                //Compare the only dates (today and firestore date)
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

                                // The dates are different and we are on a monday we retieve informations from Google
                                if ((!sdf.format(mFirestoreLastUpdate).equals(sdf.format(lDate))) && (Go4LunchHelper.getCurrentDayInt() == 2)) {
                                    // 2 = Monday
                                    //TODO for the moment deactivate the google call
                                    //getGoogleRestaurantList();
                                    getFirestoreRestaurantList();

                                } else {
                                    getFirestoreRestaurantList();
                                }
                            }
                        } else {
                            //TODO for the moment deactivate the google call
                            //getGoogleRestaurantList();
                            getFirestoreRestaurantList();
                        }
                    } else {
                        mFirestoreLastUpdate = null;
                        getFirestoreRestaurantList();
                    }
                });
        return mLDRestoList;
    }

    private void getFirestoreRestaurantList() {

        mRestoRef.get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        List<Restaurant> lRestoList = (Objects.requireNonNull(pTask.getResult()).toObjects(Restaurant.class));
                        prepareAndSendRestoListForDisplay(lRestoList);
                    } else {
                        Log.d("TAG4_GET_FIRESTORE", "Error : " + pTask.getException());
                    }
                });
    }

    private void getGoogleRestaurantList() {

        int lProximityRadius = mPreferences.getInt(PREF_KEY_RADIUS, 150);
        String lType = mPreferences.getString(PREF_KEY_TYPE_GOOGLE_SEARCH, "restaurant");

        mJsonRetrofitApi = ApiClient.getClient(BASE_URL_GOOGLE).create(JsonRetrofitApi.class);

        Call<RestaurantPojo> lRestaurantPojoCall = mJsonRetrofitApi.getNearByPlaces(mKey, lType,
                mLatitude + "," + mLongitude, lProximityRadius);

        lRestaurantPojoCall.enqueue(new Callback<RestaurantPojo>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantPojo> call, @NonNull Response<RestaurantPojo> response) {
                if (response.isSuccessful()) {
                    List<RestaurantPojo.Result> lRestoResponse = Objects.requireNonNull(response.body()).getResults();

                    for (RestaurantPojo.Result restaurantPojo : lRestoResponse) {
                        manageRestaurantInformationsAndGetGoogleDetailRestaurant(restaurantPojo, lRestoResponse.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantPojo> call, @NonNull Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }

    public void manageRestaurantInformationsAndGetGoogleDetailRestaurant(RestaurantPojo.Result pRestaurantList, int pResponseSize) {

        String lFields = mPreferences.getString(PREF_KEY_PLACE_DETAIL_FIELDS, null);

        mJsonRetrofitApi = ApiClient.getClient(BASE_URL_GOOGLE).create(JsonRetrofitApi.class);

        Call<RestaurantDetailPojo> lRestaurantDetailPojoCall = mJsonRetrofitApi.getRestaurantDetail(mKey,
                pRestaurantList.getPlaceId(), lFields);

        lRestaurantDetailPojoCall.enqueue(new Callback<RestaurantDetailPojo>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantDetailPojo> call, @NonNull Response<RestaurantDetailPojo> response) {
                if (response.isSuccessful()) {
                    RestaurantDetailPojo.Result lRestoDetResponse = Objects.requireNonNull(response.body()).getResult();

                    //Manage restaurant informations
                    String lPhoto = null;
                    double lRating = 0.0;
                    String lAddress = null;

                    String lPlaceId = pRestaurantList.getPlaceId();
                    String lName = pRestaurantList.getName();
                    if (pRestaurantList.getPhotos() != null && pRestaurantList.getPhotos().size() > 0) {
                        lPhoto = getPhoto(pRestaurantList.getPhotos().get(0).getPhotoReference(), 400, mKey);
                    }
                    RestaurantPojo.Location lLocation = pRestaurantList.getGeometry().getLocation();
                    if (pRestaurantList.getVicinity() != null) {
                        lAddress = pRestaurantList.getVicinity();
                    }
                    if (pRestaurantList.getRating() != null) {
                        lRating = pRestaurantList.getRating();
                    }

                    String lDistance = String.valueOf(Go4LunchHelper.getRestaurantDistanceToCurrentLocation(
                            mFusedLocationProvider, pRestaurantList.getGeometry().getLocation()));

                    Restaurant lRestaurant = new Restaurant(
                            lPlaceId, lName, lAddress, null, null, lDistance, 0,
                            lRating, lPhoto, lLocation, null, 0
                    );

                    lRestaurant.setRestoOpeningHours(lRestoDetResponse.getOpeningHours());
                    lRestaurant.setRestoWebSite(lRestoDetResponse.getWebsite());
                    lRestaurant.setRestoPhone(lRestoDetResponse.getFormattedPhoneNumber());

                    mRestoListDetail.add(lRestaurant);

                    backupDataInFirestore(pResponseSize);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantDetailPojo> call, @NonNull Throwable t) {
                Log.d("TAG4_DETRESTO", "onFailure: " + t.getMessage());
            }
        });


    }

    private void backupDataInFirestore(int pResponseSize) {
        if (mRestoListDetail.size() == pResponseSize) {
            for (Restaurant lRestaurant : mRestoListDetail) {
                saveRestaurantInFirestore(lRestaurant);
            }
            updateDateOfRestaurantLastUpdateInFirestore();

            prepareAndSendRestoListForDisplay(mRestoListDetail);
        }
    }

    public void saveRestaurantInFirestore(Restaurant pRestaurant) {
        mRestoRef.document(pRestaurant.getRestoPlaceId())
                .get()
                .addOnSuccessListener(pVoid -> mRestoRef.document(pRestaurant.getRestoPlaceId())
                        .set(pRestaurant)
                        .addOnSuccessListener(pDocumentReference ->
                                Log.d("TAG4_SAVE_RESTO", "onSuccess : Document saved "))
                        .addOnFailureListener(pE ->
                                Log.d("TAG4_SAVE_RESTO", "onFailure: Document not saved", pE)))
                .addOnFailureListener(pE -> Log.d("TAG4_SAVE_RESTO", "onFailure Save: Document not saved", pE));
    }

    private void prepareAndSendRestoListForDisplay(List<Restaurant> pRestaurantList) {

        List<Restaurant> lRestaurantList = updateDistanceInLiveDataRestaurantList(pRestaurantList);
        Collections.sort(lRestaurantList);
        Collections.reverse(lRestaurantList);

        updateNbWorkmateLiveDataRestaurantList(lRestaurantList);
        //TODO nombre de workmate à zéro au premier lancement
        // attendre le retour du OnComplete, gestion de l'async
        mLDRestoList.setValue(mRestaurantList);
    }

    private List<Restaurant> updateDistanceInLiveDataRestaurantList(List<Restaurant> pRestaurantList) {
        for (Restaurant lRestaurant : pRestaurantList) {

            int lDistance = Go4LunchHelper.getRestaurantDistanceToCurrentLocation(
                    mFusedLocationProvider, lRestaurant.getRestoLocation());

            String lNewDistance = Go4LunchHelper.convertDistance(lDistance);
            lRestaurant.setRestoDistance(lDistance);
            lRestaurant.setRestoDistanceText(lNewDistance);
        }
        return pRestaurantList;
    }


    private void updateNbWorkmateLiveDataRestaurantList(List<Restaurant> pRestaurantList) {

        for (Restaurant lRestaurant : pRestaurantList) {

            mChoiceRef.whereEqualTo(String.valueOf(Choice.Fields.chRestoPlaceId), lRestaurant.getRestoPlaceId())
                    .whereEqualTo(String.valueOf(Choice.Fields.chChoiceDate), mDateChoice);
            mChoiceRef.get()
                    .addOnCompleteListener(pTask -> {
                        if (pTask.isSuccessful()) {
                            List<Choice> lChoiceList = pTask.getResult().toObjects(Choice.class);
                            for (Choice lChoice : lChoiceList) {
                                if ((lChoice.getChChoiceDate().equals(mDateChoice))
                                        && (lChoice.getChRestoPlaceId().equals(lRestaurant.getRestoPlaceId()))) {
                                    lRestaurant.setRestoNbWorkmates(lRestaurant.getRestoNbWorkmates() + 1);
                                }
                            }
                        }
                    });
        }
    }

    private void updateDateOfRestaurantLastUpdateInFirestore() {

        Date lDate = new Date();
        Map<String, Object> lLastUpdate = new HashMap<>();
        lLastUpdate.put(String.valueOf(FirestoreUpdateFields.restoLastUpdateList), lDate);
        mRestoLastUpdRef.document(String.valueOf(FirestoreUpdateFields.dateLastUpdateListResto))
                .set(lLastUpdate)
                .addOnSuccessListener(pDocumentReference ->
                        Log.d("TAG4_SAVE_DATE", "onSuccess : update date "))
                .addOnFailureListener(pE ->
                        Log.d("TAG4_SAVE_DATE", "onFailure: date not saved", pE));
    }

    public String getPhoto(String pPhotoReference, int pMaxWidth, String pKey) {
        return BASE_URL_GOOGLE + "photo?photoreference=" + pPhotoReference
                + "&maxwidth=" + pMaxWidth + "&key=" + pKey;
    }

}
