package com.fleb.go4lunch.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.WorkmateRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Florence LE BOURNOT on 16/11/2020
 */
public class WorkmateListViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();
    private WorkmateListViewModel mWorkmateListViewModel;

    @Mock
    public WorkmateRepository mWorkmateRepository;

    private MutableLiveData<List<Workmate>> mNewLDWorkmateList = new MutableLiveData<>();

    private List<Workmate> mWorkmateList = new ArrayList<>();

    @Before
    public void setUp()  {
        initMocks(this);
        mWorkmateListViewModel = new WorkmateListViewModel(mWorkmateRepository);
        mWorkmateList.add(new Workmate("tataid", "tata"));
        mWorkmateList.add(new Workmate("julesid", "jules"));
    }

    @Test
    public void getWorkmateListWithSuccess() {
        mNewLDWorkmateList.setValue(mWorkmateList);

        when(mWorkmateRepository.getLDWorkmateListData()).thenReturn(mNewLDWorkmateList);
        assertNotNull(mWorkmateListViewModel.getWorkmateList());
        Mockito.verify(mWorkmateRepository).getLDWorkmateListData();

        mWorkmateRepository.getLDWorkmateListData().observeForever(pWorkmateList ->
        {
            assertEquals(pWorkmateList,mWorkmateList);
        });

    }
}