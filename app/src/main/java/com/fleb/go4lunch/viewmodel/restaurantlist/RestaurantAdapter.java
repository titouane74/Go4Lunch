package com.fleb.go4lunch.viewmodel.restaurantlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.List;
import java.util.Objects;

import static com.fleb.go4lunch.repository.RestaurantRepository.PREF_KEY_LATITUDE;
import static com.fleb.go4lunch.repository.RestaurantRepository.PREF_KEY_LONGITUDE;
import static com.fleb.go4lunch.utils.PreferencesHelper.mPreferences;

/**
 * Created by Florence LE BOURNOT on 26/09/2020
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder> {

    private static final String TAG = "TAG_STATUS";
    private List<Restaurant> mRestoList;
    private DayOpeningHours mStatus;


    public static final int CASE_0_CLOSED_TODAY_OPEN_AT = 0;
    public static final int CASE_1_CLOSED_OPEN_AT_ON = 1;
    public static final int CASE_2_CLOSED_OPEN_AT = 2;
    public static final int CASE_3_OPEN_UNTIL = 3;
    public static final int CASE_4_CLOSING_SOON = 4;
    public static final int CASE_5_CLOSED_TODAY_OPEN_AT_ON = 5;
    public static final int CASE_6_OPEN_24_7 = 6;
    public static final int CASE_7_OPEN_UNTIL_CLOSING_AM = 7;

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

        double lLatitude = Double.parseDouble(Objects.requireNonNull(mPreferences.getString(PREF_KEY_LATITUDE, null)));
        double lLongitude = Double.parseDouble(Objects.requireNonNull(mPreferences.getString(PREF_KEY_LONGITUDE, null)));

        Location lFusedLocationProvider = Go4LunchHelper.setCurrentLocation(lLatitude, lLongitude);
        int lDistance = Go4LunchHelper.getRestaurantDistanceToCurrentLocation(lFusedLocationProvider,
                mRestoList.get(position).getRestoLocation());

        String lNewDistance = Go4LunchHelper.convertDistance(lDistance);

        pRestoHolder.mRestoName.setText(mRestoList.get(position).getRestoName());
        pRestoHolder.mRestoDistance.setText(lNewDistance);
        pRestoHolder.mRestoAddress.setText(Go4LunchHelper.formatAddress(mRestoList.get(position).getRestoAddress()));
        pRestoHolder.mRestoNbWorkmates.setText("(" + mRestoList.get(position).getRestoNbWorkmates() + ")");

        if (mRestoList.get(position).getRestoOpeningHours() != null) {

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
                pRestoHolder.mRestoNote2.setVisibility(View.INVISIBLE);
                pRestoHolder.mRestoNote3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                pRestoHolder.mRestoNote3.setVisibility(View.INVISIBLE);
                break;
            case 3:
                break;
            default:
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

        pRestoHolder.itemView.setOnClickListener(v -> {
            Intent lIntentRestoDetail = new Intent(lContext, RestaurantDetailActivity.class);
            String lJsonRestaurant = GsonHelper.getGsonString(mRestoList.get(position));
            lIntentRestoDetail.putExtra("placeid", mRestoList.get(position).getRestoPlaceId());
            lIntentRestoDetail.putExtra("restaurant", lJsonRestaurant);
            lContext.startActivity(lIntentRestoDetail);
        });
    }

    private DayOpeningHours getRestaurantOpeningHoursStatus(Context pContext, Restaurant pRestaurant) {
        String lStringNextOpenDay = null;
        String lStringTime = null;
        int lCurrentDay;

        RestaurantDetailPojo.OpeningHours lRestoHoursList = pRestaurant.getRestoOpeningHours();
        mStatus = new DayOpeningHours(9, 0, 9, 9, false, null, 9, 9);

        //Get the current number day
        // -1 is for corresponding to the period which start on a sunday with a 0 value
        lCurrentDay = Go4LunchHelper.getCurrentDayInt() - 1;

        //Get the list of the opening hours of the current day and the next day
        if (lRestoHoursList.getPeriods().size() > 0) {
            mStatus.setDayCurrentOpenDay(lCurrentDay);

            if (lRestoHoursList.getPeriods().get(0).getClose() == null) {
                mStatus.setDayCase(CASE_6_OPEN_24_7);
                mStatus.setDayIsOpen(true);
            } else {
                getServiceDay(lRestoHoursList, lCurrentDay);

                //Next opening on an other day
                if (mStatus.getDayCase() < 2) {  // Search next opening day
                    searchNextOpenDay(lRestoHoursList, lCurrentDay);
                }
                //Closed. Open on an other day than tomorrow
                lStringNextOpenDay = Go4LunchHelper.getDayString(mStatus.getDayNextOpenDay());
                //Closed. Open tomorrow or an other day - get the next opening hour
                if ((mStatus.getDayCase() < 3) || (mStatus.getDayCase() == CASE_5_CLOSED_TODAY_OPEN_AT_ON)) {
                    lStringTime = Go4LunchHelper.getCurrentTimeFormatted(mStatus.getDayNextOpenHour());
                }
                //Open. Closed at
                if ((mStatus.getDayCase() == CASE_3_OPEN_UNTIL) || (mStatus.getDayCase() == CASE_7_OPEN_UNTIL_CLOSING_AM)) {
                    lStringTime = Go4LunchHelper.getCurrentTimeFormatted(mStatus.getDayCloseHour());
                }
            }
            mStatus.setDayDescription(getTextDescription(pContext, mStatus.getDayCase(), lStringTime, lStringNextOpenDay));
        }
        return mStatus;
    }

    private void searchNextOpenDay(RestaurantDetailPojo.OpeningHours pRestoHoursList, int pCurrentDay) {
        int lAddDay = 0;
        int lSearchDay = pCurrentDay;

        while (mStatus.getDayNextOpenDay() == 9) {
            if (lSearchDay == 6) {
                lSearchDay = 0;
            } else {
                lSearchDay++;
            }
            lAddDay++;
            mStatus = getNextOpenDay(pRestoHoursList, mStatus, lSearchDay);
            if (lAddDay > 1) {
                if (mStatus.getDayCase() == CASE_2_CLOSED_OPEN_AT) {
                    mStatus.setDayCase(CASE_1_CLOSED_OPEN_AT_ON);  //closed at end of the day and next opening isn't tomorrow
                } else if (mStatus.getDayCase() != CASE_7_OPEN_UNTIL_CLOSING_AM) {
                    //closed today next opening day isn't tomorrow
                    mStatus.setDayCase(CASE_5_CLOSED_TODAY_OPEN_AT_ON);
                }
            } else if ((lAddDay == 1) && (mStatus.getDayCase() == CASE_1_CLOSED_OPEN_AT_ON)) {
                mStatus.setDayCase(CASE_2_CLOSED_OPEN_AT);  //next opening is tomorrow
            } else if (((mStatus.getDayNextOpenDay() - lSearchDay) != 0) && (mStatus.getDayCase() != CASE_7_OPEN_UNTIL_CLOSING_AM)) {
                mStatus.setDayCase(CASE_5_CLOSED_TODAY_OPEN_AT_ON);
            }
        }
    }

    private void getServiceDay(RestaurantDetailPojo.OpeningHours pRestoHoursList, int pDay) {
        int lNumService = 0;
        int lService1CloseTime = 0;
        int lPreviousDay;
        for (int lIndex = 0; lIndex < pRestoHoursList.getPeriods().size(); lIndex++) {
            int lOpenDay = pRestoHoursList.getPeriods().get(lIndex).getOpen().getDay();
            int lCloseDay = pRestoHoursList.getPeriods().get(lIndex).getClose().getDay();
            if (lOpenDay == pDay) {
                lNumService++;
                int lOpenTime = Integer.parseInt(pRestoHoursList.getPeriods().get(lIndex).getOpen().getTime());
                int lCloseTime = Integer.parseInt(pRestoHoursList.getPeriods().get(lIndex).getClose().getTime());
                mStatus.setDayCurrentOpenDay(pDay);
                if (lNumService == 2) {
                    lService1CloseTime = Integer.parseInt(pRestoHoursList.getPeriods().get(lIndex - 1).getClose().getTime());
                }
                if (pDay == 0) {
                    lPreviousDay = 6;
                } else {
                    lPreviousDay = pDay - 1;
                }
                if (lOpenDay != lCloseDay) {
                    mStatus.setDayLastCloseHour(searchPreviousDay(pRestoHoursList, lPreviousDay));
                }

                if (mStatus.getDayCase() < 2) {
                    defineCase(lOpenTime, lCloseTime, lService1CloseTime);
                }
            }
        }
    }

    private void defineCase(int pOpenTime, int pCloseTime, int pService1CloseTime) {
        boolean lIsCloseSoon;
        int lCurrentTime = Go4LunchHelper.getCurrentTime();

        if ((mStatus.getDayCase() == CASE_1_CLOSED_OPEN_AT_ON) && ((pService1CloseTime < lCurrentTime) && (lCurrentTime < pOpenTime))) {
            mStatus.setDayCase(CASE_2_CLOSED_OPEN_AT); // Closed. Open at
            mStatus.setDayNextOpenHour(pOpenTime);
        } else if ((mStatus.getDayCase() == CASE_1_CLOSED_OPEN_AT_ON) && (lCurrentTime == pCloseTime)) {
            mStatus.setDayCase(CASE_2_CLOSED_OPEN_AT); // Closed. Open at
            mStatus.setDayNextOpenHour(pOpenTime);
        } else if (lCurrentTime < pOpenTime) {
            if ((mStatus.getDayLastCloseHour() < pOpenTime) && (lCurrentTime < mStatus.getDayLastCloseHour())) {
                lIsCloseSoon = verifyClosingSoonCase(mStatus.getDayLastCloseHour(), lCurrentTime);
                if (lIsCloseSoon) {
                    mStatus.setDayCase(CASE_4_CLOSING_SOON);
                } else {
                    mStatus.setDayCase(CASE_7_OPEN_UNTIL_CLOSING_AM);
                    mStatus.setDayCloseHour(mStatus.getDayLastCloseHour());
                }
            } else if ((mStatus.getDayLastCloseHour() < pOpenTime) && (lCurrentTime > mStatus.getDayLastCloseHour())) {
                mStatus.setDayCase(CASE_2_CLOSED_OPEN_AT);
                mStatus.setDayNextOpenHour(pOpenTime);
            } else if ((pCloseTime < pOpenTime) && (lCurrentTime >= pCloseTime)) {
                mStatus.setDayCase(CASE_2_CLOSED_OPEN_AT);
                mStatus.setDayNextOpenHour(pOpenTime);
            } else if (pCloseTime < pOpenTime) {
                lIsCloseSoon = verifyClosingSoonCase(pCloseTime, lCurrentTime);
                if (lIsCloseSoon) {
                    mStatus.setDayCase(CASE_4_CLOSING_SOON);
                } else {
                    mStatus.setDayCase(CASE_3_OPEN_UNTIL);
                    mStatus.setDayCloseHour(pCloseTime);
                }
            } else {
                mStatus.setDayCase(CASE_2_CLOSED_OPEN_AT); // Closed. Open at
                mStatus.setDayNextOpenHour(pOpenTime);
            }
        } else if (((pOpenTime <= lCurrentTime) && (lCurrentTime < pCloseTime))
                || ((pCloseTime < pOpenTime) && (lCurrentTime > pOpenTime))) {
            // 3 - Open until or 4 - Open. Closing soon
            lIsCloseSoon = verifyClosingSoonCase(pCloseTime, lCurrentTime);
            if (lIsCloseSoon) {
                mStatus.setDayCase(CASE_4_CLOSING_SOON);
            } else {
                mStatus.setDayCase(CASE_3_OPEN_UNTIL);
            }
            mStatus.setDayCloseHour(pCloseTime);
        } else if (pCloseTime <= lCurrentTime) {
            mStatus.setDayCase(CASE_1_CLOSED_OPEN_AT_ON);  //Closed
        } else {
            Log.d(TAG, "defineCase: nothing changed ");
        }
    }

    private DayOpeningHours getNextOpenDay(RestaurantDetailPojo.OpeningHours pRestoHoursList,
                                           DayOpeningHours mStatus,
                                           int pDay) {
        int lMaxPeriod = pRestoHoursList.getPeriods().size();
        for (int lIndex = 0; lIndex < lMaxPeriod; lIndex++) {
            int lNextOpenDay = pRestoHoursList.getPeriods().get(lIndex).getOpen().getDay();
            if (lNextOpenDay >= pDay) {
                mStatus.setDayNextOpenDay(lNextOpenDay);
                return searchServices(pRestoHoursList, lIndex, lNextOpenDay);
            }
        }
        return mStatus;
    }

    private int searchPreviousDay(RestaurantDetailPojo.OpeningHours pRestoHoursList,
                                  int pDay) {
        int lMaxPeriod = pRestoHoursList.getPeriods().size();

        for (int lIndex = lMaxPeriod - 1; lIndex >= 0; lIndex--) {
            int lPreviousDay = pRestoHoursList.getPeriods().get(lIndex).getOpen().getDay();
            if (lPreviousDay == pDay) {
                return (Integer.parseInt(pRestoHoursList.getPeriods().get(lIndex).getClose().getTime()));
            }
        }
        return 9;
    }

    private DayOpeningHours searchServices(RestaurantDetailPojo.OpeningHours pRestoHoursList, int pIndex, int pOpenDay) {

        //Find the next service which is not on the same day
        if (pOpenDay == 6) {  //next service on a sunday => index = 0
            pIndex = 0;
        }
        if (pRestoHoursList.getPeriods().size() <= pIndex) {
            pIndex = 0;
        }
        if (mStatus.getDayCase() == CASE_7_OPEN_UNTIL_CLOSING_AM) { // next closing hour
            mStatus.setDayCloseHour(Integer.parseInt(pRestoHoursList.getPeriods().get(pIndex).getClose().getTime()));
        } else {  // next opening hour
            mStatus.setDayNextOpenHour(Integer.parseInt(pRestoHoursList.getPeriods().get(pIndex).getOpen().getTime()));
        }
        mStatus.setDayNextOpenDay(pRestoHoursList.getPeriods().get(pIndex).getOpen().getDay());

        return mStatus;
    }

    private boolean verifyClosingSoonCase(int pCloseTime, int pCurrentTime) {

        pCloseTime = Go4LunchHelper.convertTimeInMinutes(pCloseTime);
        int lCloseTimeSoon = pCloseTime - 30;
        pCurrentTime = Go4LunchHelper.convertTimeInMinutes(pCurrentTime);

        if ((pCurrentTime >= lCloseTimeSoon) && (pCurrentTime < pCloseTime)) {
            mStatus.setDayIsOpen(false);
            return true;
        } else {
            mStatus.setDayIsOpen(true);
            return false;
        }
    }

    private String getTextDescription(Context pContext, int pCase, @Nullable String pTime, @Nullable String pNextOpenDay) {
        Resources lResources = pContext.getResources();
        switch (pCase) {
            case CASE_0_CLOSED_TODAY_OPEN_AT:   // closed today but open tomorrow
                return lResources.getString(R.string.text_resto_closed_today) + " " + pTime;
            case CASE_1_CLOSED_OPEN_AT_ON:  // closed (end of day) open on an other day than tomorrow
                return lResources.getString(R.string.text_resto_closed_open_at) + " " + pTime
                        + " " + pContext.getResources().getString(R.string.text_resto_closed_open_at_on) + " " + pNextOpenDay;
            case CASE_2_CLOSED_OPEN_AT:  // closed (end of day or not end of day) and open tomorrow
                return lResources.getString(R.string.text_resto_closed_open_at) + " " + pTime;
            case CASE_3_OPEN_UNTIL:   // open until
            case CASE_7_OPEN_UNTIL_CLOSING_AM:   // open until for the closing on the next day in the morning
                return lResources.getString(R.string.text_resto_open_until) + " " + pTime;
            case CASE_4_CLOSING_SOON:   //closing soon
                return lResources.getString(R.string.text_resto_closing_soon);
            case CASE_5_CLOSED_TODAY_OPEN_AT_ON:   // closed today but open on an other day than tomorrow
                return lResources.getString(R.string.text_resto_closed_today) + " " + pTime
                        + " " + lResources.getString(R.string.text_resto_on) + " " + pNextOpenDay;
            case CASE_6_OPEN_24_7:   // open 24/7
                return lResources.getString(R.string.text_resto_open_247);
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
