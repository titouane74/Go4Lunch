package com.fleb.go4lunch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Florence LE BOURNOT on 04/10/2020
 */
public class CommentPost {
    private int postId;
    private int id;
    private String name;
    private String email;
    @SerializedName("body")
    private String text;

    public int getPostId() { return postId; }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getText() { return text; }
}
