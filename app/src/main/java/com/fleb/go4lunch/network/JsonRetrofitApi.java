package com.fleb.go4lunch.network;

import com.fleb.go4lunch.model.RestaurantDetailPojo;
import com.fleb.go4lunch.model.RestaurantPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Florence LE BOURNOT on 04/10/2020
 *
 * Retrofit Api to get informations from Google
 */
public interface JsonRetrofitApi {

    String BASE_URL_GOOGLE = "https://maps.googleapis.com/maps/api/place/";
    String TXT_PHOTO_REF_GOOGLE = "photo?photoreference=";
    String TXT_MAX_WIDTH_GOOGLE = "&maxwidth=";
    String TXT_KEY_GOOGLE = "&key=";

    @GET("nearbysearch/json")
    Call<RestaurantPojo> getNearByPlaces(
            @Query("key") String pKey,
            @Query("type") String pType,
            @Query("location") String pLocation,
            @Query("radius") int pRadius);


    @GET("details/json")
    Call<RestaurantDetailPojo> getRestaurantDetail(
            @Query("key") String pKey,
            @Query("place_id") String pPlaceId,
            @Query("fields") String pFields
    );

}
