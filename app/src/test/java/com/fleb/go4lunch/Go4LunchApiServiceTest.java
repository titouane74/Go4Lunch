package com.fleb.go4lunch;

import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.service.Go4LunchApi;
import com.fleb.go4lunch.utilstest.RestaurantUtils;
import com.fleb.go4lunch.utilstest.WorkmateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Florence LE BOURNOT on 13/11/2020
 */
public class Go4LunchApiServiceTest {

    private Go4LunchApi mApi;


    @Before
    public void setup() {

        initMocks(this);
        mApi = DI.getGo4LunchApiService();
        assertThat(mApi, notNullValue());

    }

    @After
    public void tearDown() { mApi = DI.getNewInstanceApiService(); }



    @Test
    public void setAndGetWorkmateIdWithSuccess() {
        assertNull(mApi.getWorkmateId());

        mApi.setWorkmateId("AlbertId");
        assertEquals("AlbertId",mApi.getWorkmateId());
    }

    @Test
    public void setAndGetWorkmateWithSuccess() {
        assertNull(mApi.getWorkmate());

        Workmate lWorkmateGenerated = WorkmateUtils.generateWorkmateNameEmailPhoto("AlbertId", "Albert");
        mApi.setWorkmate(lWorkmateGenerated);

        Workmate lWorkmateRetrieved = mApi.getWorkmate();
        assertEquals(lWorkmateGenerated,lWorkmateRetrieved);
    }

    @Test
    public void setAndGetRestaurantWithSuccess() {
        assertNull(mApi.getRestaurant());

        Restaurant lRestaurantGenerated = RestaurantUtils.generateRestaurantName("EnLaiId", "EnLai");
        mApi.setRestaurant(lRestaurantGenerated);

        Restaurant lRestaurantRetrieved = mApi.getRestaurant();
        assertEquals(lRestaurantGenerated,lRestaurantRetrieved);

    }

    @Test
    public void setAndGetRestaurantListWithSuccess() {
        assertNull(mApi.getRestaurantList());

        List<Restaurant> lRestaurantListGenerated = new ArrayList<>();
        lRestaurantListGenerated.add(RestaurantUtils.generateRestaurantAllFields(
                "EnLaiId", "EnLai",108,4.4,48.822779,2.411444399999999));
        lRestaurantListGenerated.add(RestaurantUtils.generateRestaurantAllFields(
                "Family26Id", "Le Family 26",89,4.4,48.8239094,2.4096468));
        lRestaurantListGenerated.add(RestaurantUtils.generateRestaurantAllFields(
                "TataEugenieId", "Tante Eugénie",174,4.3,48.8244689,2.4087766));

        mApi.setRestaurantList(lRestaurantListGenerated);

        List<Restaurant> lRestaurantListRetrieved = mApi.getRestaurantList();
        assertEquals(lRestaurantListGenerated.size(),lRestaurantListRetrieved.size());
        assertEquals("EnLaiId",lRestaurantListRetrieved.get(0).getRestoPlaceId());
        assertEquals("Family26Id",lRestaurantListRetrieved.get(1).getRestoPlaceId());
        assertEquals("TataEugenieId",lRestaurantListRetrieved.get(2).getRestoPlaceId());

    }

}
