package com.fleb.go4lunch.restaurant;

import android.content.Context;

import com.fleb.go4lunch.di.DI;

import com.fleb.go4lunch.service.Go4LunchApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Florence LE BOURNOT on 14/11/2020
 */
public class RestaurantTest {

    private Go4LunchApi mApi;

    @Mock
    Context contextMock;

    private Context mContextMocked;

    @Before
    public void setup() {
        mContextMocked = mock(Context.class);

        initMocks(this);
        mApi = DI.getGo4LunchApiService();
        assertThat(mApi, notNullValue());

    }

    @After
    public void tearDown() { mApi = DI.getNewInstanceApiService(); }

    @Test
    public void getRestaurant(){

    }

    @Test
    public void setAndGetRestaurantIdWithSuccess() { }

    @Test
    public void setAndGetRestaurantNameWithSuccess() { }

    @Test
    public void setAndGetRestaurantAddressWithSuccess() { }

    @Test
    public void setAndGetPhoneWithSuccess() { }

    @Test
    public void setAndGetwebSiteWithSuccess() { }

    @Test
    public void setAndGetRestaurantDistanceInTextWithSuccess() { }

    @Test
    public void setAndGetRatingWithSuccess() { }

    @Test
    public void setAndGetRestaurantPhotoUrlWithSuccess() { }

    @Test
    public void setAndGetRestaurantLocationWithSuccess() { }

    @Test
    public void setAndGetRestaurantOpeningHoursWithSuccess() { }

    @Test
    public void setAndGetRestaurantDistanceInMetersWithSuccess() { }

    @Test
    public void setAndGetWorkmatesListComingToEatWithSuccess() { }

    @Test
    public void compareTwoRestaurantDistancesWithSuccess() { }

    @Test
    public void givenTwoRestaurantDistance_whenEquals_ThenSucceed() { }

    @Test
    public void setAndGetWorkmateIdFromWorkmatesListComingToEatWithSuccess() { }

    @Test
    public void setAndGetWorkmateNameFromWorkmatesListComingToEatWithSuccess() { }

    @Test
    public void setAndGetWorkmatePhotoUrlFromWorkmatesListComingToEatWithSuccess() { }

}
