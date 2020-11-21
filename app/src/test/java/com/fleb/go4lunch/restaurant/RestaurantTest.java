package com.fleb.go4lunch.restaurant;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.RestaurantDetailPojo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Florence LE BOURNOT on 14/11/2020
 */
public class RestaurantTest {

    private Restaurant mRestaurant;
    private String mRestoId = "id";
    private String mRestoName = "name";
    private String mRestoAddress = "address";
    private String mRestoPhone = "phone";
    private String mRestoWebSite = "web_site";
    private String mRestoDistanceText = "100m" ;
    private double mRestoRating = 4.4;
    private String mRestoPhotoUrl = "photo_url";
    private RestaurantDetailPojo.Location mRestoLocation;
    private RestaurantDetailPojo.OpeningHours mRestoOpeningHours;
    private int mRestoDistance = 100;
    private List<Restaurant.WorkmatesList> mRestoWkmList = new ArrayList<>();
    private Restaurant.WorkmatesList mRestoWkm;

    @Before
    public void setup() {

    }

    @Test
    public void setRestaurantEmptyConstructorWithSuccess() {
        mRestaurant = new Restaurant();
        assertNotNull(mRestaurant);
        assertNull(mRestaurant.getRestoPlaceId());
    }

    @Test
    public void setAndGetRestaurantIdWithSuccess() {
        mRestaurant = new Restaurant(mRestoId,mRestoName);
        assertEquals(mRestoId, mRestaurant.getRestoPlaceId());

        String lSetData = "tataid";
        mRestaurant.setRestoPlaceId(lSetData);

        String lGetData = mRestaurant.getRestoPlaceId();
        assertNotNull(lGetData);
        assertEquals(lSetData,lGetData);
    }

    @Test
    public void setAndGetRestaurantNameWithSuccess() {
        mRestaurant = new Restaurant(mRestoId,mRestoName);
        assertEquals(mRestoName, mRestaurant.getRestoName());

        String lSetData = "tata eugenie";
        mRestaurant.setRestoName(lSetData);

        String lGetData = mRestaurant.getRestoName();
        assertNotNull(lGetData);
        assertEquals(lSetData,lGetData);
    }

    @Test
    public void setAndGetRestaurantAddressWithSuccess() {
        mRestaurant = new Restaurant(mRestoId,mRestoName,mRestoAddress,mRestoDistanceText,mRestoRating,
                mRestoPhotoUrl,mRestoLocation,mRestoOpeningHours,mRestoDistance,mRestoWkmList);
        assertEquals(mRestoAddress, mRestaurant.getRestoAddress());

        String lSetData = "rue de Paris";
        mRestaurant.setRestoAddress(lSetData);

        String lGetData = mRestaurant.getRestoAddress();
        assertNotNull(lGetData);
        assertEquals(lSetData,lGetData);
    }

    @Test
    public void setAndGetPhoneWithSuccess() {
        mRestaurant = new Restaurant(mRestoId,mRestoName,mRestoAddress,mRestoPhone, mRestoWebSite,mRestoDistanceText,mRestoRating,
                mRestoPhotoUrl,mRestoLocation,mRestoOpeningHours,mRestoDistance,mRestoWkmList);
        assertEquals(mRestoPhone, mRestaurant.getRestoPhone());

        String lSetData = "01.01.01.01.01";
        mRestaurant.setRestoPhone(lSetData);

        String lGetData = mRestaurant.getRestoPhone();
        assertNotNull(lGetData);
        assertEquals(lSetData,lGetData);
    }

    @Test
    public void setAndGetWebSiteWithSuccess() {
        mRestaurant = new Restaurant(mRestoId,mRestoName,mRestoAddress,mRestoPhone, mRestoWebSite, mRestoDistanceText,mRestoRating,
                mRestoPhotoUrl,mRestoLocation,mRestoOpeningHours,mRestoDistance,mRestoWkmList);
        assertEquals(mRestoWebSite, mRestaurant.getRestoWebSite());

        String lSetData = "New web site";
        mRestaurant.setRestoWebSite(lSetData);

        String lGetData = mRestaurant.getRestoWebSite();
        assertNotNull(lGetData);
        assertEquals(lSetData,lGetData);
    }

    @Test
    public void setAndGetRestaurantDistanceInTextWithSuccess() {
        mRestaurant = new Restaurant(mRestoId,mRestoName,mRestoAddress,mRestoDistanceText,mRestoRating,
                mRestoPhotoUrl,mRestoLocation,mRestoOpeningHours,mRestoDistance,mRestoWkmList);
        assertEquals(mRestoDistanceText, mRestaurant.getRestoDistanceText());

        String lSetData = "200m";
        mRestaurant.setRestoDistanceText(lSetData);

        String lGetData = mRestaurant.getRestoDistanceText();
        assertNotNull(lGetData);
        assertEquals(lSetData,lGetData);
    }

    @Test
    public void setAndGetRatingWithSuccess() {
        mRestaurant = new Restaurant(mRestoId,mRestoName,mRestoAddress,mRestoDistanceText,mRestoRating,
                mRestoPhotoUrl,mRestoLocation,mRestoOpeningHours,mRestoDistance,mRestoWkmList);
        assertEquals(mRestaurant.getRestoRating(), (mRestoRating), 0.0);

        double lSetData = 3.2;
        mRestaurant.setRestoRating(lSetData);

        double lGetData = mRestaurant.getRestoRating();
        assertEquals(lGetData, lSetData, 0.0);
    }

    @Test
    public void setAndGetRestaurantPhotoUrlWithSuccess() {
        mRestaurant = new Restaurant(mRestoId,mRestoName,mRestoAddress,mRestoDistanceText,mRestoRating,
                mRestoPhotoUrl,mRestoLocation,mRestoOpeningHours,mRestoDistance,mRestoWkmList);
        assertEquals(mRestoPhotoUrl, mRestaurant.getRestoPhotoUrl());

        String lSetData = "New photo url";
        mRestaurant.setRestoPhotoUrl(lSetData);

        String lGetData = mRestaurant.getRestoPhotoUrl();
        assertNotNull(lGetData);
        assertEquals(lSetData,lGetData);
    }

    @Test
    public void setAndGetRestaurantLocationWithSuccess() {
        mRestaurant = new Restaurant(mRestoId,mRestoName,mRestoAddress,mRestoDistanceText,mRestoRating,
                mRestoPhotoUrl,mRestoLocation,mRestoOpeningHours,mRestoDistance,mRestoWkmList);
        assertNotNull(mRestaurant);
        assertNull(mRestaurant.getRestoLocation());

        mRestoLocation = new RestaurantDetailPojo.Location();
        assertNotNull(mRestoLocation);
        assertNull(mRestoLocation.getLat());

        RestaurantDetailPojo.Location lSetData = new RestaurantDetailPojo.Location(48.822779,2.411444399999999);
        mRestaurant.setRestoLocation(lSetData);

        RestaurantDetailPojo.Location lGetData = mRestaurant.getRestoLocation();
        assertEquals(lSetData,lGetData);
    }

    @Test
    public void setAndGetRestaurantOpeningHoursWithSuccess() {
        mRestaurant = new Restaurant(mRestoId,mRestoName,mRestoAddress,mRestoDistanceText,mRestoRating,
                mRestoPhotoUrl,mRestoLocation,mRestoOpeningHours,mRestoDistance,mRestoWkmList);
        assertNotNull(mRestaurant);
        assertNull(mRestaurant.getRestoOpeningHours());

        mRestoOpeningHours = new RestaurantDetailPojo.OpeningHours();
        assertNotNull(mRestoOpeningHours);
        assertNull(mRestoOpeningHours.getOpenNow());

        List<RestaurantDetailPojo.Period> lPeriodList = new ArrayList<>();
        lPeriodList.add(new RestaurantDetailPojo.Period(new RestaurantDetailPojo.Close(1,"2030"),new RestaurantDetailPojo.Open(1,"0800")));
        lPeriodList.add(new RestaurantDetailPojo.Period(new RestaurantDetailPojo.Close(2,"2030"),new RestaurantDetailPojo.Open(2,"0800")));
        lPeriodList.add(new RestaurantDetailPojo.Period(new RestaurantDetailPojo.Close(3,"2030"),new RestaurantDetailPojo.Open(3,"0800")));
        lPeriodList.add(new RestaurantDetailPojo.Period(new RestaurantDetailPojo.Close(4,"2030"),new RestaurantDetailPojo.Open(4,"0800")));
        lPeriodList.add(new RestaurantDetailPojo.Period(new RestaurantDetailPojo.Close(5,"2030"),new RestaurantDetailPojo.Open(5,"0800")));

        List<String> lWeekdayText = new ArrayList<>();

        lWeekdayText.add("Monday: 8:00 – 8:30 PM");
        lWeekdayText.add("Tuesday: 8:00 – 8:30 PM");
        lWeekdayText.add("Wednesday: 8:00 – 8:30 PM");
        lWeekdayText.add("Thursday: 8:00 – 8:30 PM");
        lWeekdayText.add("Friday: 8:00 – 8:30 PM");


        RestaurantDetailPojo.OpeningHours lSetData = new RestaurantDetailPojo.OpeningHours(true,lPeriodList,lWeekdayText);
        mRestaurant.setRestoOpeningHours(lSetData);

        RestaurantDetailPojo.OpeningHours lGetData = mRestaurant.getRestoOpeningHours();
        assertEquals(lSetData,lGetData);
        assertEquals(5,mRestaurant.getRestoOpeningHours().getPeriods().size());
        assertEquals(5,mRestaurant.getRestoOpeningHours().getWeekdayText().size());
    }

    @Test
    public void setAndGetRestaurantDistanceInMetersWithSuccess() {
        mRestaurant = new Restaurant(mRestoId,mRestoName,mRestoAddress,mRestoDistanceText,mRestoRating,
                mRestoPhotoUrl,mRestoLocation,mRestoOpeningHours,mRestoDistance,mRestoWkmList);
        assertEquals(mRestoDistance,mRestaurant.getRestoDistance() );

        int lSetData = 200;
        mRestaurant.setRestoDistance(lSetData);

        int lGetData = mRestaurant.getRestoDistance();
        assertEquals(lSetData, lGetData);
    }

    @Test
    public void setAndGetWorkmatesListComingToEatWithSuccess() {
        int lInitialNbWorkmates = mRestoWkmList.size();
        assertEquals(0, mRestoWkmList.size());

        mRestaurant = new Restaurant(mRestoId,mRestoName,mRestoAddress,mRestoDistanceText,mRestoRating,
                mRestoPhotoUrl,mRestoLocation,mRestoOpeningHours,mRestoDistance,mRestoWkmList);

        assertNotNull(mRestaurant);
        assertEquals(0,mRestaurant.getRestoWkList().size());

        Restaurant.WorkmatesList lSetWorkmate = new Restaurant.WorkmatesList("albert_id","albert_name","albert_url");
        mRestoWkmList.add(lSetWorkmate);
        assertEquals(lInitialNbWorkmates+1, mRestoWkmList.size());

        mRestaurant.setRestoWkList(mRestoWkmList);
        assertEquals(lInitialNbWorkmates+1, mRestaurant.getRestoWkList().size());

        Restaurant.WorkmatesList lGetWorkmate = mRestaurant.getRestoWkList().get(0);
        assertEquals(lSetWorkmate,lGetWorkmate);

        mRestoWkmList.remove(lSetWorkmate);
        mRestaurant.setRestoWkList(mRestoWkmList);
        assertEquals(lInitialNbWorkmates,mRestaurant.getRestoWkList().size());
    }

    @Test
    public void givenTwoRestaurantDistance_whenDifferent_thenReturnDifference() {
        mRestaurant = new Restaurant(mRestoId,mRestoName);
        int lSetData = 200;
        mRestaurant.setRestoDistance(lSetData);
        assertEquals(lSetData,mRestaurant.getRestoDistance() );

        Restaurant lRestaurant = new Restaurant("tata_id","tata eugenie");
        lSetData = 300;
        lRestaurant.setRestoDistance(lSetData);
        assertEquals(lSetData, lRestaurant.getRestoDistance());

        int lDistanceDifference = mRestaurant.compareTo(lRestaurant);
        assertEquals(100,lDistanceDifference);
    }

    @Test
    public void givenTwoRestaurantDistance_whenEquals_ThenReturnZero() {
        mRestaurant = new Restaurant(mRestoId,mRestoName);
        int lSetData = 200;
        mRestaurant.setRestoDistance(lSetData);
        assertEquals(lSetData,mRestaurant.getRestoDistance() );

        Restaurant lRestaurant = new Restaurant("tata_id","tata eugenie");
        lRestaurant.setRestoDistance(lSetData);
        assertEquals(lSetData, lRestaurant.getRestoDistance());

        int lDistanceDifference = mRestaurant.compareTo(lRestaurant);
        assertEquals(0,lDistanceDifference);
    }

    @Test
    public void setEmptyWorkmatesListConstructorWithSuccess() {
        mRestoWkm = new Restaurant.WorkmatesList();
        assertNotNull(mRestoWkmList);
        assertEquals(0,mRestoWkmList.size());
    }

    @Test
    public void setAndGetWorkmateIdFromWorkmatesListComingToEatWithSuccess() {
        mRestoWkm = new Restaurant.WorkmatesList();
        assertNull(mRestoWkm.getWkId());

        String lSetData = "albert_id";
        mRestoWkm.setWkId(lSetData);

        String lGetData = mRestoWkm.getWkId();
        assertNotNull(lGetData);
        assertEquals(lSetData, lGetData);
    }

    @Test
    public void setAndGetWorkmateNameFromWorkmatesListComingToEatWithSuccess() {
        mRestoWkm = new Restaurant.WorkmatesList();
        assertNull(mRestoWkm.getWkName());

        String lSetData = "albert";
        mRestoWkm.setWkName(lSetData);

        String lGetData = mRestoWkm.getWkName();
        assertNotNull(lGetData);
        assertEquals(lSetData, lGetData);
    }

    @Test
    public void setAndGetWorkmatePhotoUrlFromWorkmatesListComingToEatWithSuccess() {
        mRestoWkm = new Restaurant.WorkmatesList();
        assertNull(mRestoWkm.getWkUrl());

        String lSetData = "albert photo url";
        mRestoWkm.setWkUrl(lSetData);

        String lGetData = mRestoWkm.getWkUrl();
        assertNotNull(lGetData);
        assertEquals(lSetData, lGetData);
    }

}
