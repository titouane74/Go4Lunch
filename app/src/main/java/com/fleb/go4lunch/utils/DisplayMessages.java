package com.fleb.go4lunch.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Florence LE BOURNOT on 04/07/2020
 */
public class DisplayMessages {
    public static void displayShortMessage(Context pContext, String pMessage) {
        Toast.makeText(pContext,pMessage,Toast.LENGTH_SHORT).show();
    }

}
