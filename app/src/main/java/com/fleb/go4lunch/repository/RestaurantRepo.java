package com.fleb.go4lunch.repository;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.fleb.go4lunch.BuildConfig;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.RestaurantDetailPojo;
import com.fleb.go4lunch.model.RestaurantPojo;
import com.fleb.go4lunch.network.ApiClient;
import com.fleb.go4lunch.network.JsonRetrofitApi;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fleb.go4lunch.network.JsonRetrofitApi.BASE_URL_GOOGLE;

/**
 * Created by Florence LE BOURNOT on 26/09/2020
 */
public class RestaurantRepo {

    private List<Restaurant> mRestoList;

    public interface OnFirestoreTaskComplete {
        void restoDataLoaded(List<Restaurant> pRestoList);
        void restoOnGoogleError(String pErrorGoogle);
    }
    private OnFirestoreTaskComplete mOnFirestoreTaskComplete;
    public static final String TAG_REPO = "TAG_REPO";
    public static final String RESTO_COLLECTION = "Restaurant";
    private String mKey = BuildConfig.MAPS_API_KEY;

    private String mWebSite;
    private JsonRetrofitApi mJsonRetrofitApi;
    private RestaurantDetailPojo.Result mRestoDetResponse;

    public RestaurantRepo(OnFirestoreTaskComplete pOnFirestoreTaskComplete) {
        this.mOnFirestoreTaskComplete = pOnFirestoreTaskComplete;
    }
        //return LiveData
        public void getRestaurantsPlaces(Context pContext, Double pLatitude, Double pLongitude) {

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
                    RestaurantDetailPojo lRestoDet;

                    //TODO à supprimer après avoir trouver la currentlocation

                    Location lFusedLocationProvider = new Location("fusedLocationProvider");
                    lFusedLocationProvider.setLatitude(pLatitude);
                    lFusedLocationProvider.setLongitude(pLongitude);

                    for (RestaurantPojo.Result restaurantPojo : lRestoResponse) {

                        //TODO gérer openinghours
                        if (restaurantPojo.getOpeningHours().getOpenNow()) {
                            lOpening = "Ouvert";
                        } else {
                            lOpening = "Ferme";
                        }

                        String lPlaceId = restaurantPojo.getPlaceId();
                        String lName = restaurantPojo.getName();
                        String lPhoto = (restaurantPojo.getPhotos() != null ? getPhoto(restaurantPojo.getPhotos().get(0).getPhotoReference(), 400,mKey) : "");
                        RestaurantPojo.Location lLocation = restaurantPojo.getGeometry().getLocation();
                        String lAddress = formatAddress(restaurantPojo.getVicinity());

                        Double lRating = restaurantPojo.getRating();
                        String lDistance = String.valueOf(getRestaurantDistanceToCurrentLocation(
                                lFusedLocationProvider, restaurantPojo.getGeometry().getLocation()));

                        //RestaurantDetailPojo.Result lRestoDetResult = getRestaurantDetail(pContext,lPlaceId);
                        //Log.d(TAG_REPO, "onResponse: " + lRestoDetResult.getWebsite());
/*
                        Call<RestaurantDetailPojo> lRestaurantDetailPojoCall = lJsonRetrofitApi.getRestaurantDetail(mKey,
                                lPlaceId,lFields);
                        lRestaurantDetailPojoCall.enqueue(new Callback<RestaurantDetailPojo>() {
                            @Override
                            public void onResponse(Call<RestaurantDetailPojo> call, Response<RestaurantDetailPojo> response) {
                                if (response.isSuccessful()) {
                                    RestaurantDetailPojo.Result lRestoDetResponse = response.body().getResult();
                                    Log.d(TAG_REPO, "onResponse:weekdaytext: " + lRestoDetResponse.getOpeningHours().getWeekdayText());
                                    Log.d(TAG_REPO, "onResponse:periods: " + lRestoDetResponse.getOpeningHours().getPeriods());
                                    Log.d(TAG_REPO, "onResponse:opennow: " + lRestoDetResponse.getOpeningHours().getOpenNow());
                                    mWebSite = lRestoDetResponse.getWebsite();
                                }
                            }

                            @Override
                            public void onFailure(Call<RestaurantDetailPojo> call, Throwable t) {
                                mOnFirestoreTaskComplete.restoOnGoogleError("Error Google");
                            }
                        });
*/

                        Restaurant lRestaurant = new Restaurant(
                                lPlaceId, lName, lAddress, null, null, lDistance, 0,
                                lOpening, lRating, lPhoto, lLocation
                        );

                        lRestoList.add(lRestaurant);
                    }
                    //Log.d(TAG_GETRESTO, "onResponse: " + lRestoList.size());
                    //TODO LiveData.setValue
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

    public RestaurantDetailPojo.Result getRestaurantDetail(Context pContext, String pPlaceId) {

        String lFields = pContext.getResources().getString(R.string.place_detail_fields);
        Call<RestaurantDetailPojo> lRestaurantDetailPojoCall = mJsonRetrofitApi.getRestaurantDetail(mKey,
                pPlaceId,lFields);
        lRestaurantDetailPojoCall.enqueue(new Callback<RestaurantDetailPojo>() {
            @Override
            public void onResponse(Call<RestaurantDetailPojo> call, Response<RestaurantDetailPojo> response) {
                if (response.isSuccessful()) {
                    RestaurantDetailPojo.Result lRestoDetResponse = response.body().getResult();
                    Log.d(TAG_REPO, "onResponse:weekdaytext: " + lRestoDetResponse.getOpeningHours().getWeekdayText());
                    Log.d(TAG_REPO, "onResponse:periods: " + lRestoDetResponse.getOpeningHours().getPeriods());
                    Log.d(TAG_REPO, "onResponse:opennow: " + lRestoDetResponse.getOpeningHours().getOpenNow());
                    mWebSite = lRestoDetResponse.getWebsite();
                    mRestoDetResponse = lRestoDetResponse;
                }
            }

            @Override
            public void onFailure(Call<RestaurantDetailPojo> call, Throwable t) {
                mOnFirestoreTaskComplete.restoOnGoogleError("Error Google");
            }
        });
        return mRestoDetResponse;

    }

    public String getPhoto(String pPhotoReference, int pMaxWidth, String pKey) {
        return BASE_URL_GOOGLE + "photo?photoreference=" + pPhotoReference
                + "&maxwidth=" + pMaxWidth + "&key=" + pKey;
    }

    public int getRestaurantDistanceToCurrentLocation(Location pCurrentLocation, RestaurantPojo.Location pRestoLocation) {
        Location lRestaurantLocation = new Location("fusedLocationProvider");

        lRestaurantLocation.setLatitude(pRestoLocation.getLat());
        lRestaurantLocation.setLongitude(pRestoLocation.getLng());

        return (int) pCurrentLocation.distanceTo(lRestaurantLocation);
    }

    public String formatAddress(String pAddress) {
        return pAddress.substring(0,pAddress.indexOf(","));
    }
}
