package com.fleb.go4lunch.network;

import com.fleb.go4lunch.model.CommentPost;
import com.fleb.go4lunch.model.RestaurantDetailPojo;
import com.fleb.go4lunch.model.RestaurantPojo;
import com.fleb.go4lunch.model.Post;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Florence LE BOURNOT on 04/10/2020
 */
public interface JsonRetrofitApi {

    String BASE_URL_TEST = "https://jsonplaceholder.typicode.com/";
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
//"name,address_components,formatted_phone_number,icon,id,international_phone_number," +
//                    "rating,website,utc_offset,opening_hours"



    @GET("posts")
    Call<List<Post>> getPosts(
            @Query("userId") Integer userId,
            @Query("userId") Integer userId2,
            @Query("_sort") String pSort,
            @Query("_order") String pOrder
    );

    @GET("posts")
    Call<List<Post>> getPostsMultipleFirstParameter(
            @Query("userId") Integer[] userId,
            @Query("_sort") String pSort,
            @Query("_order") String pOrder
    );

    @GET("posts")
    Call<List<Post>> getPostsMap(@QueryMap Map<String, String> parameters);

    @GET("posts/{id}/comments")
    Call<List<CommentPost>> getComments(@Path("id") int pPostId);

    @GET
    Call<List<CommentPost>> getComments(@Url String url);

}
