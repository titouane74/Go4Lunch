package com.fleb.go4lunch.workmate;

import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.service.Go4LunchApi;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Florence LE BOURNOT on 13/11/2020
 */
public class WorkmateTest {

    private Go4LunchApi mApi;

    @BeforeEach
    public void setup() {
        initMocks(this);
        mApi = DI.getGo4LunchApiService();
        assertThat(mApi, notNullValue());

    }

    @AfterEach
    public void tearDown() { mApi = DI.getNewInstanceApiService(); }

    @Test
    public void setAndGetWorkmateIdWithSuccess() { }

    @Test
    public void setAndGetWorkmateNameWithSuccess() { }

    @Test
    public void setAndGetWorkmateEmailWithSuccess() { }

    @Test
    public void setAndGetWorkmatePhotoUrlWithSuccess() { }

    @Test
    public void setAndGetWorkmateLikesWithSuccess() { }

    @Test
    public void setAndGetWorkmateRestoChosenWithSuccess() { }

    @Test
    public void setAndGetWorkmateLikesRestoIdWithSuccess() { }

    @Test
    public void setAndGetWorkmateLikesRestoNameWithSuccess() { }

    @Test
    public void setAndGetWorkmateLikesRestoDateChoiceWithSuccess() { }

    @Test
    public void setAndGetWorkmateRestoChosenRestoIdWithSuccess() { }

    @Test
    public void setAndGetWorkmateRestoChosenRestoNameWithSuccess() { }

    @Test
    public void setAndGetWorkmateRestoChosenRestoDateChoiceWithSuccess() { }

}
