package com.bisonswap.bisonswap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.AddNewItem;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OfferMenu extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Button accept;
    private Button reject;
//    private Button extend;
    private Button shipped;
    private Button received;
    private Button feedback;
    private Button chat;

    private ImageView item_img;
    private TextView item_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_menu);

        // Get all the buttons and other stuff used on this page
        accept = (Button) findViewById(R.id.acceptOffer);
        reject = (Button) findViewById(R.id.rejectOffer);
//        extend = (Button) findViewById(R.id.extendOffer);
        shipped = (Button) findViewById(R.id.itemShipped);
        received = (Button) findViewById(R.id.itemReceived);
        feedback = (Button) findViewById(R.id.itemFeedback);
        chat = (Button) findViewById(R.id.chat);
        item_img = (ImageView) findViewById(R.id.iView);
        item_name = (TextView) findViewById(R.id.iName);

        accept.setVisibility(View.INVISIBLE);
        reject.setVisibility(View.INVISIBLE);
//        extend.setVisibility(View.INVISIBLE);
        shipped.setVisibility(View.INVISIBLE);
        received.setVisibility(View.INVISIBLE);
        feedback.setVisibility(View.INVISIBLE);

        // Get the extra information passed from the item activity
        int ownsItem = Integer.parseInt(getIntent().getStringExtra("ownsItem").toString());
        String itemKey = getIntent().getStringExtra("itemKey").toString();
        String offerKey = getIntent().getStringExtra("offerItemKey").toString();

        Log.d("KEYS:", Integer.toString(ownsItem) + ',' + itemKey + ',' + offerKey);
        switch(ownsItem) {
            case 0:
                // Case 0: This person owns the item

                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);
                shipped.setVisibility(View.VISIBLE);
                received.setVisibility(View.VISIBLE);
                feedback.setVisibility(View.VISIBLE);

                // Hide the extend button
//                extend.setVisibility(View.GONE);
                // Get the offered item's reference
                // offerBaseKey = items/offerBaseKey
                String offerBaseKey = getIntent().getStringExtra("offerKey").toString();
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference().child("items").child(offerBaseKey);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Use this to populate the ImageView and TextView
                        item_name.setText(dataSnapshot.child("itemName").getValue().toString());
                        Glide.with(OfferMenu.this)
                                .load("http://bisonswap.com/uploads/" + dataSnapshot.child("pic_1").getValue().toString())
                                .into(item_img);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case 1:
                // Case 1: This person has an offer open on the item

//                extend.setVisibility(View.VISIBLE);
                // Hide the accept and reject buttons
//                accept.setVisibility(View.GONE);
//                reject.setVisibility(View.GONE);
                break;

            default:
                // Do nothing
                break;
        }
    }

    // Open up chat with the person offering the item
    public void chat(View v) {
        Intent chatIntent = new Intent(this, Chat.class);
        String ownerEmail = getIntent().getStringExtra("ownerEmail");
        chatIntent.putExtra("ownerEmail", ownerEmail);
        startActivity(chatIntent);
    }

    // Extend the offer by resetting the date to the current time
    public void extendOffer(View v) {
        int ownsItem = Integer.parseInt(getIntent().getStringExtra("ownsItem").toString());
        final String itemKey = getIntent().getStringExtra("itemKey").toString();
        final String offerKey = getIntent().getStringExtra("offerItemKey").toString();
        database = FirebaseDatabase.getInstance();
        // Get the offer reference
        myRef = database.getReference("items").child(itemKey).child("offer");
        Query queryRef = myRef.orderByKey();

        // Note that this listener is not the typical one used in the rest of the app.
        // This is because the ValueEventListener was stuck in an infinite loop constantly
        // updating the date.
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> updates = new HashMap<>();
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    // Iterate through values in the offer reference and set the date to the current time.
                    updates.put("accepted", d.child("arrived").getValue());
                    updates.put("arrived", Integer.parseInt(d.child("arrived").getValue().toString()));
                    updates.put("date", System.currentTimeMillis());
                    updates.put("email", d.child("email").getValue().toString());
                    updates.put("item", d.child("item").getValue().toString());
                    updates.put("itemName", d.child("itemName").getValue().toString());
                    updates.put("rated", d.child("rated").getValue());
                    updates.put("shipped", d.child("shipped").getValue());
                    updates.put("uid", d.child("uid").getValue().toString());
                }
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("items/" + itemKey + "/offer/" + offerKey, updates);
                // Update the database with the new date
                database.getReference().updateChildren(childUpdates);
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Update items/itemKey/offer/offerKey and change accepted to 1.
    public void acceptOffer(View v) {
        int ownsItem = Integer.parseInt(getIntent().getStringExtra("ownsItem").toString());
        final String itemKey = getIntent().getStringExtra("itemKey").toString();
        final String offerKey = getIntent().getStringExtra("offerItemKey").toString();
//        Log.d("ITEMKEY", itemKey);
//        Log.d("OFFERKEY", offerKey);
        database = FirebaseDatabase.getInstance();
        // Get the offer reference
        myRef = database.getReference("items").child(itemKey).child("offer");
        Query queryRef = myRef.orderByKey();
        queryRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> updates = new HashMap<>();
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    // Iterate through values in the offer reference and set accepted to 1.
                    updates.put("accepted", 1);
                    updates.put("arrived", Integer.parseInt(d.child("arrived").getValue().toString()));
                    updates.put("date", d.child("date").getValue());
                    updates.put("email", d.child("email").getValue().toString());
                    updates.put("item", d.child("item").getValue().toString());
                    updates.put("itemName", d.child("itemName").getValue().toString());
                    updates.put("rated", d.child("rated").getValue());
                    updates.put("shipped", d.child("shipped").getValue());
                    updates.put("uid", d.child("uid").getValue().toString());
                    updates.put("pic_1", d.child("pic_1").getValue().toString());
                }
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("items/" + itemKey + "/offer/" + offerKey, updates);
                // Update the database with the accepted offer
                database.getReference().updateChildren(childUpdates);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Remove items/itemKey/offer/offerKey
    public void rejectOffer(View v) {
        int ownsItem = Integer.parseInt(getIntent().getStringExtra("ownsItem").toString());
        final String itemKey = getIntent().getStringExtra("itemKey").toString();
        final String offerKey = getIntent().getStringExtra("offerItemKey").toString();
//        Log.d("ITEMKEY", itemKey);
//        Log.d("OFFERKEY", offerKey);
        database = FirebaseDatabase.getInstance();
        // Get the offer reference
        myRef = database.getReference("items").child(itemKey).child("offer").child(offerKey);
        myRef.removeValue();
    }

    // Update your item's shipping status
    public void itemShipped(View v) {
        int ownsItem = Integer.parseInt(getIntent().getStringExtra("ownsItem").toString());
        final String itemKey = getIntent().getStringExtra("itemKey").toString();
        final String offerKey = getIntent().getStringExtra("offerItemKey").toString();
        Log.d("ITEM KEY", itemKey);
        database = FirebaseDatabase.getInstance();
        // Get the offer reference
        DatabaseReference shippedRef = database.getReference("items").child(itemKey);
//        myRef = database.getReference("items").child(itemKey);
        Query queryRef = shippedRef.orderByKey();
        queryRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.child("shipped").getRef().setValue(1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Set the value of arrived in items/itemKey/offer/offerKey to 1
    public void itemReceived(View v) {
        int ownsItem = Integer.parseInt(getIntent().getStringExtra("ownsItem").toString());
        final String itemKey = getIntent().getStringExtra("itemKey").toString();
        final String offerKey = getIntent().getStringExtra("offerItemKey").toString();
        database = FirebaseDatabase.getInstance();
        // Get the offer reference
        myRef = database.getReference("items").child(itemKey).child("offer").child(offerKey);
        Query queryRef = myRef.orderByKey();
        queryRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.child("arrived").getRef().setValue(1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void provideFeedback(View v) {
        Intent feedbackIntent = new Intent(this, OfferFeedback.class);
        feedbackIntent.putExtra("uid", getIntent().getStringExtra("uid"));
        feedbackIntent.putExtra("OfferMenu", "1");
        feedbackIntent.putExtra("itemKey", getIntent().getStringExtra("itemKey"));
        feedbackIntent.putExtra("offerKey", getIntent().getStringExtra("offerItemKey"));
        startActivity(feedbackIntent);
    }
}
