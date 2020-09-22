package com.fleb.go4lunch.viewmodel;

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
import com.fleb.go4lunch.model.Workmate;

import java.util.List;

/**
 * Created by Florence LE BOURNOT on 22/09/2020
 */
public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateAdapter.WorkmateHolder> {

    private List<Workmate> mWorkmateList;

    public void setWorkmateList(List<Workmate> pWorkmateList) {
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
        pWorkmateHolder.lTxtViewName.setText(mWorkmateList.get(position).getWorkmateName());
        Glide.with(pWorkmateHolder.lImgViewWorkmate.getContext())
                .load(mWorkmateList.get(position).getWorkmatePhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(pWorkmateHolder.lImgViewWorkmate);
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

        private TextView lTxtViewName;
        private ImageView lImgViewWorkmate;

        public WorkmateHolder(@NonNull View itemView) {
            super(itemView);

            lTxtViewName = itemView.findViewById(R.id.item_list_workmate_name);
            lImgViewWorkmate = itemView.findViewById(R.id.item_list_workmate_img);
        }
    }
}
