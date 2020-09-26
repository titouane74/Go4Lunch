package com.fleb.go4lunch.viewmodel.restaurantlist;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * Created by Florence LE BOURNOT on 26/09/2020
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder> {

    private List<Restaurant> mRestoList;

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
        pRestoHolder.mRestoName.setText(mRestoList.get(position).getRestoName());
        pRestoHolder.mRestoDistance.setText(mRestoList.get(position).getRestoDistance());
        pRestoHolder.mRestoAddress.setText(mRestoList.get(position).getRestoAddress());
        pRestoHolder.mRestoNbWorkmates.setText(Integer.toString(mRestoList.get(position).getRestoNbWorkmates()));
        pRestoHolder.mRestoOpening.setText(mRestoList.get(position).getRestoOpening());
        //TODO Calculer le nombre d'étoile à afficher en fonction de la note
        // VISIBLE par defaut pour le moment
/*
        pRestoHolder.mRestoNote1.setVisibility(View.VISIBLE);
        pRestoHolder.mRestoNote2.setVisibility(View.VISIBLE);
        pRestoHolder.mRestoNote3.setVisibility(View.VISIBLE);


        Bitmap lImg = null;
        try {
            lImg = downLoadImage(mRestoList.get(position).getRestoPhotoUrl());
        } catch (IOException pE) {
            pE.printStackTrace();
        }
        if (lImg != null) {
            pRestoHolder.mRestoImage.setImageBitmap(lImg);
        }
*/
    }

    @Override
    public int getItemCount() {
        if (mRestoList == null) {
            return 0;
        } else {
            return mRestoList.size();
        }
    }

    public Bitmap downLoadImage(String pUrl) throws IOException {

        URL url = new URL(pUrl);
        HttpURLConnection httpConn = (HttpURLConnection) Objects.requireNonNull(url).openConnection();
        Objects.requireNonNull(httpConn).connect();
        int resCode = Objects.requireNonNull(httpConn).getResponseCode();

        if (resCode == HttpURLConnection.HTTP_OK) {
            InputStream in = httpConn.getInputStream();
            return BitmapFactory.decodeStream(in);
        } else {
            return null;
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
            mRestoNote1 = itemView.findViewById(R.id.img_note1);
            mRestoNote2 = itemView.findViewById(R.id.img_note2);
            mRestoNote3 = itemView.findViewById(R.id.img_note3);
            mRestoImage = itemView.findViewById(R.id.img_restaurant);

        }
    }
}
