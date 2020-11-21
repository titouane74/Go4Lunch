package com.fleb.go4lunch.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Florence LE BOURNOT on 04/10/2020
 *
 * Initialize the Retrofit client for the calls to Google
 */
public class ApiClient {

    private static Retrofit mRetrofit;

    public static Retrofit getClient(String pBaseUrl) {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(pBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}
