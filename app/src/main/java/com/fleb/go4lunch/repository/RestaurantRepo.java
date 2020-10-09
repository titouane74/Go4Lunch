package com.fleb.go4lunch.repository;

import android.content.Context;
import android.location.Location;
import android.util.Log;

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
import static com.fleb.go4lunch.view.fragments.MapsFragment.TAG_GETRESTO;


/**
 * Created by Florence LE BOURNOT on 26/09/2020
 */
public class RestaurantRepo {

    public interface OnFirestoreTaskComplete {
        void restoDataLoaded(List<Restaurant> pRestoList);

        void restoOnError(Exception pE);

        void restoOnGoogleError(String pErrorGoogle);
    }

    public static final String RESTO_COLLECTION = "Restaurant";
    private String mKey = BuildConfig.MAPS_API_KEY;

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mRestoRef = mDb.collection(RESTO_COLLECTION);

    private OnFirestoreTaskComplete mOnFirestoreTaskComplete;

    public RestaurantRepo(OnFirestoreTaskComplete pOnFirestoreTaskComplete) {
        this.mOnFirestoreTaskComplete = pOnFirestoreTaskComplete;
    }

    public void getRestaurantData() {
        mRestoRef.get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        mOnFirestoreTaskComplete.restoDataLoaded(
                                Objects.requireNonNull(pTask.getResult()).toObjects(Restaurant.class)
                        );
                    } else {
                        mOnFirestoreTaskComplete.restoOnError(pTask.getException());
                    }
                });
    }

    public void getRestaurantsPlaces(Context pContext, Double pLatitude, Double pLongitude) {

        int lProximityRadius = Integer.parseInt(pContext.getResources().getString(R.string.proximity_radius));
        String lType = pContext.getResources().getString(R.string.type_search);

        JsonRetrofitApi lJsonRetrofitApi = ApiClient.getClient(BASE_URL_GOOGLE).create(JsonRetrofitApi.class);

        Call<RestaurantPojo> call = lJsonRetrofitApi.getNearByPlaces(mKey, lType,
                pLatitude + "," + pLongitude, lProximityRadius);

        call.enqueue(new Callback<RestaurantPojo>() {
            @Override
            public void onResponse(Call<RestaurantPojo> call, Response<RestaurantPojo> response) {


                if (response.isSuccessful()) {

                    List<RestaurantPojo.Result> lRestoResponse = response.body().getResults();
                    List<Restaurant> lRestoList = new ArrayList<>();
                    String lOpening ;

                    //TODO à supprimer après avoir trouver la currentlocation

                    Location lFusedLocationProvider = new Location("fusedLocationProvider");
                    lFusedLocationProvider.setLatitude(pLatitude);
                    lFusedLocationProvider.setLongitude(pLongitude);

                    for (int i = 0; i < lRestoResponse.size(); i++) {

                        //TODO gérer openinghours
                        if (lRestoResponse.get(i).getOpeningHours().getOpenNow()) {
                            lOpening = "Ouvert";
                        } else {
                            lOpening = "Ferme";
                        }

                        String lPlaceId = lRestoResponse.get(i).getPlaceId();
                        String lName = lRestoResponse.get(i).getName();
                        String lPhoto = (lRestoResponse.get(i).getPhotos() != null ? getPhoto(lRestoResponse.get(i).getPhotos().get(0).getPhotoReference(), 400) : "");
                        RestaurantPojo.Location lLocation = lRestoResponse.get(i).getGeometry().getLocation();
                        String lAddress = lRestoResponse.get(i).getVicinity();
                        Double lRating = lRestoResponse.get(i).getRating();
                        String lDistance = String.valueOf(getRestaurantDistanceToCurrentLocation(
                                lFusedLocationProvider, lRestoResponse.get(i).getGeometry().getLocation()));

                        Restaurant lRestaurant = new Restaurant(
                                lPlaceId, lName, lAddress, null, null, lDistance, 0,
                                lOpening, lRating, lPhoto, lLocation
                        );

                        lRestoList.add(lRestaurant);
                    }
                    Log.d(TAG_GETRESTO, "onResponse: " + lRestoList.size());
                    mOnFirestoreTaskComplete.restoDataLoaded(lRestoList);
                } else {
                    mOnFirestoreTaskComplete.restoOnGoogleError("Error Google");
                }

            }

            @Override
            public void onFailure(Call<RestaurantPojo> call, Throwable t) {
                Log.d("onFailure", t.toString());

            }
        });
    }

    public String getPhoto(String pPhotoReference, int pMaxWidth) {
        return BASE_URL_GOOGLE + "photo?photoreference=" + pPhotoReference
                + "&maxwidth=" + pMaxWidth + "&key=" + mKey;
    }

    public int getRestaurantDistanceToCurrentLocation(Location pCurrentLocation, RestaurantPojo.Location pRestoLocation) {
        Location lRestaurantLocation = new Location("fusedLocationProvider");

        lRestaurantLocation.setLatitude(pRestoLocation.getLat());
        lRestaurantLocation.setLongitude(pRestoLocation.getLng());

        return (int) pCurrentLocation.distanceTo(lRestaurantLocation);
    }
}
