package com.fleb.go4lunch;

import android.content.Context;
import android.location.Location;

import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.model.RestaurantDetailPojo;
import com.fleb.go4lunch.service.Go4LunchApi;
import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.fleb.go4lunch.utilstest.RestaurantUtils;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;

import static com.fleb.go4lunch.utils.Go4LunchHelper.TXT_PROVIDER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Florence LE BOURNOT on 14/11/2020
 */
public class Go4LunchHelperTest {

    private Go4LunchApi mApi;

    @Mock
    Context mContextMocked;

    public FusedLocationProviderClient mLocationProviderClient;

    @BeforeEach
    public void setup() {

        initMocks(this);
        mApi = DI.getGo4LunchApiService();
        assertThat(mApi, notNullValue());

        mLocationProviderClient.setMockMode(true);
    }

    @AfterEach
    public void tearDown() { mApi = DI.getNewInstanceApiService(); }

    //--------------------------------RATING---------------------------------------//

    @DisplayName("Rating 0 - 0 star" )
    @Test
    public void givenRating_whenZero_thenDisplayNoStar() {

        int lNbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay(0);
        assertEquals(0,lNbStarToDisplay);
    }

    @DisplayName("Rating 1.50 - 1 star" )
    @Test
    public void givenRating_whenUnderToMaxLevelOneStar_thenDisplayOneStar() {

        int lNbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay(1.50);
        assertEquals(1,lNbStarToDisplay);
    }

    @DisplayName("Rating equal 1.67 - 1 star" )
    @Test
    public void givenRating_whenEqualToMaxLevelOneStar_thenDisplayOneStar() {

        int lNbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay(1.67);
        assertEquals(1,lNbStarToDisplay);
    }

    @DisplayName("Rating 2.25 - 2 stars" )
    @Test
    public void givenRating_whenAboveMaxLevelOneStarAndUnderToMaxLevelTwoStar_thenDisplayTwoStars() {

        int lNbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay(2.25);
        assertEquals(2,lNbStarToDisplay);
    }

    @DisplayName("Rating equal 3.34 - 2 stars" )
    @Test
    public void givenRating_whenAboveMaxLevelOneStarAndEqualToMaxLevelTwoStar_thenDisplayTwoStars() {

        int lNbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay(3.34);
        assertEquals(2,lNbStarToDisplay);
    }

    @DisplayName("Rating 4.4 - 3 stars" )
    @Test
    public void givenRating_whenAboveMaxLevelTwoStar_thenDisplayThreeStars() {

        int lNbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay(4.4);
        assertEquals(3,lNbStarToDisplay);
    }

    //--------------------------------LOCATION-------------------------------------//

    @Test
    public void convertLatitudeAndLongitudeIntoALocationWithSuccess() {
        Double lLat = 48.8244689;
        Double lLng = 2.4087766;


        //Mock ?
        Location lFusedLocationProvider = new Location(TXT_PROVIDER);
        lFusedLocationProvider.setLatitude(lLat);
        lFusedLocationProvider.setLongitude(lLng);

        Location lLocationRetrieved = Go4LunchHelper.setCurrentLocation(lLat,lLng);

        assertEquals(lFusedLocationProvider,lLocationRetrieved);
        assertThat(lLocationRetrieved.getLatitude(),equalTo(lLat));
        assertThat(lLocationRetrieved.getLongitude(),equalTo(lLng));
    }

    @Test
    public void getDistanceBetweenRestaurantAndCurrentLocationWithSuccee() {
        double lLatCurrent = 48.8236549;
        double lLngCurrent = 2.4102578;
        double lLatRestaurant = 48.8244689;
        double lLngRestaurant = 2.4087766;
        int lDistance = 174;


        Location lCurrentLocation = Go4LunchHelper.setCurrentLocation(lLatCurrent,lLngCurrent);
        RestaurantDetailPojo.Location lRestaurantLocation = RestaurantUtils.generateLocation(lLatRestaurant, lLngRestaurant);

        int lDistanceRetrieved = Go4LunchHelper.getRestaurantDistanceToCurrentLocation(lCurrentLocation, lRestaurantLocation);

        Location mockLocationRestaurant = mock(Location.class);
        given(mockLocationRestaurant.getLatitude()).willReturn(lLatRestaurant);
        given(mockLocationRestaurant.getLongitude()).willReturn(lLngRestaurant);

        Location mockLocationCurrent = mock(Location.class);
        given(mockLocationCurrent.getLatitude()).willReturn(lLatCurrent);
        given(mockLocationCurrent.getLongitude()).willReturn(lLngCurrent);
        given(mockLocationCurrent.distanceTo(mockLocationRestaurant)).willReturn((float) lDistance);

        //assertEquals(lDistance,lDistanceRetrieved);
        assertThat(mockLocationCurrent.distanceTo(mockLocationRestaurant),equalTo((float) lDistance));
    }

    //-----------------------------------------------------------------------------//
    //-----------------------------------------------------------------------------//
    //-----------------------------------------------------------------------------//
    //-----------------------------------------------------------------------------//

}
