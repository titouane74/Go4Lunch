package com.fleb.go4lunch.network;


import com.fleb.go4lunch.model.RestaurantDetailPojo;
import com.fleb.go4lunch.model.RestaurantPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Florence LE BOURNOT on 04/10/2020
 */
public interface JsonRetrofitApi {

    //String BASE_URL_TEST = "https://jsonplaceholder.typicode.com/";
    String BASE_URL_GOOGLE = "https://maps.googleapis.com/maps/api/place/";

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
