package com.fleb.go4lunch.view.adapters;

import android.content.Context;
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

import java.util.List;


/**
 * Created by Florence LE BOURNOT on 22/09/2020
 */
public class RestaurantDetailWorkmateAdapter extends RecyclerView.Adapter<RestaurantDetailWorkmateAdapter.WorkmateHolder> {

    private List<Restaurant.WorkmatesList> mWorkmateList;

    public void setWorkmateList(List<Restaurant.WorkmatesList> pWorkmateList) {
        mWorkmateList = pWorkmateList;
    }

    @NonNull
    @Override
    public WorkmateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_workmate_list_item,
                parent,false);
        return new WorkmateHolder(lView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateHolder pWorkmateHolder, int position) {
        String lTxtWorkmate ;

        Context lContext = pWorkmateHolder.itemView.getContext();

        lTxtWorkmate = mWorkmateList.get(position).getWkName()
                + " " + lContext.getString(R.string.text_workmate_joining);
        pWorkmateHolder.mTxtViewName.setText(lTxtWorkmate);

        Glide.with(pWorkmateHolder.mImgViewWorkmate.getContext())
                .load(mWorkmateList.get(position).getWkUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(pWorkmateHolder.mImgViewWorkmate);
    }


    @Override
    public int getItemCount() {
        if(mWorkmateList == null) {
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
