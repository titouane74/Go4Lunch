package com.fleb.go4lunch.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;

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
 * Created by Florence LE BOURNOT on 16/11/2020
 */
public class MainActivityViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();
    private MainActivityViewModel mMainActivityViewModel;

    @Mock
    public RestaurantRepository mRestaurantRepository;

    @Mock
    public WorkmateRepository mWorkmateRepository;

    @Mock
    private MutableLiveData<List<Restaurant>> mNewLDRestaurantList = new MutableLiveData<>();

    @Mock
    private MutableLiveData<Workmate> mNewLDWorkmate = new MutableLiveData<>();

    private List<Restaurant> mRestaurantList = new ArrayList<>();
    private Workmate mWorkmate;


    @Before
    public void setup() {
        initMocks(this);
        mMainActivityViewModel = new MainActivityViewModel(mRestaurantRepository, mWorkmateRepository);

        mRestaurantList .add(new Restaurant("tataeugenieid", "tata eugnénie"));
        mRestaurantList .add(new Restaurant("julesid", "jules"));
        mRestaurantList .add(new Restaurant("tatafaridaid", "tata farida"));

        mWorkmate = new Workmate("albertid", "Albert");

    }

    @Test
    public void getRestaurantListWithSuccess() {

        mNewLDRestaurantList.postValue(mRestaurantList);

        when(mRestaurantRepository.getLDRestaurantList()).thenReturn(mNewLDRestaurantList);
        assertNotNull(mMainActivityViewModel.getRestaurantList());
        Mockito.verify(mRestaurantRepository).getLDRestaurantList();

        mNewLDRestaurantList.postValue(mRestaurantList);

        System.out.println("hors obeserve " + mRestaurantList.size());
        mRestaurantRepository.getLDRestaurantList().observeForever(pRestaurantList ->
        {
            System.out.println("dans observe " + pRestaurantList.size());
            assertEquals(pRestaurantList,mRestaurantList);
        });
        mNewLDRestaurantList.postValue(mRestaurantList);
    }

    @Test
    public void getWorkmateInfosWithSuccess() {
        mNewLDWorkmate.setValue(mWorkmate);

        when(mWorkmateRepository.getLDWorkmateData(mWorkmate.getWorkmateId())).thenReturn(mNewLDWorkmate);
        assertNotNull(mMainActivityViewModel.getWorkmateInfos(mWorkmate.getWorkmateId()));
        Mockito.verify(mWorkmateRepository).getLDWorkmateData(mWorkmate.getWorkmateId());

        mNewLDWorkmate.setValue(mWorkmate);

        System.out.println("hors observe " + mWorkmate.getWorkmateId());

        mWorkmateRepository.getLDWorkmateData(mWorkmate.getWorkmateId()).observeForever(pWorkmate -> {
            System.out.println("dans observe " + pWorkmate.getWorkmateId());
            assertEquals(pWorkmate, mWorkmate);
        });
        mNewLDWorkmate.setValue(mWorkmate);
    }

}
