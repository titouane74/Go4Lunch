package com.fleb.go4lunch.utilstest;

import com.fleb.go4lunch.model.Workmate;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florence LE BOURNOT on 13/11/2020
 */
public class WorkmateUtils {


    public static Workmate generateWorkmateName(String pWorkmateId, String pWorkmateName) {
        return new Workmate(pWorkmateId, pWorkmateName);
    }

    public static Workmate generateWorkmateLikes(String pWorkmateId) {
        List<Workmate.Likes> lLikesList = generateLikes();

        return new Workmate(pWorkmateId,lLikesList);
    }

    public static Workmate generateWorkmateNameEmailPhoto(String pWorkmateId, String pWorkmateName) {
        return new Workmate(pWorkmateId, pWorkmateName,
                "Workmate@go4lunch.com", "Workmate_url_photo");
    }

    public static Workmate generateWorkmateNameEmailPhotoRestoChoice(String pWorkmateId, String pWorkmateName) {
        Workmate.WorkmateRestoChoice lRestoChoice = generateRestoChoice();

        return new Workmate(pWorkmateId, pWorkmateName,
                "Workmate@go4lunch.com", "Workmate_url_photo",
                lRestoChoice);
    }

    public static Workmate generateWorkmateNameEmailPhotoRestoChoiceLikes(String pWorkmateId, String pWorkmateName) {
        Workmate.WorkmateRestoChoice lRestoChoice = generateRestoChoice();
        List<Workmate.Likes> lLikesList = generateLikes();

        return new Workmate(pWorkmateId, pWorkmateName,
                "Workmate@go4lunch.com", "Workmate_url_photo",
                lRestoChoice, lLikesList);
    }


    public static Workmate.WorkmateRestoChoice generateRestoChoice() {
        return new Workmate.WorkmateRestoChoice("resto1_id", "resto1_name" , (Timestamp.now()));
    }

    public static List<Workmate.Likes> generateLikes() {
        List<Workmate.Likes> lLikesList = new ArrayList<>();

        Workmate.Likes lLikes1 = new Workmate.Likes("resto1_id", "resto1_name");
        Workmate.Likes lLikes2 = new Workmate.Likes("resto2_id", "resto2_name");
        Workmate.Likes lLikes3 = new Workmate.Likes("resto3_id", "resto3_name");

        lLikesList.add(lLikes1);
        lLikesList.add(lLikes2);
        lLikesList.add(lLikes3);

        return lLikesList;
    }
}
