package com.fleb.go4lunch.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.fleb.go4lunch.R;

/**
 * Created by Florence LE BOURNOT on 09/10/2020
 */
public class RatingCalculation {

    public static int numberStarToDisplay(Context pContext, double pNote) {

        int lMaxNote = Integer.parseInt(pContext.getResources().getString(R.string.max_level_three_star));
        int lNbNote = Integer.parseInt(pContext.getResources().getString(R.string.nb_star));
        double lMaxLevelOneStar = Double.parseDouble(pContext.getResources().getString(R.string.max_level_one_star));
        double lMaxLevelTwoStar = Double.parseDouble(pContext.getResources().getString(R.string.max_level_two_star));
        double lNote;
        int lNbStarToDisplay;

        lNote = (pNote / lMaxNote) * lNbNote;

        if (lNote == 0) {
            lNbStarToDisplay = 0;
        } else if (lNote > 0 && lNote <= lMaxLevelOneStar) {
            lNbStarToDisplay = 1;
        } else if (lNote > lMaxLevelOneStar && lNote <= lMaxLevelTwoStar) {
            lNbStarToDisplay = 2;
        } else {
            lNbStarToDisplay = 3;
        }
        return lNbStarToDisplay;
    }
}
