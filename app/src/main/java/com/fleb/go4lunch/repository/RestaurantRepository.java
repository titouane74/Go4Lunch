package com.fleb.go4lunch.repository;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.BuildConfig;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.RestaurantPojo;
import com.fleb.go4lunch.network.ApiClient;
import com.fleb.go4lunch.network.JsonRetrofitApi;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fleb.go4lunch.network.JsonRetrofitApi.BASE_URL_GOOGLE;

/**
 * Created by Florence LE BOURNOT on 13/10/2020
 */
public class RestaurantRepository {

    public static final String TAG_REPO = "TAG_REPO";

    /**
     * Firebase declarations
     */
    public static final String RESTO_COLLECTION = "Restaurant";
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mRestoRef = mDb.collection(RESTO_COLLECTION);

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


    public MutableLiveData<List<Restaurant>> getLDFirestoreRestaurantList() {
        mRestoRef.get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Log.d("TAG_repository", "onComplete: setValue");
                        mLDRestoList.setValue(Objects.requireNonNull(pTask.getResult()).toObjects(Restaurant.class));
                    } else {
                        Log.d("TAG_REPO_ERROR", "getRestaurantData: " + pTask.getException());
                    }
                });

        return mLDRestoList;
    }

    public MutableLiveData<List<Restaurant>> getLDGoogleRestaurantList(Context pContext, Double pLatitude, Double pLongitude) {

        int lProximityRadius = Integer.parseInt(pContext.getResources().getString(R.string.proximity_radius));
        String lType = pContext.getResources().getString(R.string.type_search);

        mJsonRetrofitApi = ApiClient.getClient(BASE_URL_GOOGLE).create(JsonRetrofitApi.class);

        Call<RestaurantPojo> lRestaurantPojoCall = mJsonRetrofitApi.getNearByPlaces(mKey, lType,
                pLatitude + "," + pLongitude, lProximityRadius);

        lRestaurantPojoCall.enqueue(new Callback<RestaurantPojo>() {
            @Override
            public void onResponse(Call<RestaurantPojo> call, Response<RestaurantPojo> response) {
                if (response.isSuccessful()) {
                    List<RestaurantPojo.Result> lRestoResponse = response.body().getResults();
                    List<Restaurant> lRestoList = new ArrayList<>();
                    String lOpening ;

                    //TODO à supprimer après avoir trouver la currentlocation
                    setCurrentLocation(pLatitude, pLongitude);

                    for (RestaurantPojo.Result restaurantPojo : lRestoResponse) {
                        String lPlaceId = restaurantPojo.getPlaceId();
                        String lName = restaurantPojo.getName();
                        String lPhoto = (restaurantPojo.getPhotos() != null ? getPhoto(restaurantPojo.getPhotos().get(0).getPhotoReference(), 400,mKey) : "");
                        RestaurantPojo.Location lLocation = restaurantPojo.getGeometry().getLocation();
                        String lAddress = formatAddress(restaurantPojo.getVicinity());
                        Double lRating = restaurantPojo.getRating();
                        String lDistance = String.valueOf(getRestaurantDistanceToCurrentLocation(
                                mFusedLocationProvider, restaurantPojo.getGeometry().getLocation()));

                        //TODO gérer openinghours
                        if (restaurantPojo.getOpeningHours().getOpenNow()) {
                            lOpening = "Ouvert";
                        } else {
                            lOpening = "Ferme";
                        }
                        Restaurant lRestaurant = new Restaurant(
                                lPlaceId, lName, lAddress, null, null, null, 0,
                                lOpening, lRating, lPhoto, lLocation
                        );

                        lRestoList.add(lRestaurant);
                    }
                    mLDRestoList.setValue(lRestoList);
                }
            }

            @Override
            public void onFailure(Call<RestaurantPojo> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
        return mLDRestoList;
    }

    public String getPhoto(String pPhotoReference, int pMaxWidth, String pKey) {
        return BASE_URL_GOOGLE + "photo?photoreference=" + pPhotoReference
                + "&maxwidth=" + pMaxWidth + "&key=" + pKey;
    }
    public String formatAddress(String pAddress) {
        return pAddress.substring(0,pAddress.indexOf(","));
    }
    public int getRestaurantDistanceToCurrentLocation(Location pCurrentLocation, RestaurantPojo.Location pRestoLocation) {
        Location lRestaurantLocation = new Location("fusedLocationProvider");

        lRestaurantLocation.setLatitude(pRestoLocation.getLat());
        lRestaurantLocation.setLongitude(pRestoLocation.getLng());

        return (int) pCurrentLocation.distanceTo(lRestaurantLocation);
    }

    private void setCurrentLocation(Double pLat, Double pLng) {
        mFusedLocationProvider = new Location("fusedLocationProvider");
        mFusedLocationProvider.setLatitude(pLat);
        mFusedLocationProvider.setLongitude(pLng);
    }
}
