package com.fleb.go4lunch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Florence LE BOURNOT on 04/10/2020
 */
public class Post {

    private int userId;
    private int id;
    private String title;

    @SerializedName("body")
    private String text;

    public int getUserId() { return userId; }

    public int getId() { return id; }

    public String getTitle() { return title; }

    public String getText() { return text; }
}
