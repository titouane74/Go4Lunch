package com.fleb.go4lunch.repository;

import android.content.Context;
import android.util.Log;

import com.facebook.places.BuildConfig;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.RestaurantPojo;
import com.fleb.go4lunch.network.ApiClient;
import com.fleb.go4lunch.network.JsonRetrofitApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

    public interface OnGoogleResponse {
        void restoDataGoogle(RestaurantPojo pRestoListGoogle);

        void restoOnErrrorGoogle(String pErrorBody);
    }

    public static final String RESTO_COLLECTION = "Restaurant";

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mRestoRef = mDb.collection(RESTO_COLLECTION);

    private OnFirestoreTaskComplete mOnFirestoreTaskComplete;
    private OnGoogleResponse mOnGoogleResponse;

    public RestaurantRepo(OnFirestoreTaskComplete pOnFirestoreTaskComplete) {
        this.mOnFirestoreTaskComplete = pOnFirestoreTaskComplete;
    }

/*    public RestaurantRepo(OnGoogleResponse pOnGoogleResponse) {
        this.mOnGoogleResponse = pOnGoogleResponse;
    }*/

    public void getRestoData() {
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

    public void getRestaurantsPlaces(Context pContext, String pType, Double pLatitude, Double pLongitude, String pKey) {

        int lProximityRadius = Integer.parseInt(pContext.getResources().getString(R.string.proximity_radius));

        JsonRetrofitApi lJsonRetrofitApi = ApiClient.getClient(BASE_URL_GOOGLE).create(JsonRetrofitApi.class);

        Call<RestaurantPojo> call = lJsonRetrofitApi.getNearByPlaces(pKey, pType,
                pLatitude + "," + pLongitude, lProximityRadius);

        call.enqueue(new Callback<RestaurantPojo>() {
            @Override
            public void onResponse(Call<RestaurantPojo> call, Response<RestaurantPojo> response) {


                if (response.isSuccessful()) {

                    List<RestaurantPojo.Result> lRestoResponse = response.body().getResults();
                    List<Restaurant> lRestoList = new ArrayList<>();
                    String lOpening = null;

                    Log.d(TAG_GETRESTO, "onResponse: lrestoResponse size" + lRestoResponse.size());

                    for (int i = 0; i < lRestoResponse.size(); i++) {

                        //TODO gérer openinghours
/*
                        if (lRestoResponse.get(i).getOpeningHours().getOpenNow() ) {
                            lOpening = null;
                        } else {
                            lOpening = null;
                        }
*/

                        Restaurant lRestaurant = new Restaurant(
                                //pRestoPlaceId
                                lRestoResponse.get(i).getPlaceId(),
                                //pRestoName
                                lRestoResponse.get(i).getName(),
                                //pRestoAddress
                                lRestoResponse.get(i).getVicinity(),
                                //pRestoPhone
                                null,
                                //pRestoWebsite
                                null,
                                //pRestoDistance
                                null,
                                //pRestoNbWorkmates
                                0,
                                //pRestoOpening
                                lOpening,
                                //pRestoRating
                                lRestoResponse.get(i).getRating(),
                                //pRestoPhotoUrl
                                //lRestoResponse.get(i).getPhotos().get(i).getHtmlAttributions(),
                                null,
                                //pRestoLat
                                lRestoResponse.get(i).getGeometry().getLocation().getLat(),
                                //pRestoLng
                                lRestoResponse.get(i).getGeometry().getLocation().getLng()
                        );

                        lRestoList.add(lRestaurant);
                    }
                    Log.d(TAG_GETRESTO, "onResponse: " + lRestoList.size());
                    mOnFirestoreTaskComplete.restoDataLoaded(lRestoList);
                } else {
                    mOnFirestoreTaskComplete.restoOnGoogleError("Error Google");
                }
/*
                if (response.isSuccessful()) {
                    mOnGoogleResponse.restoDataGoogle(response.body());
                } else {
                    mOnGoogleResponse.restoOnErrrorGoogle("Error response");
                }
*/
            }

            @Override
            public void onFailure(Call<RestaurantPojo> call, Throwable t) {
                Log.d("onFailure", t.toString());

            }
        });
    }

}
