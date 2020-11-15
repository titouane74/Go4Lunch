package com.fleb.go4lunch.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.viewmodel.Go4LunchViewModel;

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
public class Go4LunchViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();
    private Go4LunchViewModel mGo4LunchListViewModel;

    @Mock
    public RestaurantRepository mRestaurantRepository;

    @Mock
    public WorkmateRepository mWorkmateRepository;

    @Mock
    private MutableLiveData<List<Restaurant>> mNewLDRestaurantList = new MutableLiveData<>();

    private List<Restaurant> mRestaurantList = new ArrayList<>();

    @Before
    public void setup() {
        initMocks(this);
        mGo4LunchListViewModel = new Go4LunchViewModel(mRestaurantRepository, mWorkmateRepository);

        mRestaurantList .add(new Restaurant("tataid", "tata"));
        mRestaurantList .add(new Restaurant("julesid", "jules"));
    }

    @Test
    public void getRestaurantListWithSuccess() {
        when(mRestaurantRepository.getLDRestaurantList()).thenReturn(mNewLDRestaurantList);
        assertNotNull(mGo4LunchListViewModel.getRestaurantList());
        Mockito.verify(mRestaurantRepository).getLDRestaurantList();

        mRestaurantRepository.getLDRestaurantList().observeForever(pRestaurantList ->
        {
            System.out.println(pRestaurantList.size());
            assertEquals(pRestaurantList,mNewLDRestaurantList);
            assertEquals(pRestaurantList,mRestaurantList);
        });
    }

}
