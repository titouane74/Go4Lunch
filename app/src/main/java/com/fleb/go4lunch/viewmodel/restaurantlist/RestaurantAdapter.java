package com.fleb.go4lunch.viewmodel.restaurantlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.utils.GsonHelper;
import com.fleb.go4lunch.utils.RatingCalculation;
import com.fleb.go4lunch.view.activities.RestaurantDetailActivity;

import java.util.List;

/**
 * Created by Florence LE BOURNOT on 26/09/2020
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder> {

    private static final String TAG_LIST_RESTO = "TAG_LIST_RESTO";
    private List<Restaurant> mRestoList;

    public void setRestoList(List<Restaurant> pRestoList) { mRestoList = pRestoList; }

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
        String lDistance = String.valueOf(mRestoList.get(position).getRestoDistance());

        pRestoHolder.mRestoName.setText(mRestoList.get(position).getRestoName());
        pRestoHolder.mRestoDistance.setText(lDistance);
        pRestoHolder.mRestoAddress.setText(mRestoList.get(position).getRestoAddress());
        pRestoHolder.mRestoNbWorkmates.setText("(" + mRestoList.get(position).getRestoNbWorkmates() + ")");

        if (mRestoList.get(position).getRestoOpening() != null) {
            lOpening = mRestoList.get(position).getRestoOpening();
            pRestoHolder.mRestoOpening.setText(lOpening);
            if (lOpening.contains(lContext.getResources().getString(R.string.text_resto_list_clos))) {
                pRestoHolder.mRestoOpening.setTextColor(lContext.getResources().getColor(R.color.colorTextRed));
                pRestoHolder.mRestoOpening.setTypeface(null, Typeface.BOLD);
            } else {
                pRestoHolder.mRestoOpening.setTypeface(null, Typeface.ITALIC);
            }
        }

        int lnbStarToDisplay = RatingCalculation.numberStarToDisplay(lContext,
                mRestoList.get(position).getRestoRating());
        switch (lnbStarToDisplay) {
            case 1:
                Log.d(TAG_LIST_RESTO, "onBindViewHolder: 2 stars to hide : " );
                pRestoHolder.mRestoNote2.setVisibility(View.INVISIBLE);
                pRestoHolder.mRestoNote3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                Log.d(TAG_LIST_RESTO, "onBindViewHolder: 1 star to hide : " );
                pRestoHolder.mRestoNote3.setVisibility(View.INVISIBLE);
                break;
            case 3:
                Log.d(TAG_LIST_RESTO, "onBindViewHolder: no star to hide : " );
                break;
            default:
                Log.d(TAG_LIST_RESTO, "onBindViewHolder: 3 stars to hide : " );
                pRestoHolder.mRestoNote1.setVisibility(View.INVISIBLE);
                pRestoHolder.mRestoNote2.setVisibility(View.INVISIBLE);
                pRestoHolder.mRestoNote3.setVisibility(View.INVISIBLE);
                break;
        }

        if (mRestoList.get(position).getRestoPhotoUrl() != null ) {
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
                lIntentRestoDetail.putExtra("placeid",mRestoList.get(position).getRestoPlaceId());
                lIntentRestoDetail.putExtra("restaurant" ,lJsonRestaurant);
                lContext.startActivity(lIntentRestoDetail);
            }
        });


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
