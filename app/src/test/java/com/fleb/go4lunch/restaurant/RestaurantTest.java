package com.fleb.go4lunch.restaurant;

import android.content.Context;

import com.fleb.go4lunch.di.DI;

import com.fleb.go4lunch.service.Go4LunchApi;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
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

    @BeforeEach
    public void setup() {
        mContextMocked = mock(Context.class);

        initMocks(this);
        mApi = DI.getGo4LunchApiService();
        assertThat(mApi, notNullValue());

    }

    @AfterEach
    public void tearDown() { mApi = DI.getNewInstanceApiService(); }

    @Test
    public void getRestaurant(){

        assertNull(contextMock);
        assertNull(mContextMocked);

    }
}
