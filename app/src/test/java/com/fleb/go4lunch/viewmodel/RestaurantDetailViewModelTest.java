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

    @Mock
    private MutableLiveData<Restaurant> mNewLDRestaurant = new MutableLiveData<>();

    @Mock
    private MutableLiveData<Workmate> mNewLDWorkmate = new MutableLiveData<>();

    @Mock
    private MutableLiveData<ActionStatus> mNewLDActionStatus;

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

        when(mWorkmateRepository.getLDWorkmateData(mWorkmate.getWorkmateId())).thenReturn(mNewLDWorkmate);
        assertNotNull(mRestaurantDetailViewModel.getWorkmateData(mWorkmate.getWorkmateId()));
        Mockito.verify(mWorkmateRepository).getLDWorkmateData(mWorkmate.getWorkmateId());

        System.out.println("hors observe " + mWorkmate.getWorkmateId());

        mWorkmateRepository.getLDWorkmateData(mWorkmate.getWorkmateId()).observeForever(pWorkmate -> {
            System.out.println("dans observe " + pWorkmate.getWorkmateId());
            assertEquals(pWorkmate, mWorkmate);
        });
    }


    @Test
    public void getRestaurantDetailWithSuccess() {
        mNewLDRestaurant.postValue(mRestaurant);

        when(mRestaurantRepository.getLDRestaurantDetail(mRestaurant.getRestoPlaceId())).thenReturn(mNewLDRestaurant);
        assertNotNull(mRestaurantDetailViewModel.getRestaurantDetail(mRestaurant.getRestoPlaceId()));
        Mockito.verify(mRestaurantRepository).getLDRestaurantDetail(mRestaurant.getRestoPlaceId());

        System.out.println("hors observe " + mRestaurant.getRestoName());
        mRestaurantRepository.getLDRestaurantDetail(mRestaurant.getRestoPlaceId()).observeForever(pRestaurant ->
        {
            System.out.println("dans observe " +mRestaurant.getRestoName());
            assertEquals(pRestaurant,mRestaurant);
        });

    }

    @Test
    public void givenSearchChoice_whenRestaurantIsChosen_thenStatusIsChosen() {
        ActionStatus lActionStatusWaited = ActionStatus.IS_CHOSEN;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mRestaurant, mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSearch);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSearch).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });
    }

    @Test
    public void givenSearchChoice_whenRestaurantIsNotChosen_thenStatusIsNotChosen() {
        ActionStatus lActionStatusWaited = ActionStatus.NOT_CHOSEN;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mRestaurant, mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSearch);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSearch).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });
    }

    @Test
    public void givenSearchChoice_whenSearchFailed_thenStatusIsError() {
        ActionStatus lActionStatusWaited = ActionStatus.ERROR;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mRestaurant, mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSearch);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSearch).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });
    }

    @Test
    public void givenSaveChoice_whenRestaurantIsChosen_thenStatusIsAdded() {
        ActionStatus lActionStatusWaited = ActionStatus.ADDED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mRestaurant, mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });

    }

    @Test
    public void givenSaveChoice_whenRestaurantIsNoMoreChosen_thenStatusIsRemoved() {
        ActionStatus lActionStatusWaited = ActionStatus.REMOVED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mRestaurant, mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });

    }

    @Test
    public void givenSaveChoice_whenRestaurantIsChosenAndOtherRestaurantIsAlreadyChosen_thenStatusIsAdded() {
        ActionStatus lActionStatusWaited = ActionStatus.ADDED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mRestaurant, mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });

    }

    @Test
    public void givenSaveChoice_whenSaveFailed_thenStatusIsSaveFailed() {
        ActionStatus lActionStatusWaited = ActionStatus.SAVED_FAILED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mRestaurant, mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });

    }

    @Test
    public void givenSaveChoice_whenSaveUnknownError_thenStatusIsError() {
        ActionStatus lActionStatusWaited = ActionStatus.ERROR;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateChoiceForRestaurant(mRestaurant, mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateRestaurantChoice(mRestaurant, mActionStatusSave).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });

    }


    @Test
    public void givenSearchLike_whenRestaurantIsLiked_thenStatusIsChosen() {
        ActionStatus lActionStatusWaited = ActionStatus.IS_CHOSEN;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant(mRestaurant,mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mRestaurant,mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant(mRestaurant, mActionStatusSearch);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSearch).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });

    }


    @Test
    public void givenSearchLike_whenRestaurantIsNotLiked_thenStatusIsNotChosen() {
        ActionStatus lActionStatusWaited = ActionStatus.NOT_CHOSEN;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant(mRestaurant, mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mRestaurant, mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant(mRestaurant, mActionStatusSearch);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSearch).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });

    }

    @Test
    public void givenSearchLike_whenSearchFailed_thenStatusIsError() {
        ActionStatus lActionStatusWaited = ActionStatus.ERROR;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant(mRestaurant, mActionStatusSearch)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mRestaurant, mActionStatusSearch));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant(mRestaurant, mActionStatusSearch);


        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSearch).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });
    }

    @Test
    public void givenSaveLike_whenRestaurantIsLiked_thenStatusIsAdded() {
        ActionStatus lActionStatusWaited = ActionStatus.ADDED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mRestaurant, mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSave);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSave).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });

    }

    @Test
    public void givenSaveLike_whenRestaurantIsNoMoreLiked_thenStatusIsRemoved() {
        ActionStatus lActionStatusWaited = ActionStatus.REMOVED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mRestaurant, mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSave);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSave).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });

    }

    @Test
    public void givenSaveLike_whenSaveFailed_thenStatusIsSaveFailed() {
        ActionStatus lActionStatusWaited = ActionStatus.SAVED_FAILED;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mRestaurant, mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSave);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSave).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });

    }

    @Test
    public void givenSaveLike_whenSaveUnknownError_thenStatusIsError() {
        ActionStatus lActionStatusWaited = ActionStatus.ERROR;

        mNewLDActionStatus.setValue(lActionStatusWaited);

        when(mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSave)).thenReturn(mNewLDActionStatus);
        assertNotNull(mRestaurantDetailViewModel.getOrSaveWorkmateLikeForRestaurant(mRestaurant, mActionStatusSave));
        Mockito.verify(mWorkmateRepository).getOrSaveLDWorkmateLikeForRestaurant( mRestaurant, mActionStatusSave);

        System.out.println("hors observe " );

        mWorkmateRepository.getOrSaveLDWorkmateLikeForRestaurant(mRestaurant, mActionStatusSave).observeForever(pActionStatus -> {
            System.out.println("dans observe " );
            assertEquals(pActionStatus, lActionStatusWaited);
        });
    }

}
