package com.fleb.go4lunch.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.view.activities.RestaurantDetailActivity;


import java.util.List;

import static com.fleb.go4lunch.view.activities.RestaurantDetailActivity.RESTO_PLACE_ID;


/**
 * Created by Florence LE BOURNOT on 22/09/2020
 */
public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateAdapter.WorkmateHolder> {

    private List<Workmate> mWorkmateList;
    private Workmate.WorkmateRestoChoice mRestaurantChosen;

    public void setWorkmateList(List<Workmate> pWorkmateList) {
        mWorkmateList = pWorkmateList;
    }


    @NonNull
    @Override
    public WorkmateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_workmate_list_item,
                parent, false);
        return new WorkmateHolder(lView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateHolder pWorkmateHolder, int position) {

        String lTxtWorkmate;

        Context lContext = pWorkmateHolder.itemView.getContext();

        if (mWorkmateList.get(position).getWorkmateRestoChosen() != null) {
            mRestaurantChosen = mWorkmateList.get(position).getWorkmateRestoChosen();
            lTxtWorkmate = mWorkmateList.get(position).getWorkmateName()
            + " " + lContext.getString(R.string.text_workmate_eating) + " (" + mRestaurantChosen.getRestoName() + ")";
            pWorkmateHolder.mTxtViewName.setTextColor(lContext.getResources().getColor(R.color.colorTextBlack));
            pWorkmateHolder.mTxtViewName.setTypeface(null, Typeface.NORMAL);
        } else {
            lTxtWorkmate = mWorkmateList.get(position).getWorkmateName()
                    + " " + lContext.getString(R.string.text_workmate_not_decided);
            pWorkmateHolder.mTxtViewName.setTextColor(lContext.getResources().getColor(R.color.colorTextUnavailable));
            pWorkmateHolder.mTxtViewName.setTypeface(null, Typeface.ITALIC);
        }
        pWorkmateHolder.mTxtViewName.setText(lTxtWorkmate);

        Glide.with(pWorkmateHolder.mImgViewWorkmate.getContext())
                .load(mWorkmateList.get(position).getWorkmatePhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(pWorkmateHolder.mImgViewWorkmate);

        pWorkmateHolder.itemView.setOnClickListener(v -> {
            if (mWorkmateList.get(position).getWorkmateRestoChosen() != null) {
                Intent lIntentRestoDetail = new Intent(lContext, RestaurantDetailActivity.class);
                mRestaurantChosen =  mWorkmateList.get(position).getWorkmateRestoChosen();
                lIntentRestoDetail.putExtra(RESTO_PLACE_ID, mRestaurantChosen.getRestoId());
                lContext.startActivity(lIntentRestoDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (mWorkmateList == null) {
            return 0;
        } else {
            return mWorkmateList.size();
        }
    }

    static class WorkmateHolder extends RecyclerView.ViewHolder {

        private final TextView mTxtViewName;
        private final ImageView mImgViewWorkmate;

        public WorkmateHolder(@NonNull View itemView) {
            super(itemView);

            mTxtViewName = itemView.findViewById(R.id.item_list_workmate_name);
            mImgViewWorkmate = itemView.findViewById(R.id.item_list_workmate_img);
        }
    }
}
