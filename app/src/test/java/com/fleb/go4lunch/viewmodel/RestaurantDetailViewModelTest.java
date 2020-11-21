package com.fleb.go4lunch.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.RestaurantRepository;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.utils.ActionStatus;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Florence LE BOURNOT on 16/11/2020
 */
public class RestaurantDetailViewModelTest {
    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();
    private RestaurantDetailViewModel mRestaurantDetailViewModel;

    @Mock
    public RestaurantRepository mRestaurantRepository;

    @Mock
    public WorkmateRepository mWorkmateRepository;

    private MutableLiveData<Restaurant> mNewLDRestaurant = new MutableLiveData<>();

    private MutableLiveData<Workmate> mNewLDWorkmate = new MutableLiveData<>();

    private MutableLiveData<ActionStatus> mNewLDActionStatus = new MutableLiveData<>();

    private Restaurant mRestaurant;
    private Workmate mWorkmate;
    private ActionStatus mActionStatusSearch ;
    private ActionStatus mActionStatusSave;

    @Before
    public void setup() {
        initMocks(this);
        mRestaurantDetailViewModel = new RestaurantDetailViewModel(mRestaurantRepository, mWorkmateRepository);

        mRestaurant = new Restaurant("tataeugenieid", "tata eugnénie");
        mWorkmate = new Workmate("albertid", "Albert");

        mActionStatusSearch = ActionStatus.TO_SEARCH;
        mActionStatusSave = ActionStatus.TO_SAVE;
    }

    @Test
    public void getWorkmateDataWithSuccess() {
        mNewLDWorkmate.setValue(mWorkmate);

        when(mWorkmateRepository.getLDWorkmateData()).thenReturn(mNewLDWorkmate);
        assertNotNull(mRestaurantDetailViewModel.getWorkmateData());
        Mockito.verify(mWorkmateRepository).getLDWorkmateData();

        mWorkmateRepository.getLDWorkmateData()
                .observeForever(pWorkmate -> assertEquals(pWorkmate, mWorkmate));
    }


    @Test
    public void getRestaurantDetailWithSuccess() {
        mNewLDRestaurant.setValue(mRestaurant);

        when(mRestaurantRepository.getLDRestaurantDetail()).thenReturn(mNewLDRestaurant);
        assertNotNull(mRestaurantDetailViewModel.getRestaurantDetail());
        Mockito.verify(mRestaurantRepository).getLDRestaurantDetail();

        mRestaurantRepository.getLDRestaurantDetail()
                .observeForever(pRestaurant -> assertEquals(pRestaurant,mRestaurant));
    }

    @Test
    public void givenSearchChoice_whenRestaurantIsChosen_thenStatusIsChosen() {
        ActionStatus lActionStatusWaited = ActionStatus.IS_CHOSEN;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mActionStatusSearch);

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSearch)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSearchChoice_whenRestaurantIsNotChosen_thenStatusIsNotChosen() {
        ActionStatus lActionStatusWaited = ActionStatus.NOT_CHOSEN;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mActionStatusSearch);

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSearch)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSearchChoice_whenSearchFailed_thenStatusIsError() {
        ActionStatus lActionStatusWaited = ActionStatus.ERROR;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mActionStatusSearch);

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSearch)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSaveChoice_whenRestaurantIsChosen_thenStatusIsAdded() {
        ActionStatus lActionStatusWaited = ActionStatus.ADDED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave);

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSaveChoice_whenRestaurantIsNoMoreChosen_thenStatusIsRemoved() {
        ActionStatus lActionStatusWaited = ActionStatus.REMOVED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave);

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSaveChoice_whenRestaurantIsChosenAndOtherRestaurantIsAlreadyChosen_thenStatusIsAdded() {
        ActionStatus lActionStatusWaited = ActionStatus.ADDED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave);

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSaveChoice_whenSaveFailed_thenStatusIsSaveFailed() {
        ActionStatus lActionStatusWaited = ActionStatus.SAVED_FAILED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave);

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSaveChoice_whenSaveUnknownError_thenStatusIsError() {
        ActionStatus lActionStatusWaited = ActionStatus.ERROR;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave);

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mActionStatusSave)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }


    @Test
    public void givenSearchLike_whenRestaurantIsLiked_thenStatusIsChosen() {
        ActionStatus lActionStatusWaited = ActionStatus.IS_CHOSEN;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant(mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant(mActionStatusSearch);

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSearch)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }


    @Test
    public void givenSearchLike_whenRestaurantIsNotLiked_thenStatusIsNotChosen() {
        ActionStatus lActionStatusWaited = ActionStatus.NOT_CHOSEN;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant(mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant(mActionStatusSearch);

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSearch)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSearchLike_whenSearchFailed_thenStatusIsError() {
        ActionStatus lActionStatusWaited = ActionStatus.ERROR;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant(mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant(mActionStatusSearch);

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSearch)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSaveLike_whenRestaurantIsLiked_thenStatusIsAdded() {
        ActionStatus lActionStatusWaited = ActionStatus.ADDED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSave);

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSave)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSaveLike_whenRestaurantIsNoMoreLiked_thenStatusIsRemoved() {
        ActionStatus lActionStatusWaited = ActionStatus.REMOVED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSave);

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSave)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSaveLike_whenSaveFailed_thenStatusIsSaveFailed() {
        ActionStatus lActionStatusWaited = ActionStatus.SAVED_FAILED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSave);

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mActionStatusSave)
                .observeForever(pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }

    @Test
    public void givenSaveLike_whenSaveUnknownError_thenStatusIsError() {
        ActionStatus lActionStatusWaited = ActionStatus.ERROR;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant(mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant(mActionStatusSave);

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant(mActionStatusSave).observeForever(
                pActionStatus -> assertEquals(pActionStatus, lActionStatusWaited));
    }
}
