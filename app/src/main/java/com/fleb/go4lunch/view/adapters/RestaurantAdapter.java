package com.fleb.go4lunch.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
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
import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.fleb.go4lunch.view.activities.RestaurantDetailActivity;

import java.util.List;

import static com.fleb.go4lunch.view.activities.RestaurantDetailActivity.RESTO_PLACE_ID;

/**
 * Created by Florence LE BOURNOT on 26/09/2020
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder> {

    private static final String TAG = "TAG_STATUS";
    private List<Restaurant> mRestoList;
    private DayOpeningHours mDayOpenHourStatus;
    private Context mContext;

    /**
     * Declarations for the opening hours display
     */
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
        int lNbWorkmate = 0;
        mContext = pRestoHolder.itemView.getContext();

        pRestoHolder.mRestoName.setText(mRestoList.get(position).getRestoName());
        pRestoHolder.mRestoDistance.setText(mRestoList.get(position).getRestoDistanceText());
        pRestoHolder.mRestoAddress.setText(Go4LunchHelper.formatAddress(mRestoList.get(position).getRestoAddress()));

        if (mRestoList.get(position).getRestoWkList() != null) {
            lNbWorkmate = mRestoList.get(position).getRestoWkList().size();
        }

        pRestoHolder.mRestoNbWorkmates.setText("(" + lNbWorkmate + ")");

        displayOpeningHour(pRestoHolder, position);

        displayRating(pRestoHolder, position);

        displayRestoImg(pRestoHolder, position);

        displayLogoWorkmate(pRestoHolder);

        pRestoHolder.itemView.setOnClickListener(v -> {
            Intent lIntentRestoDetail = new Intent(mContext, RestaurantDetailActivity.class);
            lIntentRestoDetail.putExtra(RESTO_PLACE_ID, mRestoList.get(position).getRestoPlaceId());
            mContext.startActivity(lIntentRestoDetail);
        });
    }

    /**
     * Display the workmate logo
     * @param pRestoHolder : object : restaurant holder
     */
    private void displayLogoWorkmate(RestaurantHolder pRestoHolder) {
        int lImgWorkmate = R.drawable.ic_workmate;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            lImgWorkmate = R.drawable.ic_workmate_small_png;
        }

        Glide.with(pRestoHolder.mWorkmateImg.getContext())
                .load(lImgWorkmate)
                .apply(RequestOptions.centerCropTransform())
                .into(pRestoHolder.mWorkmateImg);
    }

    /**
     * Display the opening hours
     * @param pRestoHolder : object : restaurant holder
     * @param pPosition : int : position in the adapter
     */
    private void displayOpeningHour(RestaurantHolder pRestoHolder, int pPosition) {
        if (mRestoList.get(pPosition).getRestoOpeningHours() != null) {

            DayOpeningHours lOpeningStatus = getRestaurantOpeningHoursStatus(mContext, mRestoList.get(pPosition));
            if (lOpeningStatus.isDayIsOpen()) {
                pRestoHolder.mRestoOpening.setText(lOpeningStatus.getDayDescription());
                pRestoHolder.mRestoOpening.setTextColor(mContext.getResources().getColor(R.color.colorTextBlack));
                pRestoHolder.mRestoOpening.setTypeface(null, Typeface.ITALIC);
            } else {
                pRestoHolder.mRestoOpening.setText(lOpeningStatus.getDayDescription());
                pRestoHolder.mRestoOpening.setTextColor(mContext.getResources().getColor(R.color.colorTextRed));
                pRestoHolder.mRestoOpening.setTypeface(null, Typeface.BOLD);
            }
        } else {
            pRestoHolder.mRestoOpening.setText("");
        }
    }

    /**
     * Display the restaurant image
     * @param pRestoHolder : object : restaurant holder
     * @param pPosition : int : position in the adapter
     */
    private void displayRestoImg(RestaurantHolder pRestoHolder, int pPosition) {
        if (mRestoList.get(pPosition).getRestoPhotoUrl() != null) {
            Glide.with(pRestoHolder.mRestoImage.getContext())
                    .load(mRestoList.get(pPosition).getRestoPhotoUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .into(pRestoHolder.mRestoImage);
        } else {
            Glide.with(pRestoHolder.mRestoImage.getContext())
                    .load(R.drawable.ic_resto_default_detail)
                    .apply(RequestOptions.centerCropTransform())
                    .into(pRestoHolder.mRestoImage);
        }
    }

    /**
     * Display the rating
     * @param pRestoHolder : object : restaurant holder
     * @param pPosition : int : position in the adapter
     */
    private void displayRating(RestaurantHolder pRestoHolder, int pPosition) {
        int lImgStar = R.drawable.ic_star_yellow_note;

        Glide.with(pRestoHolder.mRestoNote1.getContext())
                .load(lImgStar)
                .apply(RequestOptions.centerCropTransform())
                .into(pRestoHolder.mRestoNote1);
        Glide.with(pRestoHolder.mRestoNote2.getContext())
                .load(lImgStar)
                .apply(RequestOptions.centerCropTransform())
                .into(pRestoHolder.mRestoNote2);
        Glide.with(pRestoHolder.mRestoNote3.getContext())
                .load(lImgStar)
                .apply(RequestOptions.centerCropTransform())
                .into(pRestoHolder.mRestoNote3);

        int lNbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay(mRestoList.get(pPosition).getRestoRating());
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
    }

    /**
     * Get the restaurant opening hours status : open or not with the description
     * @param pContext : object : context
     * @param pRestaurant : object : restaurant
     * @return : object : DayOpeningHours : information on the opening hour at the moment
     */
    private DayOpeningHours getRestaurantOpeningHoursStatus(Context pContext, Restaurant pRestaurant) {
        String lStringNextOpenDay = null;
        String lStringTime = null;
        int lCurrentDay;

        RestaurantDetailPojo.OpeningHours lRestoHoursList = pRestaurant.getRestoOpeningHours();
        mDayOpenHourStatus = new DayOpeningHours(9, 0, 9, 9, false, null, 9, 9);

        //Get the current number day
        // -1 is for corresponding to the period which start on a sunday with a 0 value
        lCurrentDay = Go4LunchHelper.getCurrentDayInt() - 1;

        //Get the list of the opening hours of the current day and the next day
        if (lRestoHoursList.getPeriods().size() > 0) {
            mDayOpenHourStatus.setDayCurrentOpenDay(lCurrentDay);

            if (lRestoHoursList.getPeriods().get(0).getClose() == null) {
                mDayOpenHourStatus.setDayCase(CASE_6_OPEN_24_7);
                mDayOpenHourStatus.setDayIsOpen(true);
            } else {
                getServiceDay(lRestoHoursList, lCurrentDay);

                //Next opening on an other day
                if (mDayOpenHourStatus.getDayCase() < 2) {  // Search next opening day
                    searchNextOpenDay(lRestoHoursList, lCurrentDay);
                }
                //Closed. Open on an other day than tomorrow
                lStringNextOpenDay = Go4LunchHelper.getDayString(mDayOpenHourStatus.getDayNextOpenDay());
                //Closed. Open tomorrow or an other day - get the next opening hour
                if ((mDayOpenHourStatus.getDayCase() < 3) || (mDayOpenHourStatus.getDayCase() == CASE_5_CLOSED_TODAY_OPEN_AT_ON)) {
                    lStringTime = Go4LunchHelper.getCurrentTimeFormatted(mDayOpenHourStatus.getDayNextOpenHour());
                }
                //Open. Closed at
                if ((mDayOpenHourStatus.getDayCase() == CASE_3_OPEN_UNTIL) || (mDayOpenHourStatus.getDayCase() == CASE_7_OPEN_UNTIL_CLOSING_AM)) {
                    lStringTime = Go4LunchHelper.getCurrentTimeFormatted(mDayOpenHourStatus.getDayCloseHour());
                }
            }
            mDayOpenHourStatus.setDayDescription(getTextDescription(pContext, mDayOpenHourStatus.getDayCase(), lStringTime, lStringNextOpenDay));
        }
        return mDayOpenHourStatus;
    }

    /**
     * Search the next opening day
     * @param pRestoHoursList : list object : list of all the opening hours of the restaurant
     * @param pCurrentDay : int : current day of week
     */
    private void searchNextOpenDay(RestaurantDetailPojo.OpeningHours pRestoHoursList, int pCurrentDay) {
        int lAddDay = 0;
        int lSearchDay = pCurrentDay;

        while (mDayOpenHourStatus.getDayNextOpenDay() == 9) {
            if (lSearchDay == 6) {
                lSearchDay = 0;
            } else {
                lSearchDay++;
            }
            lAddDay++;
            mDayOpenHourStatus = getNextOpenDay(pRestoHoursList, mDayOpenHourStatus, lSearchDay);
            if (lAddDay > 1) {
                if (mDayOpenHourStatus.getDayCase() == CASE_2_CLOSED_OPEN_AT) {
                    mDayOpenHourStatus.setDayCase(CASE_1_CLOSED_OPEN_AT_ON);  //closed at end of the day and next opening isn't tomorrow
                } else if (mDayOpenHourStatus.getDayCase() != CASE_7_OPEN_UNTIL_CLOSING_AM) {
                    //closed today next opening day isn't tomorrow
                    mDayOpenHourStatus.setDayCase(CASE_5_CLOSED_TODAY_OPEN_AT_ON);
                }
            } else if ((lAddDay == 1) && (mDayOpenHourStatus.getDayCase() == CASE_1_CLOSED_OPEN_AT_ON)) {
                mDayOpenHourStatus.setDayCase(CASE_2_CLOSED_OPEN_AT);  //next opening is tomorrow
            } else if (((mDayOpenHourStatus.getDayNextOpenDay() - lSearchDay) != 0) && (mDayOpenHourStatus.getDayCase() != CASE_7_OPEN_UNTIL_CLOSING_AM)) {
                mDayOpenHourStatus.setDayCase(CASE_5_CLOSED_TODAY_OPEN_AT_ON);
            }
        }
    }

    /**
     * Get service information of the opening day
     * @param pRestoHoursList : list object : list of all the opening hours of the restaurant
     * @param pDay : int : day of week
     */
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
                mDayOpenHourStatus.setDayCurrentOpenDay(pDay);
                if (lNumService == 2) {
                    lService1CloseTime = Integer.parseInt(pRestoHoursList.getPeriods().get(lIndex - 1).getClose().getTime());
                }
                if (pDay == 0) {
                    lPreviousDay = 6;
                } else {
                    lPreviousDay = pDay - 1;
                }
                if (lOpenDay != lCloseDay) {
                    mDayOpenHourStatus.setDayLastCloseHour(searchPreviousDay(pRestoHoursList, lPreviousDay));
                }

                if (mDayOpenHourStatus.getDayCase() < 2) {
                    defineCase(lOpenTime, lCloseTime, lService1CloseTime);
                }
            }
        }
    }

    /**
     * Define the case of the restaurant opening hours to display
     * @param pOpenTime : int : opening time
     * @param pCloseTime : int : closing time
     * @param pService1CloseTime : int : closing time of the first service of the day
     */
    private void defineCase(int pOpenTime, int pCloseTime, int pService1CloseTime) {
        boolean lIsCloseSoon;
        int lCurrentTime = Go4LunchHelper.getCurrentTime();

        if ((mDayOpenHourStatus.getDayCase() == CASE_1_CLOSED_OPEN_AT_ON) && ((pService1CloseTime < lCurrentTime) && (lCurrentTime < pOpenTime))) {
            mDayOpenHourStatus.setDayCase(CASE_2_CLOSED_OPEN_AT); // Closed. Open at
            mDayOpenHourStatus.setDayNextOpenHour(pOpenTime);
        } else if ((mDayOpenHourStatus.getDayCase() == CASE_1_CLOSED_OPEN_AT_ON) && (lCurrentTime == pCloseTime)) {
            mDayOpenHourStatus.setDayCase(CASE_2_CLOSED_OPEN_AT); // Closed. Open at
            mDayOpenHourStatus.setDayNextOpenHour(pOpenTime);
        } else if (lCurrentTime < pOpenTime) {
            if ((mDayOpenHourStatus.getDayLastCloseHour() < pOpenTime) && (lCurrentTime < mDayOpenHourStatus.getDayLastCloseHour())) {
                lIsCloseSoon = verifyClosingSoonCase(mDayOpenHourStatus.getDayLastCloseHour(), lCurrentTime);
                if (lIsCloseSoon) {
                    mDayOpenHourStatus.setDayCase(CASE_4_CLOSING_SOON);
                } else {
                    mDayOpenHourStatus.setDayCase(CASE_7_OPEN_UNTIL_CLOSING_AM);
                    mDayOpenHourStatus.setDayCloseHour(mDayOpenHourStatus.getDayLastCloseHour());
                }
            } else if ((mDayOpenHourStatus.getDayLastCloseHour() < pOpenTime) && (lCurrentTime > mDayOpenHourStatus.getDayLastCloseHour())) {
                mDayOpenHourStatus.setDayCase(CASE_2_CLOSED_OPEN_AT);
                mDayOpenHourStatus.setDayNextOpenHour(pOpenTime);
            } else if ((pCloseTime < pOpenTime) && (lCurrentTime >= pCloseTime)) {
                mDayOpenHourStatus.setDayCase(CASE_2_CLOSED_OPEN_AT);
                mDayOpenHourStatus.setDayNextOpenHour(pOpenTime);
            } else if (pCloseTime < pOpenTime) {
                lIsCloseSoon = verifyClosingSoonCase(pCloseTime, lCurrentTime);
                if (lIsCloseSoon) {
                    mDayOpenHourStatus.setDayCase(CASE_4_CLOSING_SOON);
                } else {
                    mDayOpenHourStatus.setDayCase(CASE_3_OPEN_UNTIL);
                    mDayOpenHourStatus.setDayCloseHour(pCloseTime);
                }
            } else {
                mDayOpenHourStatus.setDayCase(CASE_2_CLOSED_OPEN_AT); // Closed. Open at
                mDayOpenHourStatus.setDayNextOpenHour(pOpenTime);
            }
        } else if ((lCurrentTime < pCloseTime) || ((pCloseTime < pOpenTime) && (lCurrentTime > pOpenTime))) {
            // 3 - Open until or 4 - Open. Closing soon
            lIsCloseSoon = verifyClosingSoonCase(pCloseTime, lCurrentTime);
            if (lIsCloseSoon) {
                mDayOpenHourStatus.setDayCase(CASE_4_CLOSING_SOON);
            } else {
                mDayOpenHourStatus.setDayCase(CASE_3_OPEN_UNTIL);
            }
            mDayOpenHourStatus.setDayCloseHour(pCloseTime);
        } else if (pCloseTime <= lCurrentTime) {
            mDayOpenHourStatus.setDayCase(CASE_1_CLOSED_OPEN_AT_ON);  //Closed
        } else {
            Log.i(TAG, "defineCase: nothing changed ");
        }
    }

    /**
     * Get the next open day
     * @param pRestoHoursList : list object : list of all the opening hours of the restaurant
     * @param mStatus : object : dayopeninhours status
     * @param pDay : int : day
     * @return : object : DayOpeningHours : information on the opening hour at the moment
     */
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

    /**
     * Search previous opening day for the restaurant which are open until 1:00
     * @param pRestoHoursList : list object : list of all the opening hours of the restaurant
     * @param pDay : int : day
     * @return : int : previous opening day
     */
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

    /**
     * Search the service information
     * @param pRestoHoursList : list object : list of all the opening hours of the restaurant
     * @param pIndex : int : index
     * @param pOpenDay : int : open day
     * @return : object : DayOpeningHours : information on the opening hour at the moment
     */
    private DayOpeningHours searchServices(RestaurantDetailPojo.OpeningHours pRestoHoursList, int pIndex, int pOpenDay) {

        //Find the next service which is not on the same day
        if (pOpenDay == 6) {  //next service on a sunday => index = 0
            pIndex = 0;
        }
        if (pRestoHoursList.getPeriods().size() <= pIndex) {
            pIndex = 0;
        }
        if (mDayOpenHourStatus.getDayCase() == CASE_7_OPEN_UNTIL_CLOSING_AM) { // next closing hour
            mDayOpenHourStatus.setDayCloseHour(Integer.parseInt(pRestoHoursList.getPeriods().get(pIndex).getClose().getTime()));
        } else {  // next opening hour
            mDayOpenHourStatus.setDayNextOpenHour(Integer.parseInt(pRestoHoursList.getPeriods().get(pIndex).getOpen().getTime()));
        }
        mDayOpenHourStatus.setDayNextOpenDay(pRestoHoursList.getPeriods().get(pIndex).getOpen().getDay());

        return mDayOpenHourStatus;
    }

    /**
     * Verify if the restaurant is going to close soon
     * Update the restaurant status is open or not
     * @param pCloseTime : int : close time
     * @param pCurrentTime : int : current time
     * @return : boolean : true if closing soon
     */
    private boolean verifyClosingSoonCase(int pCloseTime, int pCurrentTime) {

        pCloseTime = Go4LunchHelper.convertTimeInMinutes(pCloseTime);
        int lCloseTimeSoon = pCloseTime - 30;
        pCurrentTime = Go4LunchHelper.convertTimeInMinutes(pCurrentTime);

        if ((pCurrentTime >= lCloseTimeSoon) && (pCurrentTime < pCloseTime)) {
            mDayOpenHourStatus.setDayIsOpen(false);
            return true;
        } else {
            mDayOpenHourStatus.setDayIsOpen(true);
            return false;
        }
    }

    /**
     * Get the text description in function of the case of the restaurant
     * @param pContext : object : Context
     * @param pCase : int : case of the restaurant
     * @param pTime : string : time for closing or opening
     * @param pNextOpenDay : string : next opening day
     * @return string : the description
     */
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

        private final TextView mRestoName;
        private final TextView mRestoDistance;
        private final TextView mRestoAddress;
        private final TextView mRestoNbWorkmates;
        private final TextView mRestoOpening;
        private final ImageView mRestoNote1;
        private final ImageView mRestoNote2;
        private final ImageView mRestoNote3;
        private final ImageView mRestoImage;
        private final ImageView mWorkmateImg;

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
            mWorkmateImg = itemView.findViewById(R.id.img_workmate);
        }
    }
}
