package com.fleb.go4lunch.viewmodel.restaurantlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;


/**
 * Created by Florence LE BOURNOT on 26/09/2020
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder> {

    private static final String TAG_LIST_RESTO = "TAG_LIST_RESTO";
    private List<Restaurant> mRestoList;
    private Bitmap mResult;
    private String mUrl;

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
        int lMaxNote = Integer.parseInt(lContext.getResources().getString(R.string.max_level_three_star));
        int lNbNote = Integer.parseInt(lContext.getResources().getString(R.string.nb_star));
        double lMaxLevelOneStar = Double.parseDouble(lContext.getResources().getString(R.string.max_level_one_star));
        double lMaxLevelTwoStar = Double.parseDouble(lContext.getResources().getString(R.string.max_level_two_star));
        double lNote;
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

        if (mRestoList.get(position).getRestoRating() > 0) {
            lNote = mRestoList.get(position).getRestoRating();
            lNote = (lNote / lMaxNote) * lNbNote;
        } else {
            lNote = 0;
        }

        if (lNote == 0) {
            Log.d(TAG_LIST_RESTO, "onBindViewHolder: 3 stars to hide : " + lNote);
            pRestoHolder.mRestoNote1.setVisibility(View.INVISIBLE);
            pRestoHolder.mRestoNote2.setVisibility(View.INVISIBLE);
            pRestoHolder.mRestoNote3.setVisibility(View.INVISIBLE);
        } else if (lNote > 0 && lNote <= lMaxLevelOneStar) {
            Log.d(TAG_LIST_RESTO, "onBindViewHolder: 2 stars to hide : " + lNote);
            pRestoHolder.mRestoNote2.setVisibility(View.INVISIBLE);
            pRestoHolder.mRestoNote3.setVisibility(View.INVISIBLE);
        } else if (lNote > lMaxLevelOneStar && lNote <= lMaxLevelTwoStar) {
            Log.d(TAG_LIST_RESTO, "onBindViewHolder: 1 star to hide : " + lNote);
            pRestoHolder.mRestoNote3.setVisibility(View.INVISIBLE);
        } else {
            Log.d(TAG_LIST_RESTO, "onBindViewHolder: no star to hide : " + lNote);
        }

        if (mRestoList.get(position).getRestoPhotoUrl() != null ) {
            mUrl = mRestoList.get(position).getRestoPhotoUrl();
            try {
                downLoadImage(mRestoList.get(position).getRestoPhotoUrl());
            } catch (IOException pE) {
                pE.printStackTrace();
            }
            if (mResult != null) {
                pRestoHolder.mRestoImage.setImageBitmap(mResult);
            }
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

    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTasks extends AsyncTask<URL, Integer, Bitmap> {


        protected Bitmap doInBackground(URL... urls) {
            URL url = null;
            try {
                url = new URL(mUrl);
            } catch (MalformedURLException pE) {
                pE.printStackTrace();
            }

            HttpURLConnection httpConn = null;
            int resCode = 0;
            try {
                httpConn = (HttpURLConnection) Objects.requireNonNull(url).openConnection();
                Objects.requireNonNull(httpConn).connect();
                resCode = Objects.requireNonNull(httpConn).getResponseCode();
            } catch (IOException pE) {
                pE.printStackTrace();
            }
            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream in = null;
                try {
                    in = httpConn.getInputStream();
                } catch (IOException pE) {
                    pE.printStackTrace();
                }
                return BitmapFactory.decodeStream(in);
            } else {
                return null;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Bitmap result) {
            //showDialog("Downloaded " + result + " bytes");
            mResult = result;
        }
    }


    public void downLoadImage(String pUrl) throws IOException {

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

/*        URL url = new URL(pUrl);

        HttpURLConnection httpConn = (HttpURLConnection) Objects.requireNonNull(url).openConnection();
        Objects.requireNonNull(httpConn).connect();
        int resCode = Objects.requireNonNull(httpConn).getResponseCode();

        if (resCode == HttpURLConnection.HTTP_OK) {
            InputStream in = httpConn.getInputStream();
            return BitmapFactory.decodeStream(in);
        } else {
            return null;
        }*/
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
