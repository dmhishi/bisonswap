package com.bisonswap.bisonswap;

/**
 * Created by adlal on 3/10/2017.
 */

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

class CustomAdapter extends ArrayAdapter<ItemData>{

    public CustomAdapter( Context context,  ItemData[] items) {
        super(context, R.layout.item_row, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater bisonInflater = LayoutInflater.from(getContext());
        View customView = bisonInflater.inflate(R.layout.item_row, parent, false);

        ItemData singleItem = getItem(position);
        TextView bisonRating = (TextView) customView.findViewById(R.id.rating);
        TextView bisonDescription = (TextView) customView.findViewById(R.id.itDescription);
        TextView bisonText = (TextView) customView.findViewById(R.id.bisonItemText);
        ImageView bisonImage = (ImageView) customView.findViewById(R.id.bisonImage);
        bisonText.setText(singleItem.name);
        if(singleItem.description.length()>50)
            bisonDescription.setText(singleItem.description.substring(0, 50));
        else
            bisonDescription.setText(singleItem.description);
        bisonRating.setText(singleItem.rating);
        if(singleItem.name.equals("bison"))
            singleItem.ref = "http://bisonswap.com/img/logo-lq.png";
//        Log.d("SINGLE ITEM REF", singleItem.ref);
        Glide.with(getContext())
                .load(singleItem.ref)
                .into(bisonImage);
        return customView;
    }
}