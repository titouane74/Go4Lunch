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

    private static final String TAG_LIST_RESTO = "TAG_LIST_RESTO";
    private static final String TAG = "TAG_STATUS";
    private List<Restaurant> mRestoList;
    private double mLatitude;
    private double mLongitude;

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
        String lStringNextOpenDay = null;
        String lStringTime = null;
        int lCurrentDay = 0;

        RestaurantDetailPojo.OpeningHours lRestoHoursList = pRestaurant.getRestoOpeningHours();
        DayOpeningHours lStatus = new DayOpeningHours();

        //Get the current number day
        // -1 is for corresponding to the period which start on a sunday with a 0 value
        lCurrentDay = Go4LunchHelper.getCurrentDayInt() - 1;

        //Get the list of the opening hours of the current day and the next day
        if (lRestoHoursList.getPeriods().size() > 0) {

            lStatus.setDayIsOpen(false);
            lStatus.setDayCurrentOpenDay(lCurrentDay);
            lStatus = getServiceDay(lStatus, lRestoHoursList, lCurrentDay);

            //Next opening on an other day
            if (lStatus.getDayCase() < 3) {
                lStatus = searchNextOpenDay(lRestoHoursList, lStatus, lCurrentDay + 1);
            }
            //Closed. Open on an other day than tomorrow
            lStringNextOpenDay = Go4LunchHelper.getDayString(lStatus.getDayNextOpenDay());
            //Closed. Open tomorrow or an other day - get the next opening hour
            if ((lStatus.getDayCase() < 3) || (lStatus.getDayCase() == 5)) {
                lStringTime = Go4LunchHelper.getCurrentTimeFormatted(lStatus.getDayNextOpenHour());
            }
            //Open. Closed at
            if (lStatus.getDayCase() == 3) {
                lStringTime = Go4LunchHelper.getCurrentTimeFormatted(lStatus.getDayCloseHour());
            }

            lStatus.setDayDescription(getTextDescription(pContext, lStatus.getDayCase(), lStringTime, lStringNextOpenDay));
            Log.d(TAG, "getTextDescription : " + getTextDescription(pContext, lStatus.getDayCase(), lStringTime, lStringNextOpenDay));
        }
        return lStatus;
    }

    private DayOpeningHours getServiceDay(DayOpeningHours pStatus,
                                          RestaurantDetailPojo.OpeningHours pRestoHoursList,
                                          int pDay) {
        int lNumService = 0;
        int lService1CloseTime = 0;
        for (int lIndex = 0; lIndex < pRestoHoursList.getPeriods().size(); lIndex++) {
            int lOpenDay = pRestoHoursList.getPeriods().get(lIndex).getOpen().getDay();
            if (lOpenDay == pDay) {
                lNumService++;
                int lOpenTime = Integer.parseInt(pRestoHoursList.getPeriods().get(lIndex).getOpen().getTime());
                int lCloseTime = Integer.parseInt(pRestoHoursList.getPeriods().get(lIndex).getClose().getTime());
                pStatus.setDayCurrentOpenDay(pDay);
                if (lNumService == 2) {
                    lService1CloseTime = Integer.parseInt(pRestoHoursList.getPeriods().get(lIndex - 1).getClose().getTime());
                }
                pStatus = defineCase(pStatus, lOpenTime, lCloseTime, lService1CloseTime);
            }
        }
        return pStatus;
    }

    private DayOpeningHours defineCase(DayOpeningHours pStatus, int pOpenTime, int pCloseTime, int pService1CloseTime) {
        //Get the current time
        int lCurrentTime = Go4LunchHelper.getCurrentTime();

        //First service has been checked, verify that the current hour is betwwen the 2 services
        if ((pStatus.getDayCase() == 1) && (pService1CloseTime < lCurrentTime) && (lCurrentTime < pOpenTime)) {
            Log.d(TAG, "defineCase: 2eme Service : case 2 : closed. open at");
            Log.d(TAG, "defineCase: 2eme Service : case 2 : closed : " + pCloseTime + " currenttime " + lCurrentTime);
            pStatus.setDayCase(2); // Closed. Open at
            pStatus.setDayNextOpenHour(pOpenTime);
        } else if (lCurrentTime < pOpenTime) {
            Log.d(TAG, "defineCase: case 2 : closed. open at");
            Log.d(TAG, "defineCase: case 2 : closed : " + pCloseTime + " currenttime " + lCurrentTime);
            pStatus.setDayCase(2); // Closed. Open at
            pStatus.setDayNextOpenHour(pOpenTime);
        } else if ((pOpenTime < lCurrentTime) && (lCurrentTime < pCloseTime)) {
            // 3 - Open until or 4 - Open. Closing soon
            pStatus.setDayCase(verifyClosingSoonCase(pCloseTime, lCurrentTime));
            if (pStatus.getDayCase() == 4) {
                pStatus.setDayIsOpen(false);
                Log.d(TAG, "defineCase: case 4 : open : " + pOpenTime + " currenttime " + lCurrentTime);
                Log.d(TAG, "defineCase: case 4 : closed : " + pCloseTime + " currenttime " + lCurrentTime);
            } else {
                pStatus.setDayIsOpen(true);
                Log.d(TAG, "defineCase: case 3 : open : " + pOpenTime + " currenttime " + lCurrentTime);
                Log.d(TAG, "defineCase: case 3 : closed : " + pCloseTime + " currenttime " + lCurrentTime);
            }
            pStatus.setDayCloseHour(pCloseTime);
        } else if (pCloseTime < lCurrentTime) {
            Log.d(TAG, "defineCase: case 1 : closed. open at on");
            Log.d(TAG, "defineCase: case 1 : closed " + pCloseTime + " currentime " + lCurrentTime);
            pStatus.setDayCase(1);  //Closed
        } else {
            Log.d(TAG, "defineCase: nothing changed ");
        }
        return pStatus;
    }


    private DayOpeningHours searchNextOpenDay(RestaurantDetailPojo.OpeningHours pRestoHoursList,
                                              DayOpeningHours pStatus,
                                              int pDay) {
        for (int lIndex = 0; lIndex < pRestoHoursList.getPeriods().size(); lIndex++) {
            int lNextOpenDay = pRestoHoursList.getPeriods().get(lIndex).getOpen().getDay();
            if (lNextOpenDay >= pDay) {
                pStatus.setDayNextOpenDay(pRestoHoursList.getPeriods().get(lIndex).getOpen().getDay());
                if (lNextOpenDay == pDay + 1) {   //next opening day isn't tomorrow
                    pStatus.setDayCase(5);
                }
                return searchNextServices(pRestoHoursList, lIndex, pRestoHoursList.getPeriods().get(lIndex).getOpen().getDay(),
                        pStatus);
            }
        }
        return pStatus;
    }

    private DayOpeningHours searchNextServices(RestaurantDetailPojo.OpeningHours pRestoHoursList, int pIndex, int pOpenDay,
                                               DayOpeningHours pStatus) {

        //Find the next service which is not on the same day
        if (pOpenDay == 6) {  //next service on a sunday => index = 0
            Log.d(TAG, "prochain service est un dimanche: index = 0");
            pIndex = 0;
        } else {      //next service is not on a sunday => no change to the index
            Log.d(TAG, "prochain service n'est pas un dimanche");
        }
        if (pRestoHoursList.getPeriods().size() <= pIndex) {
            Log.d(TAG, "searchNextServices: last day in table isn't sunday  pass index to 0");
            pIndex = 0;
        }

        pStatus.setDayNextOpenHour(Integer.parseInt(pRestoHoursList.getPeriods().get(pIndex).getOpen().getTime()));
        pStatus.setDayNextOpenDay(pRestoHoursList.getPeriods().get(pIndex).getOpen().getDay());

        return pStatus;
    }

    private int verifyClosingSoonCase(int pCloseTime, int pCurrentTime) {
        int lCloseTimeSoon = pCloseTime - 150;
        if ((pCurrentTime >= lCloseTimeSoon) && (pCurrentTime < pCloseTime)) {
            Log.d(TAG, "verifyClosingSoonCase: case 4 : closing soon");
            return 4;
        } else {
            Log.d(TAG, "verifyClosingSoonCase: case 3 : closed at");
            return 3;
        }
    }

    private String getTextDescription(Context pContext, int pCase, @Nullable String pTime, @Nullable String pNextOpenDay) {
        switch (pCase) {
            case 0:   // closed today but open tomorrow
                return pContext.getResources().getString(R.string.text_resto_closed_today) + ". " + pTime;
            case 1:  // closed (end of day) open on an other day than tomorrow
                return pContext.getResources().getString(R.string.text_resto_closed_open_at) + " " + pTime
                        + " " + pContext.getResources().getString(R.string.text_resto_closed_open_at_on) + " " + pNextOpenDay;
            case 2:  // closed (end of day or not end of day) and open tomorrow
                return pContext.getResources().getString(R.string.text_resto_closed_open_at) + " " + pTime;
            case 3:   // open until
                return pContext.getResources().getString(R.string.text_resto_open_until) + " " + pTime;
            case 4:   //closing soon
                return pContext.getResources().getString(R.string.text_resto_closing_soon);
            case 5:   // closed today but open on an other day than tomorrow
                return pContext.getResources().getString(R.string.text_resto_closed_today) + ". " + pTime
                        + " " + "le" + " " + pNextOpenDay;
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
