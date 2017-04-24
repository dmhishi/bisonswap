package com.bisonswap.bisonswap;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Destiny on 3/25/2017.
 */

@IgnoreExtraProperties
public class Item {
    public String date;
    public String email;
    public String itemCategory;
    public String itemDescription;
    public String itemName;
    public String pic_1;
    public String rating;
    public int arrived;
    public int shipped;
    public int rated;
    public String uid;
//    public String name;
//    public String category;
//    public String condition;
//    public String description;
//    public String imgName;
//    public String ownerEmail;

    public Item()
    {

    }

    public Item(int arrived, String email, String itemCategory, String itemDescription, String itemName, String pic_1, int rated, String rating, int shipped, String uid)
    {
        this.arrived = arrived;
        this.date = String.valueOf(System.currentTimeMillis());
        this.email = email;
        this.itemCategory = itemCategory;
        this.itemDescription = itemDescription;
        this.itemName = itemName;
        this.pic_1 = pic_1;
        this.rated = rated;
        this.rating = rating;
        this.shipped = shipped;
        this.uid = uid;
//        this.name = name;
//        this.category = category;
//        this.condition = rating;
//        this.description = description;
//        this.imgName = imgName;
//        this.ownerEmail = ownerEmail;
    }

    //implement a push function here to item into database and image into storage
}
