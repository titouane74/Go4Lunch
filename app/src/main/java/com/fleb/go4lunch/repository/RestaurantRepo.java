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
        JsonRetrofitApi lJsonRetrofitApi;
        int lProximityRadius;

        lProximityRadius = Integer.parseInt(pContext.getResources().getString(R.string.proximity_radius));

        lJsonRetrofitApi = ApiClient.getClient(BASE_URL_GOOGLE).create(JsonRetrofitApi.class);

        Call<RestaurantPojo> call = lJsonRetrofitApi.getNearByPlaces(pKey,pType,
                pLatitude + "," + pLongitude, lProximityRadius);

        call.enqueue(new Callback<RestaurantPojo>() {
            @Override
            public void onResponse(Call<RestaurantPojo> call, Response<RestaurantPojo> response) {

                if (response.isSuccessful()) {
                    mOnGoogleResponse.restoDataGoogle(response.body());
                } else {
                    mOnGoogleResponse.restoOnErrrorGoogle("Error response");
                }
            }

            @Override
            public void onFailure(Call<RestaurantPojo> call, Throwable t) {
                Log.d("onFailure", t.toString());

            }
        });
    }

}
