package com.fleb.go4lunch.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.utils.ActionStatus;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Florence LE BOURNOT on 16/11/2020
 */
public class SettingsViewModelTest {
    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private SettingsViewModel mSettingsViewModel;

    @Mock
    public WorkmateRepository mWorkmateRepository;

    private MutableLiveData<Workmate> mNewLDWorkmate = new MutableLiveData<>();
    private MutableLiveData<ActionStatus> mNewLDActionStatus = new MutableLiveData<>();
    private Workmate mWorkmate;

    @Before
    public void setUp() {
        initMocks(this);
        mSettingsViewModel = new SettingsViewModel(mWorkmateRepository);

        mWorkmate = new Workmate("albertid", "Albert");
    }

    @Test
    public void getWorkmateData() {
        mNewLDWorkmate.setValue(mWorkmate);

        when(mWorkmateRepository.getLDWorkmateData()).thenReturn(mNewLDWorkmate);
        assertNotNull(mSettingsViewModel.getWorkmateData());
        Mockito.verify(mWorkmateRepository).getLDWorkmateData();

        mWorkmateRepository.getLDWorkmateData()
                .observeForever(pWorkmate -> assertEquals(pWorkmate, mWorkmate));
    }

    @Test
    public void givenNewUserName_whenUpdateWorkmateUserName_thenStatusIsSaved() {
        String lNewName = "Albert Dupont";
        ActionStatus lActionStatusWaited = ActionStatus.SAVED;

        mNewLDActionStatus.setValue(ActionStatus.SAVED);

        when(mWorkmateRepository.updateLDWorkmateUserName(lNewName)).thenReturn(mNewLDActionStatus);
        assertNotNull(mSettingsViewModel.updateWorkmateUserName(lNewName));
        Mockito.verify(mWorkmateRepository).updateLDWorkmateUserName(lNewName);

        mWorkmateRepository.updateLDWorkmateUserName(lNewName)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenNewUserName_whenUpdateWorkmateUserNameFailed_thenStatusIsSavedFailed() {
        String lNewName = "Albert Dupont";
        ActionStatus lActionStatusWaited = ActionStatus.SAVED_FAILED;

        mNewLDActionStatus.setValue(ActionStatus.SAVED_FAILED);

        when(mWorkmateRepository.updateLDWorkmateUserName(lNewName)).thenReturn(mNewLDActionStatus);
        assertNotNull(mSettingsViewModel.updateWorkmateUserName(lNewName));
        Mockito.verify(mWorkmateRepository).updateLDWorkmateUserName(lNewName);

        mWorkmateRepository.updateLDWorkmateUserName(lNewName)
                .observeForever(pActionStatus ->  assertEquals(pActionStatus, lActionStatusWaited));
    }
}