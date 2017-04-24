package com.bisonswap.bisonswap;

/**
 * Created by adlal on 3/10/2017.
 */

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

class CustomAdapter extends ArrayAdapter<ItemData>{

    public CustomAdapter( Context context,  ItemData[] items) {
        super(context, R.layout.custom_row, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater bisonInflater = LayoutInflater.from(getContext());
        View customView = bisonInflater.inflate(R.layout.custom_row, parent, false);

        ItemData singleItem = getItem(position);
        TextView bisonText = (TextView) customView.findViewById(R.id.bisonItemText);
        ImageView bisonImage = (ImageView) customView.findViewById(R.id.bisonImage);

        bisonText.setText(singleItem.name);
        Glide.with(getContext())
                .load(singleItem.ref)
                .into(bisonImage);
        return customView;
    }
}
