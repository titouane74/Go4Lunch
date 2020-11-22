package com.fleb.go4lunch.repository;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.BuildConfig;
import com.fleb.go4lunch.model.FirestoreUpdateFields;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.RestaurantDetailPojo;
import com.fleb.go4lunch.model.RestaurantPojo;
import com.fleb.go4lunch.network.ApiClient;
import com.fleb.go4lunch.network.JsonRetrofitApi;
import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fleb.go4lunch.AppGo4Lunch.ERROR_ON_FAILURE_LISTENER;
import static com.fleb.go4lunch.AppGo4Lunch.PREF_KEY_BOUND_RADIUS;
import static com.fleb.go4lunch.AppGo4Lunch.PREF_KEY_PLACE_DETAIL_FIELDS;
import static com.fleb.go4lunch.AppGo4Lunch.PREF_KEY_RADIUS;
import static com.fleb.go4lunch.AppGo4Lunch.PREF_KEY_TYPE_GOOGLE_SEARCH;
import static com.fleb.go4lunch.AppGo4Lunch.sApi;
import static com.fleb.go4lunch.network.JsonRetrofitApi.BASE_URL_GOOGLE;
import static com.fleb.go4lunch.network.JsonRetrofitApi.TXT_KEY_GOOGLE;
import static com.fleb.go4lunch.network.JsonRetrofitApi.TXT_MAX_WIDTH_GOOGLE;
import static com.fleb.go4lunch.network.JsonRetrofitApi.TXT_PHOTO_REF_GOOGLE;
import static com.fleb.go4lunch.service.Go4LunchApiService.PREF_KEY_LATITUDE;
import static com.fleb.go4lunch.service.Go4LunchApiService.PREF_KEY_LONGITUDE;
import static com.fleb.go4lunch.utils.PreferencesHelper.mPreferences;


/**
 * Created by Florence LE BOURNOT on 13/10/2020
 */
public class RestaurantRepository {

    public static final String TAG = "TAG_REPO_RESTO";

    public static final String RESTAURANT = "RESTAURANT";
    public static final String TYPE_DEF_VALUE = "restaurant";

    /**
     * Firebase declarations
     */
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private final CollectionReference mRestoRef = mDb.collection(String.valueOf(Restaurant.Fields.Restaurant));
    private final CollectionReference mRestoLastUpdRef = mDb.collection(String.valueOf(FirestoreUpdateFields.RestaurantLastUpdate));
    /**
     * Google  / Retrofit declarations
     */
    private final String mKey = BuildConfig.MAPS_API_KEY;
    private JsonRetrofitApi mJsonRetrofitApi;

    /**
     * MutableLiveData Declarations
     */
    private final MutableLiveData<List<Restaurant>> mLDRestoList = new MutableLiveData<>();
    private final MutableLiveData<Restaurant> mLDResto = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurant>> mLDAutocompleteRestoList = new MutableLiveData<>();

    private Date mFirestoreLastUpdate = new Date();

    private Location mFusedLocationProvider;
    private Double mLatitude;
    private Double mLongitude;
    private boolean mIsFromAutoComplete = false;

    private List<Restaurant> mRestaurantList = new ArrayList<>();
    private List<Restaurant> mRestoListDetail = new ArrayList<>();

    /**
     * Get from Firestore the restaurant detail
     * @return : mutable live data object : restaurant information
     */
        public MutableLiveData<Restaurant> getLDRestaurantDetail() {
        String lRestaurantId = sApi.getRestaurantId();
        mRestoRef.document(lRestaurantId)
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Restaurant lRestaurant = pTask.getResult().toObject(Restaurant.class);
                        sApi.setRestaurant(lRestaurant);
                        mLDResto.setValue(lRestaurant);
                    }
                })
                .addOnFailureListener(pE -> Log.e(TAG, ERROR_ON_FAILURE_LISTENER + pE));
        return mLDResto;
    }

    /**
     * Manage the recovery of the restaurant list
     * if it's the first start of the application the data are retrieved from Google and Firestore is updated
     * if it's the first connection on a monday, the data are retrieved from Google and Firestore is updated
     * (the Firestore last update date is before now)
     * in the other case, the data are retrieved from Firestore
     * @return : mutable live data list object : list of the restaurants
     */
    public MutableLiveData<List<Restaurant>> getLDRestaurantList() {
        mIsFromAutoComplete = false;
        mLatitude = sApi.getLocationFromSharedPreferences(PREF_KEY_LATITUDE);
        mLongitude = sApi.getLocationFromSharedPreferences(PREF_KEY_LONGITUDE);
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
                                Calendar lCal = Calendar.getInstance();
                                lCal.setTime(lDate);
                                lDate = lCal.getTime();

                                mFirestoreLastUpdate = lTimestamp.toDate();

                                //Compare the only dates (today and firestore date)
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

                                // The dates are different and we are on a monday we retrieve information from Google
                                if ((!sdf.format(mFirestoreLastUpdate).equals(sdf.format(lDate))) && (Go4LunchHelper.getCurrentDayInt() == 2)) {
                                    // 2 = Monday
                                    getGoogleRestaurantList();
                                } else {
                                    getFirestoreRestaurantList();
                                }
                            }
                        } else {
                            getGoogleRestaurantList();
                        }
                    } else {
                        mFirestoreLastUpdate = null;
                        getFirestoreRestaurantList();
                    }
                });
        return mLDRestoList;
    }

    /**
     * Get the restaurant list from Firestore
     */
    private void getFirestoreRestaurantList() {

        mRestoRef.get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        List<Restaurant> lRestoList = (Objects.requireNonNull(pTask.getResult()).toObjects(Restaurant.class));
                        prepareAndSendRestoListForDisplay(lRestoList);
                    }
                })
                .addOnFailureListener(pE -> Log.e(TAG, ERROR_ON_FAILURE_LISTENER + pE));
    }

    /**
     * Get the restaurant list from Google
     */
    private void getGoogleRestaurantList() {

        int lProximityRadius = mPreferences.getInt(PREF_KEY_RADIUS, 150);
        String lType = mPreferences.getString(PREF_KEY_TYPE_GOOGLE_SEARCH, TYPE_DEF_VALUE);

        mJsonRetrofitApi = ApiClient.getClient(BASE_URL_GOOGLE).create(JsonRetrofitApi.class);

        Call<RestaurantPojo> lRestaurantPojoCall = mJsonRetrofitApi.getNearByPlaces(mKey, lType,
                mLatitude + "," + mLongitude, lProximityRadius);

        lRestaurantPojoCall.enqueue(new Callback<RestaurantPojo>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantPojo> call, @NonNull Response<RestaurantPojo> response) {
                if (response.isSuccessful()) {
                    List<RestaurantPojo.Result> lRestoResponse = Objects.requireNonNull(response.body()).getResults();

                    for (RestaurantPojo.Result restaurantPojo : lRestoResponse) {
                        getGoogleDetailRestaurant(restaurantPojo.getPlaceId(), lRestoResponse.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantPojo> call, @NonNull Throwable t) {
                Log.e(TAG, ERROR_ON_FAILURE_LISTENER + t.toString());
            }
        });
    }

    /**
     * Get the restaurant detail information from Google
     * @param pRestaurantListId : string : id of the restaurant
     * @param pResponseSize : int : size of the restaurant list
     */
    public void getGoogleDetailRestaurant(String pRestaurantListId, int pResponseSize) {
        String lFields = mPreferences.getString(PREF_KEY_PLACE_DETAIL_FIELDS, null);

        mJsonRetrofitApi = ApiClient.getClient(BASE_URL_GOOGLE).create(JsonRetrofitApi.class);

        Call<RestaurantDetailPojo> lRestaurantDetailPojoCall = mJsonRetrofitApi.getRestaurantDetail(mKey,
                pRestaurantListId, lFields);

        lRestaurantDetailPojoCall.enqueue(new Callback<RestaurantDetailPojo>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantDetailPojo> call, @NonNull Response<RestaurantDetailPojo> response) {
                if (response.isSuccessful()) {
                    RestaurantDetailPojo.Result lRestoDetResponse = Objects.requireNonNull(response.body()).getResult();

                    //Manage restaurant information
                    String lPhoto = null;
                    double lRating = 0.0;
                    String lAddress = null;
                    RestaurantDetailPojo.Location lLocation = null;
                    String lName = lRestoDetResponse.getName();

                    if (lRestoDetResponse.getPhotos() != null && lRestoDetResponse.getPhotos().size() > 0) {
                        lPhoto = getPhoto(lRestoDetResponse.getPhotos().get(0).getPhotoReference(), 400, mKey);
                    }
                    if (lRestoDetResponse.getGeometry().getLocation() != null) {
                        lLocation = lRestoDetResponse.getGeometry().getLocation();
                    }
                    if (lRestoDetResponse.getVicinity() != null) {
                        lAddress = lRestoDetResponse.getVicinity();
                    }
                    if (lRestoDetResponse.getRating() != null) {
                        lRating = lRestoDetResponse.getRating();
                    }

                    Restaurant lRestaurant = new Restaurant(
                            pRestaurantListId, lName, lAddress, null, null, null,
                            lRating, lPhoto, lLocation, null, 0, null
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
                Log.e(TAG, ERROR_ON_FAILURE_LISTENER + t.getMessage());
            }
        });
    }

    /**
     * Backup the restaurant list in Firestore if all the restaurant data have been retrieved
     * @param pResponseSize : int : size of the restaurant list retrieve from Google
     */
    private void backupDataInFirestore(int pResponseSize) {

        if (mRestoListDetail.size() == pResponseSize) {
            for (Restaurant lRestaurant : mRestoListDetail) {
                saveRestaurantInFirestore(lRestaurant);
            }
            updateLastUpdateDateInFirestore();

            prepareAndSendRestoListForDisplay(mRestoListDetail);
        }
    }

    /**
     * Save the restaurant in Firestore
     * @param pRestaurant : object : restaurant to be saved
     */
    public void saveRestaurantInFirestore(Restaurant pRestaurant) {

        mRestoRef.document(pRestaurant.getRestoPlaceId())
                .get()
                .addOnSuccessListener(pDocumentSnapshot ->
                        mRestoRef.document(pRestaurant.getRestoPlaceId())
                                .set(pRestaurant)
                                .addOnSuccessListener(pDocumentReference ->
                                        Log.d(TAG, "onSuccess : Document saved " + pRestaurant.getRestoName()))
                                .addOnFailureListener(pE ->
                                        Log.e(TAG, ERROR_ON_FAILURE_LISTENER + pE)))
                .addOnFailureListener(pE -> Log.e(TAG, ERROR_ON_FAILURE_LISTENER + pE));
    }

    /**
     * Prepare the restaurant list to be display and send it back to the view model
     * @param pRestaurantList : list object : restaurant list to be prepared
     */
    private void prepareAndSendRestoListForDisplay(List<Restaurant> pRestaurantList) {

        mRestaurantList = updateDistanceInRestaurantList(pRestaurantList);
        Collections.sort(mRestaurantList);
        Collections.reverse(mRestaurantList);
        if (!mIsFromAutoComplete) {
            removeRestaurantOutOfRadiusFromList(mRestaurantList);
        }
        sApi.setRestaurantList(mRestaurantList);

        if (mIsFromAutoComplete) {
            mLDAutocompleteRestoList.setValue(mRestaurantList);
        } else {
            mLDRestoList.setValue(mRestaurantList);
        }
    }

    /**
     * Remove the restaurant which have a distance above the radius
     * @param pRestaurantList : list object : restaurant list
     */
    private void removeRestaurantOutOfRadiusFromList(List<Restaurant> pRestaurantList) {
        int lProximityRadius = mPreferences.getInt(PREF_KEY_RADIUS, 150);
        try {
            for (Iterator<Restaurant> lRestaurantIterator = mRestaurantList.iterator(); lRestaurantIterator.hasNext(); ) {
                Restaurant lRestaurant = lRestaurantIterator.next();
                if (lRestaurant.getRestoDistance() > lProximityRadius) {
                    lRestaurantIterator.remove();
                }
            }
        } catch (java.util.ConcurrentModificationException exception) {
            Log.e(TAG, "removeRestaurantOutOfRadiusFromList: " + exception);
        }

        mRestaurantList = pRestaurantList;
    }

    /**
     * Update in the LiveData the distance of the restaurant
     * @param pRestaurantList : list object: restaurant list
     * @return : list object : restaurant list
     */
    private List<Restaurant> updateDistanceInRestaurantList(List<Restaurant> pRestaurantList) {
        for (Restaurant lRestaurant : pRestaurantList) {
            int lDistance = Go4LunchHelper.getRestaurantDistanceToCurrentLocation(
                    mFusedLocationProvider, lRestaurant.getRestoLocation());
            String lNewDistance = Go4LunchHelper.convertDistance(lDistance);
            lRestaurant.setRestoDistance(lDistance);
            lRestaurant.setRestoDistanceText(lNewDistance);
        }
        return pRestaurantList;
    }

    /**
     * Update the last update date of and in Firestore
     */
    private void updateLastUpdateDateInFirestore() {

        Date lDate = new Date();
        Map<String, Object> lLastUpdate = new HashMap<>();
        lLastUpdate.put(String.valueOf(FirestoreUpdateFields.restoLastUpdateList), lDate);
        mRestoLastUpdRef.document(String.valueOf(FirestoreUpdateFields.dateLastUpdateListResto))
                .set(lLastUpdate)
                .addOnSuccessListener(pDocumentReference ->
                        Log.d(TAG, "onSuccess : date updated "))
                .addOnFailureListener(pE ->
                        Log.e(TAG, ERROR_ON_FAILURE_LISTENER + pE));
    }

    /**
     * Get the restaurant list for the autocomplete prediction request
     * @param pPlacesClient : object : placesClient
     * @param pQuery : strung : query to be put in the request to autocomplete
     * @return : mutable live data list object : restaurant list
     */
    public MutableLiveData<List<Restaurant>> getLDAutocompleteRestaurantList(PlacesClient pPlacesClient, String pQuery) {

        mIsFromAutoComplete = true;

        manageAutocomplete(pPlacesClient, pQuery);

        return mLDAutocompleteRestoList;
    }

    /**
     * Manage the autocomplete prediction: generate the request, get and filter the result
     * Call also the data from Firestore to be compared if all the information are already stored
     * @param pPlacesClient : object : placesClient
     * @param pQuery : string : query to be put in the request to autocomplete
     */
    private void manageAutocomplete(PlacesClient pPlacesClient, String pQuery) {
        double lLat = sApi.getLocation().getLatitude();
        double lLng = sApi.getLocation().getLongitude();
        LatLng lLatLng = new LatLng(lLat, lLng);

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        int lBoundRadius = mPreferences.getInt(PREF_KEY_BOUND_RADIUS, 1000);
        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                Go4LunchHelper.toBounds(lLatLng, lBoundRadius).southwest,
                Go4LunchHelper.toBounds(lLatLng, lBoundRadius).northeast);

        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationRestriction(bounds)
                .setOrigin(new LatLng(lLat, lLng))
                .setCountries("FR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setSessionToken(token)
                .setQuery(pQuery)
                .build();

        List<Restaurant> lRestaurantList = new ArrayList<>();
        List<String> lRestaurantsId = new ArrayList<>();

        pPlacesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {

            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Restaurant lRestaurant = new Restaurant(prediction.getPlaceId(), prediction.getPrimaryText(null).toString());
                if (prediction.getPlaceTypes().size() > 0) {
                    for (Place.Type lPlace : prediction.getPlaceTypes()) {
                        if (lPlace.toString().equals(RESTAURANT)) {
                            lRestaurantList.add(lRestaurant);
                            lRestaurantsId.add(prediction.getPlaceId());
                            break;
                        }
                    }
                }

            }

            getAutoCompleteRestaurantsFromFirestore(lRestaurantList, lRestaurantsId);

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, ERROR_ON_FAILURE_LISTENER + apiException.getStatusCode());
            }
        });
    }

    /**
     * Get from Firestore the restaurant list retrieved from the autocomplete prediction result
     * If all the restaurant are in Firestore, the list is prepared and send for the display
     * If there's some restaurant missing, the list is submitted to Google to have their details
     * @param pRestaurantList : list object : restaurant list from the autocomplete prediction
     * @param pRestaurantsId : list string : restaurant id from the autocomplete prediction
     */
    private void getAutoCompleteRestaurantsFromFirestore(List<Restaurant> pRestaurantList, List<String> pRestaurantsId) {
        int lMaxRestaurantList = pRestaurantList.size();
        if (lMaxRestaurantList > 0) {
            mRestoRef.whereIn(String.valueOf(Restaurant.Fields.restoPlaceId), pRestaurantsId)
                    .get().addOnCompleteListener(pTask -> {
                if (pTask.isSuccessful()) {
                    List<Restaurant> lRestaurantListFirestore = pTask.getResult().toObjects(Restaurant.class);
                    if (lMaxRestaurantList != lRestaurantListFirestore.size()) {
                        getAutoCompleteMissingRestaurant(pRestaurantList);
                    } else {
                        prepareAndSendRestoListForDisplay(lRestaurantListFirestore);
                    }
                }
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, ERROR_ON_FAILURE_LISTENER + "Place not found in Firestore : " + apiException.getStatusCode());
                }
            });
        } else {
            Log.d(TAG, "getRestaurantsFromFirestore: no restaurant to search");
        }
    }

    /**
     * Retrieve restaurant detail information from Google
     * @param pAutocompleteRestaurantList : list object : autocomplete prediction restaurant list
     */
    private void getAutoCompleteMissingRestaurant(List<Restaurant> pAutocompleteRestaurantList) {

        mRestoListDetail = new ArrayList<>();

        if (pAutocompleteRestaurantList.size() > 0) {
            for (Restaurant lAutoRestaurant : pAutocompleteRestaurantList) {
                getGoogleDetailRestaurant(lAutoRestaurant.getRestoPlaceId(),
                        pAutocompleteRestaurantList.size());
            }
        }
    }

    /**
     * Get the photo from Google
     * @param pPhotoReference : string : photo reference of the restaurant
     * @param pMaxWidth : int : max width of the photo
     * @param pKey : string : google key
     * @return : string : the link to the photo
     */
    public static String getPhoto(String pPhotoReference, int pMaxWidth, String pKey) {
        return BASE_URL_GOOGLE + TXT_PHOTO_REF_GOOGLE + pPhotoReference
                + TXT_MAX_WIDTH_GOOGLE + pMaxWidth + TXT_KEY_GOOGLE + pKey;
    }
}