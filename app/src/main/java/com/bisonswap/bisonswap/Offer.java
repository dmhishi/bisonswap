package com.bisonswap.bisonswap;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by fischer on 4/23/17.
 */

@IgnoreExtraProperties
public class Offer {
    public int accepted;
    public int arrived;
    public String date;
    public String email;
    public String item;
    public String itemName;
    public int rated;
    public int shipped;
    public String uid;

    public Offer(int accepted, int arrived, String email, String item, String itemName, int rated, int shipped, String uid) {
        this.accepted = accepted;
        this.arrived = arrived;
        this.date = String.valueOf(System.currentTimeMillis());
        this.email = email;
        this.item = item;
        this.itemName = itemName;
        this.rated = rated;
        this.shipped = shipped;
        this.uid = uid;
    }
}