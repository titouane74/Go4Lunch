package com.fleb.go4lunch;

import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Florence LE BOURNOT on 14/11/2020
 */
public class Go4LunchHelperTest {

    @Before
    public void setup() {
    }

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

    //------------------------------DISTANCE----------------------------------//

    @Test
    public void givenDistance_whenUnder1000Meters_thenDisplayInMeterUnit() {
        assertEquals("100m",Go4LunchHelper.convertDistance(100));
    }

    //------------------------------DATE AND TIME----------------------------------//

    @Test
    public void getTheCurrentDayOfWeekWithSuccess() {
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int lExpectedDayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        assertEquals(lExpectedDayOfWeek,Go4LunchHelper.getCurrentDayInt());
    }

    @Test
    public void getTheShortWeekDayWithSuccess() {
        assertEquals("lun.",Go4LunchHelper.getDayString(1));
    }

    @Test
    public void getTheCurrentTimeFormattedHHmmWithSuccess() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        int lExpectedTime = Integer.parseInt(sdf.format(new Date()));
        assertEquals(lExpectedTime, Go4LunchHelper.getCurrentTime());
    }

    @Test
    public void getTheCurrentTimeFormattedInStringWithSuccess() {
        int lTime = 1500;
        String lExpectedTime = String.format("%02d:%02d", lTime / 100, lTime % 100);
        assertEquals(lExpectedTime, Go4LunchHelper.getCurrentTimeFormatted(lTime));
    }

    @Test
    public void givenTime_whenTimeWithoutMinutes_thenSucceed() {
        int lExpectedMinutes = 900;
        int lTime = 1500;
        assertEquals(lExpectedMinutes, Go4LunchHelper.convertTimeInMinutes(lTime));
    }

    @Test
    public void givenTime_whenTimeWithMinutes_thenSucceed() {
        int lExpectedMinutes = 930;
        int lTime = 1530;
        assertEquals(lExpectedMinutes, Go4LunchHelper.convertTimeInMinutes(lTime));
    }
    //---------------------------RESTAURANT DETAIL---------------------------------//

    @Test
    public void formatVicinityAddressWithSuccess() {
        assertEquals("10 rue des bois",Go4LunchHelper.formatAddress("10 rue des bois, Paris"));
    }

    @Test
    public void toBoundsPerimeterWithSuccess() {
        LatLngBounds lLatLngBoundsExpected = new LatLngBounds(new LatLng(48.813784989961,2.3977874699409685),new LatLng(48.83177139630744,2.425106232110535));

        LatLngBounds lLatLngBoundsRetrieved = Go4LunchHelper.toBounds(new LatLng(48.822779,2.411444399999999), 1000);
        assertEquals(lLatLngBoundsExpected, lLatLngBoundsRetrieved);
    }
}
