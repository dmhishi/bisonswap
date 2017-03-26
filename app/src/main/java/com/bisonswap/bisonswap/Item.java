package com.bisonswap.bisonswap;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Destiny on 3/25/2017.
 */

@IgnoreExtraProperties
public class Item {
    public String name;
    public String category;
    public String condition;
    public String description;
    public String imgRef;
    public String ownerEmail;

    public Item(String name, String category, String rating, String description, String imgRef, String ownerEmail)
    {
        this.name = name;
        this.category = category;
        this.condition = rating;
        this.description = description;
        this.imgRef = imgRef;
        this.ownerEmail = ownerEmail;
    }

    //implement a push function here to item into database and image into storage
}
