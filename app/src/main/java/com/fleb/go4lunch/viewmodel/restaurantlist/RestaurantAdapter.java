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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florence LE BOURNOT on 26/09/2020
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder> {

    private static final String TAG_LIST_RESTO = "TAG_LIST_RESTO";
    private static final String TAG_WEEKDAY = "TAG_WEEKDAY";
    private static final String TAG_STATUS = "TAG_STATUS";
    private List<Restaurant> mRestoList;
    private double mLatitude = 48.8236549;
    private double mLongitude = 2.4102578;

    private int mOpen = 0;
    private int mClose = 0;
    private int mNumService = 1;
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
        String lOpening;

        //TODO à supprimer après avoir trouver la currentlocation
        Location lFusedLocationProvider = Go4LunchHelper.setCurrentLocation(mLatitude, mLongitude);
        int lDistance = Go4LunchHelper.getRestaurantDistanceToCurrentLocation(lFusedLocationProvider,
                mRestoList.get(position).getRestoLocation());
        //String lDistance = String.valueOf(mRestoList.get(position).getRestoDistance());

        String lNewDistance = Go4LunchHelper.convertDistance(lDistance);

        pRestoHolder.mRestoName.setText(mRestoList.get(position).getRestoName());
        pRestoHolder.mRestoDistance.setText(lNewDistance);
        pRestoHolder.mRestoAddress.setText(mRestoList.get(position).getRestoAddress());
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

        int lnbStarToDisplay = Go4LunchHelper.ratingNumberOfStarToDisplay(lContext,
                mRestoList.get(position).getRestoRating());
        switch (lnbStarToDisplay) {
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

        int lIndexDay, lIndexNextDay = 0;

        RestaurantDetailPojo.OpeningHours lRestoHoursList = pRestaurant.getRestoOpeningHours();
        List<DayOpeningHours> lDayHourList = new ArrayList<>();
        DayOpeningHours lStatus = new DayOpeningHours();

        //Get the current number day
        // -1 is for corresponding to the period which start on a sunday with a 0 value
        lIndexDay = Go4LunchHelper.getCurrentDayInt() - 1;
        lIndexNextDay = lIndexDay + 1;

        //Get the list of the opening hours of the current day and the next day
        if(lRestoHoursList.getPeriods().size()>0) {
            lDayHourList = getOpeningHourOfTheCurrentAndNextDay(lRestoHoursList, lIndexDay, lIndexNextDay);

            lStatus.setDayIsOpen(false);
            lStatus.setDayNumber(lIndexDay);

            //Compare the current time to the opening hours to determine the status of the restaurant : open or closed
            lStatus = getRestaurantStatusDescription(pContext, lDayHourList, lStatus);
        }
        return lStatus;
    }

    private List<DayOpeningHours> getOpeningHourOfTheCurrentAndNextDay(RestaurantDetailPojo.OpeningHours pRestoHoursList,
                                                                       int pIndexDay, int pIndexNextDay) {
        List<DayOpeningHours> lDayHourList = new ArrayList<>();

        int lNumService = 1;
        int lOpeningDay = 9;

        int lMaxPeriod = pRestoHoursList.getPeriods().size();

        //Get the opening hours of the day and the next day
        for (int lIndexList = 0; lIndexList < lMaxPeriod; lIndexList++) {
            if ((pRestoHoursList.getPeriods().get(lIndexList).getOpen().getDay() == pIndexDay)
                    || (pRestoHoursList.getPeriods().get(lIndexList).getOpen().getDay() == pIndexNextDay)
                && lNumService<4) {

                if (pRestoHoursList.getPeriods().get(lIndexList).getOpen().getDay() == pIndexDay) {
                    lOpeningDay = pIndexDay;
                } else if (pRestoHoursList.getPeriods().get(lIndexList).getOpen().getDay() == pIndexNextDay){
                    lOpeningDay = pIndexNextDay;
                }

                DayOpeningHours.DayService lService = new DayOpeningHours.DayService(lNumService,
                        Integer.parseInt(pRestoHoursList.getPeriods().get(lIndexList).getClose().getTime()),
                        Integer.parseInt(pRestoHoursList.getPeriods().get(lIndexList).getOpen().getTime()),
                        lOpeningDay);

                lDayHourList.add(new DayOpeningHours(
                        lOpeningDay,
                        lNumService,
                        lService,
                        Integer.parseInt(pRestoHoursList.getPeriods().get(lIndexList).getOpen().getTime()),
                        Integer.parseInt(pRestoHoursList.getPeriods().get(lIndexList).getClose().getTime()),
                        false,
                        null));
                lNumService++;
            }
        }

        return lDayHourList;
    }

    private DayOpeningHours getRestaurantStatusDescription(Context pContext,List<DayOpeningHours> pDayHourList, DayOpeningHours pStatus) {
        int lTime = 0;
        int lCase = 0;

        DayOpeningHours.DayService lService1 = new DayOpeningHours.DayService();
        DayOpeningHours.DayService lService2 = new DayOpeningHours.DayService();
        DayOpeningHours.DayService lService3 = new DayOpeningHours.DayService();

        //Get the current time
        int lCurrentTime = Go4LunchHelper.getCurrentTime();

        //Identificate the number of services
        int lNbMaxService = pDayHourList.size();
        switch (lNbMaxService) {
            case 0:
                lCase = 0; //Close today
            case 1:
                lService1 = pDayHourList.get(0).getDayService();
                break;
            case 2:
                lService1 = pDayHourList.get(0).getDayService();
                lService2 = pDayHourList.get(1).getDayService();
                break;
            case 3:
                lService1 = pDayHourList.get(0).getDayService();
                lService2 = pDayHourList.get(1).getDayService();
                lService3 = pDayHourList.get(2).getDayService();
                break;
            default:
                lCase = 0;
        }

        if (((lNbMaxService == 1) && (lService1.getCloseTime() < lCurrentTime))
            // 1 && 2030 < 2200
            || ((lNbMaxService == 2) && (lService2.getCloseTime() < lCurrentTime))
            // 2 && 2145 < 2300
        ) {
            lCase = 1; //Closed
        } else if ((lCurrentTime < lService1.getOpenTime())
                // 1 && 0500 < 0700
                // 2 && 0500 < 1200
                // 3 && 0500 < 1200
            ) {
            lCase = 2; //Closed. Open at
            lTime = lService1.getOpenTime();
        } else if (((lNbMaxService == 2) || (lNbMaxService == 3))
                && (lService1.getCloseTime() < lCurrentTime) && (lCurrentTime < lService2.getOpenTime())
                // 2 && 1345 < 1500 && 1500 < 1930
                // 3 && 1430 < 1500 && 1500 < 1900
        ) {
            lCase = 2; //Closed. Open at
            lTime = lService2.getOpenTime();
        } else if ((lNbMaxService == 3) &&
                ((lService2.getCloseTime() < lCurrentTime) && (lService3.getOpenTime() < lService2.getCloseTime()))
                // 3 && 2230 < 2300
                || ((lCurrentTime < lService3.getOpenTime()) && (lService3.getOpenTime() < lService2.getCloseTime()))
                // 3 && 0100 < 1200
        ) {
            lCase = 2; //Closed. Open at
            lTime = lService3.getOpenTime();

        } else if ((lService1.getOpenTime() < lCurrentTime) && (lCurrentTime < lService1.getCloseTime())
        )  {
            //1 && 0700 < 1200 && 1200 < 2030
            //2 && 1200 < 1300 && 1300 < 1345
            //3 && 1200 < 1300 && 1300 < 1430
            lCase = 3; //Open until
            lTime = lService1.getCloseTime();
            pStatus.setDayIsOpen(true);
        } else if (((lNbMaxService == 2) || (lNbMaxService == 3))
                && (lService2.getOpenTime() < lCurrentTime) && (lCurrentTime < lService2.getCloseTime())) {
            //2 && 1930 < 2000 && 2000 < 2145
            //3 && 1900 < 2000 && 2000 < 2230
            lCase = 3; //Open. Closed at
            lTime = lService2.getCloseTime();
            pStatus.setDayIsOpen(true);
        } else {
            lCase = 1; //Closed
        }

        //Determine the text description in function of the case
        String lStringTime = Go4LunchHelper.getCurrentTimeFormatted(lTime);
        pStatus.setDayDescription(getTextDescription(pContext, lCase, lStringTime));

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
