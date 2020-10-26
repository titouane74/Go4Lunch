package com.fleb.go4lunch.di;

import com.fleb.go4lunch.service.Go4LunchApiService;
import com.fleb.go4lunch.service.Go4LunchApi;

/**
 * Created by Florence LE BOURNOT on 26/10/2020
 */
public class DI {

    private static Go4LunchApi mService = new Go4LunchApiService();

    public static Go4LunchApi getGo4LunchApiService(){ return mService; }

    //Will serve for the unit test
    public static Go4LunchApi getNewInstanceApiService() {
        return new Go4LunchApiService();
    }
}
