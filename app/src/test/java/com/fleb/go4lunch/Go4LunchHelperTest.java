package com.fleb.go4lunch;

import com.fleb.go4lunch.utils.Go4LunchHelper;


import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Florence LE BOURNOT on 14/11/2020
 */
public class Go4LunchHelperTest {

    @BeforeEach
    public void setup() {

        initMocks(this);

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

    //------------------------------DATE AND TIME----------------------------------//

    @Test
    public void getTheCurrentDayOfWeekWithSuccess() { }

    @Test
    public void getTheShortWeekDayWithSuccess() { }

    @Test
    public void getTheCurrentTimeFormattedHHmmWithSuccess() { }

    @Test
    public void getTheCurrentTimeFormattedInStringWithSuccess() { }

    @Test
    public void convertTimeIntoMinutesWithSuccess() { }

    //---------------------------RESTAURANT DETAIL---------------------------------//

    @Test
    public void formatVicinityAddressWithSuccess() { }

    @Test
    public void getPhotoFromGoogle() { }

}
