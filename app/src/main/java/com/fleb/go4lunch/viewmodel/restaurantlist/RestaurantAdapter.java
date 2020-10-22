package com.fleb.go4lunch.viewmodel.restaurantlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.DayOpeningHours;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.RestaurantDetailPojo;
import com.fleb.go4lunch.utils.GsonHelper;
import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.fleb.go4lunch.view.activities.RestaurantDetailActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fleb.go4lunch.repository.RestaurantRepository.PREF_KEY_LATITUDE;
import static com.fleb.go4lunch.repository.RestaurantRepository.PREF_KEY_LONGITUDE;
import static com.fleb.go4lunch.utils.PreferencesHelper.mPreferences;

/**
 * Created by Florence LE BOURNOT on 26/09/2020
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder> {

    /**
     * Firebase declarations
     */
    public static final String RESTO_COLLECTION = "Restaurant";
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mRestoRef = mDb.collection(RESTO_COLLECTION);

    private static final String TAG_LIST_RESTO = "TAG_LIST_RESTO";
    private static final String TAG_WEEKDAY = "TAG_WEEKDAY";
    private static final String TAG = "TAG_STATUS";
    private List<Restaurant> mRestoList;
    private double mLatitude;
    private double mLongitude;

    private DayOpeningHours.DayService mService = new DayOpeningHours.DayService();

    public void setRestoList(List<Restaurant> pRestoList) {
        mRestoList = pRestoList;
    }

    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_restaurant_list_item,
                parent, false);
        return new RestaurantHolder(lView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RestaurantHolder pRestoHolder, int position) {

        Context lContext = pRestoHolder.itemView.getContext();

        mLatitude = Double.parseDouble(Objects.requireNonNull(mPreferences.getString(PREF_KEY_LATITUDE, null)));
        mLongitude = Double.parseDouble(Objects.requireNonNull(mPreferences.getString(PREF_KEY_LONGITUDE, null)));

        Location lFusedLocationProvider = Go4LunchHelper.setCurrentLocation(mLatitude, mLongitude);
        int lDistance = Go4LunchHelper.getRestaurantDistanceToCurrentLocation(lFusedLocationProvider,
                mRestoList.get(position).getRestoLocation());

        String lNewDistance = Go4LunchHelper.convertDistance(lDistance);

        pRestoHolder.mRestoName.setText(mRestoList.get(position).getRestoName());
        pRestoHolder.mRestoDistance.setText(lNewDistance);
        pRestoHolder.mRestoAddress.setText(Go4LunchHelper.formatAddress(mRestoList.get(position).getRestoAddress()));
        pRestoHolder.mRestoNbWorkmates.setText("(" + mRestoList.get(position).getRestoNbWorkmates() + ")");

        if (mRestoList.get(position).getRestoOpeningHours() != null) {
            Log.d(TAG, "resto name : " + mRestoList.get(position).getRestoName());

            DayOpeningHours lOpeningStatus = getRestaurantOpeningHoursStatus(lContext, mRestoList.get(position));
            if (lOpeningStatus.isDayIsOpen()) {
                pRestoHolder.mRestoOpening.setText(lOpeningStatus.getDayDescription());
                pRestoHolder.mRestoOpening.setTextColor(lContext.getResources().getColor(R.color.colorTextBlack));
                pRestoHolder.mRestoOpening.setTypeface(null, Typeface.ITALIC);
            } else {
                pRestoHolder.mRestoOpening.setText(lOpeningStatus.getDayDescription());
                pRestoHolder.mRestoOpening.setTextColor(lContext.getResources().getColor(R.color.colorTextRed));
                pRestoHolder.mRestoOpening.setTypeface(null, Typeface.BOLD);
            }
        }

        int lNbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay(lContext,
                mRestoList.get(position).getRestoRating());
        switch (lNbStarToDisplay) {
            case 1:
                Log.d(TAG_LIST_RESTO, "onBindViewHolder: 2 stars to hide : ");
                pRestoHolder.mRestoNote2.setVisibility(View.INVISIBLE);
                pRestoHolder.mRestoNote3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                Log.d(TAG_LIST_RESTO, "onBindViewHolder: 1 star to hide : ");
                pRestoHolder.mRestoNote3.setVisibility(View.INVISIBLE);
                break;
            case 3:
                Log.d(TAG_LIST_RESTO, "onBindViewHolder: no star to hide : ");
                break;
            default:
                Log.d(TAG_LIST_RESTO, "onBindViewHolder: 3 stars to hide : ");
                pRestoHolder.mRestoNote1.setVisibility(View.INVISIBLE);
                pRestoHolder.mRestoNote2.setVisibility(View.INVISIBLE);
                pRestoHolder.mRestoNote3.setVisibility(View.INVISIBLE);
                break;
        }

        if (mRestoList.get(position).getRestoPhotoUrl() != null) {
            Glide.with(pRestoHolder.mRestoImage.getContext())
                    .load(mRestoList.get(position).getRestoPhotoUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .into(pRestoHolder.mRestoImage);
        }

        pRestoHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lIntentRestoDetail = new Intent(lContext, RestaurantDetailActivity.class);
                String lJsonRestaurant = GsonHelper.getGsonString(mRestoList.get(position));
                lIntentRestoDetail.putExtra("placeid", mRestoList.get(position).getRestoPlaceId());
                lIntentRestoDetail.putExtra("restaurant", lJsonRestaurant);
                lContext.startActivity(lIntentRestoDetail);
            }
        });
    }

    private DayOpeningHours getRestaurantOpeningHoursStatus(Context pContext, Restaurant pRestaurant) {

        int lCurrentDay = 0;

        RestaurantDetailPojo.OpeningHours lRestoHoursList = pRestaurant.getRestoOpeningHours();
        List<DayOpeningHours> lDayHourList = new ArrayList<>();
        DayOpeningHours lStatus = new DayOpeningHours();

        //Get the current number day
        // -1 is for corresponding to the period which start on a sunday with a 0 value
        lCurrentDay = Go4LunchHelper.getCurrentDayInt() - 1;

        //Get the list of the opening hours of the current day and the next day
        if (lRestoHoursList.getPeriods().size() > 0) {
            lDayHourList = getOpeningHourOfTheCurrentAndNextDay(lRestoHoursList, lCurrentDay);

            lStatus.setDayIsOpen(false);
            lStatus.setDayNumber(lCurrentDay);

            //Compare the current time to the opening hours to determine the status of the restaurant : open or closed
            lStatus = getRestaurantStatusDescription(pContext, lDayHourList, lStatus);
        }
        return lStatus;
    }

    private List<DayOpeningHours> getOpeningHourOfTheCurrentAndNextDay(RestaurantDetailPojo.OpeningHours pRestoHoursList,
                                                                       int pCurrentDay) {
        List<DayOpeningHours> lDayHourList = new ArrayList<>();
        int lNumService = 0;

        lDayHourList = getCurrentDayService(lDayHourList, pRestoHoursList, pCurrentDay);

        if (lDayHourList.size() == 0) {
            Log.d(TAG, "current day n'est pas un jour ouvert : ");
            lDayHourList = addServiceList(lDayHourList, lNumService, pRestoHoursList, 99, 9);
        }

        lDayHourList = searchNextOpenDay(pRestoHoursList, lDayHourList,pCurrentDay + 1);

        return lDayHourList;
    }



    private List<DayOpeningHours> getCurrentDayService(List<DayOpeningHours> pDayHourList,
                                                       RestaurantDetailPojo.OpeningHours pRestoHoursList,
                                                       int pDay) {

        for (int lIndex = 0; lIndex < pRestoHoursList.getPeriods().size(); lIndex++) {
            int lOpenDay = pRestoHoursList.getPeriods().get(lIndex).getOpen().getDay();
            if (lOpenDay == pDay) {
                Log.d(TAG, "Current day : service du jour ajouté à la liste " + lOpenDay);
                pDayHourList = addServiceList(pDayHourList, 0, pRestoHoursList, lIndex, lOpenDay);
            }
        }
        return pDayHourList;
    }

    private List<DayOpeningHours> searchNextOpenDay(RestaurantDetailPojo.OpeningHours pRestoHoursList, List<DayOpeningHours> pDayHourList,
                                                    int pDay) {
        int lIndex;
        for (lIndex = 0; lIndex < pRestoHoursList.getPeriods().size(); lIndex++) {
            if (pRestoHoursList.getPeriods().get(lIndex).getOpen().getDay() >= pDay) {
                Log.d(TAG, "searchNextOpenDay: " + lIndex + " day " + pRestoHoursList.getPeriods().get(lIndex).getOpen().getDay());

                return searchNextServices(pRestoHoursList, lIndex, pRestoHoursList.getPeriods().get(lIndex).getOpen().getDay(),
                        pDayHourList, 0);
            }
        }
        return pDayHourList;
    }

    private List<DayOpeningHours> addServiceList(List<DayOpeningHours> pDayHourList,
                                                 int pNumService,
                                                 RestaurantDetailPojo.OpeningHours pRestoHoursList,
                                                 int pIndex, int pOpenDay) {

        int lCloseHour = 0;
        int lOpenHour = 0;
        if (pIndex != 99) {
            lCloseHour = Integer.parseInt(pRestoHoursList.getPeriods().get(pIndex).getClose().getTime());
            lOpenHour = Integer.parseInt(pRestoHoursList.getPeriods().get(pIndex).getOpen().getTime());
        }

        DayOpeningHours.DayService lService = new DayOpeningHours.DayService(pNumService, lCloseHour, lOpenHour, pOpenDay);
        Log.d(TAG, "premier service à vide ajouté : ");

        pDayHourList.add(new DayOpeningHours(pOpenDay, pNumService, lService,
                lCloseHour, lOpenHour, false, null));

        return pDayHourList;
    }

    private List<DayOpeningHours> searchNextServices(RestaurantDetailPojo.OpeningHours pRestoHoursList, int pIndex, int pOpenDay,
                                                     List<DayOpeningHours> pDayHourList,
                                                     int pNumService) {
        pIndex++;

        //Find the next service which is not on the same day
        if (pOpenDay == 6) {
            Log.d(TAG, "prochain service est un dimanche: index = 0");
            pIndex = 0;
        } else {
            Log.d(TAG, "prochain service n'est pas un dimanche");
        }
        Log.d(TAG, "jour du premier service suivant: " + pRestoHoursList.getPeriods().get(pIndex).getOpen().getDay());

        pDayHourList = addServiceList(pDayHourList, pNumService, pRestoHoursList, pIndex, pOpenDay);
        Log.d(TAG, "service suivant ajouté à la liste: ");

        return pDayHourList;
    }

    private DayOpeningHours getRestaurantStatusDescription(Context pContext, List<DayOpeningHours> pDayHourList, DayOpeningHours pStatus) {

        DayOpeningHours.DayService lService1 = new DayOpeningHours.DayService();
        DayOpeningHours.DayService lService2 = new DayOpeningHours.DayService();
        DayOpeningHours.DayService lService3 = new DayOpeningHours.DayService();

        //Identificate the number of services
        int lNbMaxService = 0;
        if (pDayHourList.size() > 0) {
            lNbMaxService = pDayHourList.size();
        }
        Log.d(TAG, "lNbMaxService: " + lNbMaxService);

        switch (lNbMaxService) {
            case 1:
                lService1 = pDayHourList.get(0).getDayService();
                Log.d(TAG, "open service 1: ");
                break;
            case 2:
                lService1 = pDayHourList.get(0).getDayService();
                lService2 = pDayHourList.get(1).getDayService();
                Log.d(TAG, "open services 1 and 2: ");
                break;
            case 3:
                lService1 = pDayHourList.get(0).getDayService();
                lService2 = pDayHourList.get(1).getDayService();
                lService3 = pDayHourList.get(2).getDayService();
                Log.d(TAG, "open services 1,2 and 3: ");
                break;
            default:
                //lCase = 0; //Close today
                Log.d(TAG, "it's a closed day: ");
        }
        pStatus = identifyDayCase(pContext, pStatus, lService1, lService2, lService3, lNbMaxService);
        return pStatus;
    }

    private DayOpeningHours identifyDayCase(Context pContext, DayOpeningHours pStatus, DayOpeningHours.DayService pService1,
                                            DayOpeningHours.DayService pService2, DayOpeningHours.DayService pService3,
                                            int pNbMaxService) {
        int lCase = 0;
        int lTime = 0;
        //Get the current time
        int lCurrentTime = Go4LunchHelper.getCurrentTime();

        if ((pService1.getOpenTime() == 0) && (pService1.getCloseTime() == 0)) {
            Log.d(TAG, "1 : closed today ");
            Log.d(TAG, "1 - Serv1 OP " + pService1.getOpenTime() + " Serv1 CT" + pService1.getCloseTime());
            //lCase is by default at 0 in Closed today
            lCase = 0; //Close today
        } else if (((pNbMaxService == 1) && (pService1.getCloseTime() < lCurrentTime))
                // 1 && 2030 < 2200
                || ((pNbMaxService == 2) && (pService2.getCloseTime() < lCurrentTime))
            // 2 && 2145 < 2300
        ) {
            if (pNbMaxService == 1) {
                Log.d(TAG, "2 - Serv1 OP " + pService1.getOpenTime() + " Serv1 CT" + pService1.getCloseTime());
            } else if (pNbMaxService == 2) {
                Log.d(TAG, "3 - Serv2 OP " + pService2.getOpenTime() + " Serv2 CT" + pService2.getCloseTime());
            } else {
                Log.d(TAG, "error: 1");
            }
            lCase = 1; //Closed
            Log.d(TAG, "2 : closed : MaxServ" + pNbMaxService + " curTime" + lCurrentTime);
        } else if ((lCurrentTime < pService1.getOpenTime())
            // 1 && 0500 < 0700
            // 2 && 0500 < 1200
            // 3 && 0500 < 1200
        ) {
            Log.d(TAG, "3 : closed. Open at : MaxServ" + pNbMaxService + " curTime" + lCurrentTime);
            Log.d(TAG, "4 - Serv1 OP " + pService1.getOpenTime() + " Serv1 CT" + pService1.getCloseTime());
            lCase = 2; //Closed. Open at
            lTime = pService1.getOpenTime();
        } else if (((pNbMaxService == 2) || (pNbMaxService == 3))
                && (pService1.getCloseTime() < lCurrentTime) && (lCurrentTime < pService2.getOpenTime())
            // 2 && 1345 < 1500 && 1500 < 1930
            // 3 && 1430 < 1500 && 1500 < 1900
        ) {
            if (pNbMaxService == 2) {
                Log.d(TAG, "5 - Serv1 OP " + pService1.getOpenTime() + " Serv1 CT" + pService1.getCloseTime());
                Log.d(TAG, "6 - Serv2 OP " + pService2.getOpenTime() + " Serv2 CT" + pService2.getCloseTime());
            } else if (pNbMaxService == 3) {
                Log.d(TAG, "7 - Serv3 OP " + pService3.getOpenTime() + " Serv3 CT" + pService3.getCloseTime());
            } else {
                Log.d(TAG, "error: 2");
            }
            Log.d(TAG, "4 : closed. Open at : MaxServ" + pNbMaxService + " curTime" + lCurrentTime);
            lCase = 2; //Closed. Open at
            lTime = pService2.getOpenTime();
        } else if ((pNbMaxService == 3) &&
                ((pService2.getCloseTime() < lCurrentTime) && (pService3.getOpenTime() < pService2.getCloseTime()))
                // 3 && 2230 < 2300
                || ((lCurrentTime < pService3.getOpenTime()) && (pService3.getOpenTime() < pService2.getCloseTime()))
            // 3 && 0100 < 1200
        ) {
            Log.d(TAG, "4 : closed. Open at : MaxServ" + pNbMaxService + " curTime" + lCurrentTime);
            if (pNbMaxService == 3) {
                Log.d(TAG, "8 - Serv2 OP " + pService2.getOpenTime() + " Serv2 CT" + pService2.getCloseTime());
                Log.d(TAG, "9 - Serv3 OP " + pService3.getOpenTime() + " Serv3 CT" + pService3.getCloseTime());
            } else {
                Log.d(TAG, "error: 3");
            }
            lCase = 2; //Closed. Open at
            lTime = pService3.getOpenTime();

        } else if ((pService1.getOpenTime() < lCurrentTime) && (lCurrentTime < pService1.getCloseTime())
        ) {
            //1 && 0700 < 1200 && 1200 < 2030
            //2 && 1200 < 1300 && 1300 < 1345
            //3 && 1200 < 1300 && 1300 < 1430
            Log.d(TAG, "5 : Open until : MaxServ" + pNbMaxService + " curTime" + lCurrentTime);
            Log.d(TAG, "10 - Serv1 OP " + pService1.getOpenTime() + " Serv1 CT" + pService1.getCloseTime());
            lCase = 3; //Open until
            lTime = pService1.getCloseTime();
            pStatus.setDayIsOpen(true);
        } else if (((pNbMaxService == 2) || (pNbMaxService == 3))
                && (pService2.getOpenTime() < lCurrentTime) && (lCurrentTime < pService2.getCloseTime())) {
            //2 && 1930 < 2000 && 2000 < 2145
            //3 && 1900 < 2000 && 2000 < 2230
            Log.d(TAG, "6 : Open until : MaxServ" + pNbMaxService + " curTime" + lCurrentTime);
            if ((pNbMaxService == 2) || (pNbMaxService == 3)) {
                Log.d(TAG, "11 - Serv2 OP " + pService2.getOpenTime() + " Serv2 CT" + pService2.getCloseTime());
            } else {
                Log.d(TAG, "error: 4");
            }
            lCase = 3; //Open. Closed at
            lTime = pService2.getCloseTime();
            pStatus.setDayIsOpen(true);
        } else if (pNbMaxService != 0) {
            Log.d(TAG, "7 : Closed : MaxServ" + pNbMaxService + " curTime" + lCurrentTime);
            lCase = 1; //Closed
        }

        //Determine the text description in function of the case
        String lStringTime = Go4LunchHelper.getCurrentTimeFormatted(lTime);
        pStatus.setDayDescription(getTextDescription(pContext, lCase, lStringTime));
        Log.d(TAG, "getTextDescription : " + getTextDescription(pContext, lCase, lStringTime));

        return pStatus;
    }

    private String getTextDescription(Context pContext, int pCase, String pTime) {
        switch (pCase) {
            case 0:
                return pContext.getResources().getString(R.string.text_resto_closed_today);
            case 1:
                return pContext.getResources().getString(R.string.text_resto_closed);
            case 2:
                return pContext.getResources().getString(R.string.text_resto_closed_open_at) + " " + pTime;
            case 3:
                return pContext.getResources().getString(R.string.text_resto_open_until) + " " + pTime;
            default:
                return "";
        }
    }

    @Override
    public int getItemCount() {
        if (mRestoList == null) {
            return 0;
        } else {
            return mRestoList.size();
        }
    }

    static class RestaurantHolder extends RecyclerView.ViewHolder {

        private TextView mRestoName;
        private TextView mRestoDistance;
        private TextView mRestoAddress;
        private TextView mRestoNbWorkmates;
        private TextView mRestoOpening;
        private ImageView mRestoNote1;
        private ImageView mRestoNote2;
        private ImageView mRestoNote3;
        private ImageView mRestoImage;

        public RestaurantHolder(@NonNull View itemView) {
            super(itemView);

            mRestoName = itemView.findViewById(R.id.text_restaurant_name);
            mRestoDistance = itemView.findViewById(R.id.text_distance);
            mRestoAddress = itemView.findViewById(R.id.text_restaurant_address);
            mRestoNbWorkmates = itemView.findViewById(R.id.text_nb_workmates);
            mRestoOpening = itemView.findViewById(R.id.text_opening_info);
            mRestoNote1 = itemView.findViewById(R.id.img_star_note_1);
            mRestoNote2 = itemView.findViewById(R.id.img_star_note_2);
            mRestoNote3 = itemView.findViewById(R.id.img_star_note_3);
            mRestoImage = itemView.findViewById(R.id.img_restaurant);
        }
    }
}
