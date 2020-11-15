package com.fleb.go4lunch.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.viewmodel.RestaurantListViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

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

    @Mock
    private MutableLiveData<List<Restaurant>> mNewLDRestaurantList = new MutableLiveData<>();

    @Before
    public void setup() {
        initMocks(this);
        mRestaurantListViewModel = new RestaurantListViewModel();
    }

    @Test
    public void getRestauarntLIstWithSuccess() {
        when(mRestaurantRepository.getLDRestaurantList()).thenReturn(mNewLDRestaurantList);
        assertNotNull(mRestaurantListViewModel.getRestaurantList());
        Mockito.verify(mRestaurantRepository).getLDRestaurantList();

    }
}
