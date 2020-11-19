package com.fleb.go4lunch.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Florence LE BOURNOT on 15/11/2020
 */
public class RestaurantListViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();
    private RestaurantListViewModel mRestaurantListViewModel;

    @Mock
    public RestaurantRepository mRestaurantRepository;

    private MutableLiveData<List<Restaurant>> mNewLDRestaurantList = new MutableLiveData<>();

    private List<Restaurant> mRestaurantList = new ArrayList<>();

    @Before
    public void setup() {
        initMocks(this);
        mRestaurantListViewModel = new RestaurantListViewModel(mRestaurantRepository);
        mRestaurantList .add(new Restaurant("tataid", "tata"));
        mRestaurantList .add(new Restaurant("julesid", "jules"));
    }

    @Test
    public void getRestaurantListWithSuccess() {

        mNewLDRestaurantList.setValue(mRestaurantList);

        when(mRestaurantRepository.getLDRestaurantList()).thenReturn(mNewLDRestaurantList);
        assertNotNull(mRestaurantListViewModel.getRestaurantList());
        Mockito.verify(mRestaurantRepository).getLDRestaurantList();

        mRestaurantRepository.getLDRestaurantList()
                .observeForever(pRestaurantList -> assertEquals(pRestaurantList,mRestaurantList));
    }
}
