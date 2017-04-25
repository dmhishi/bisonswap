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
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
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

// TODO: Add a description to the textview that informs you of what item you offered
public class OfferMenu_manageOffers extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Button chat;
    private Button extend;
    private Button shipped;
    private Button received;
    private Button feedback;
    private ImageView iView;
    private TextView iName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_menu_manage_offers);

        chat = (Button) findViewById(R.id.chat);
        extend = (Button) findViewById(R.id.extendOffer);
        shipped = (Button) findViewById(R.id.iShipped);
        received = (Button) findViewById(R.id.iReceived);
        feedback = (Button) findViewById(R.id.iFeedback);
        iView = (ImageView) findViewById(R.id.iView);
        iName = (TextView) findViewById(R.id.iName);

        String offerItemKey = getIntent().getStringExtra("offerItemKey");
        String itemKey = getIntent().getStringExtra("itemKey");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("items").child(itemKey);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                iName.setText(dataSnapshot.child("itemName").getValue().toString());
                Glide.with(OfferMenu_manageOffers.this)
                        .load("http://bisonswap.com/uploads/" + dataSnapshot.child("pic_1").getValue().toString())
                        .into(iView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Open up chat with the person that you made an offer to.
    public void chat(View v) {
//        FirebaseAuth fAuth = FirebaseAuth.getInstance();
//        FirebaseUser fUser = fAuth.getCurrentUser();
//        String user_email = fUser.getEmail();
        final String itemKey = getIntent().getStringExtra("itemKey").toString();
        final String offerKey = getIntent().getStringExtra("offerItemKey").toString();
        database = FirebaseDatabase.getInstance();
        // Get the offer reference
        myRef = database.getReference("items").child(itemKey);
        Query queryRef = myRef.orderByKey();
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String other_email = dataSnapshot.child("email").getValue().toString();
                Intent chatIntent = new Intent(OfferMenu_manageOffers.this, Chat.class);
                chatIntent.putExtra("ownerEmail", other_email);
                startActivity(chatIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Extend the offer by resetting the date in items/itemKey/offer/offerKey
    public void extendOffer(View v) {
        final String itemKey = getIntent().getStringExtra("itemKey").toString();
        final String offerKey = getIntent().getStringExtra("offerItemKey").toString();
        database = FirebaseDatabase.getInstance();
        // Get the offer reference
        myRef = database.getReference("items").child(itemKey).child("offer").child(offerKey);
        Query queryRef = myRef.orderByKey();
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.child("date").getRef().setValue(System.currentTimeMillis());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void itemShipped(View v) {
        final String itemKey = getIntent().getStringExtra("itemKey").toString();
        final String offerKey = getIntent().getStringExtra("offerItemKey").toString();
        database = FirebaseDatabase.getInstance();
        // Get the offer reference
        myRef = database.getReference("items").child(itemKey).child("offer").child(offerKey);
        Query queryRef = myRef.orderByKey();
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.child("shipped").getRef().setValue(1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void itemReceived(View v) {
        final String itemKey = getIntent().getStringExtra("itemKey").toString();
        final String offerKey = getIntent().getStringExtra("offerItemKey").toString();
        database = FirebaseDatabase.getInstance();
        // Get the offer reference
        myRef = database.getReference("items").child(itemKey);
        Query queryRef = myRef.orderByKey();
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {

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
        final String itemKey = getIntent().getStringExtra("itemKey").toString();
        final String offerKey = getIntent().getStringExtra("offerItemKey").toString();
        database = FirebaseDatabase.getInstance();
        // Get the offer reference
        myRef = database.getReference("items").child(itemKey);
        Query queryRef = myRef.orderByKey();
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent feedbackIntent = new Intent(OfferMenu_manageOffers.this, OfferFeedback.class);
                feedbackIntent.putExtra("uid", dataSnapshot.child("uid").getValue().toString());
                feedbackIntent.putExtra("OfferMenu", "0");
                feedbackIntent.putExtra("itemKey", itemKey);
                feedbackIntent.putExtra("offerKey", offerKey);
                startActivity(feedbackIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
