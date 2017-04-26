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
import android.widget.Spinner;
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

public class OfferFeedback extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Button submit;
    private Spinner rateSpinner;
    private Spinner rateSpinner2;
    private Spinner rateSpinner3;
    private int rate;
    private int rate2;
    private int rate3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_feedback);
        String uid = getIntent().getStringExtra("uid");
        Log.d("UID", uid);
        submit = (Button) findViewById(R.id.Submit);

        rateSpinner = (Spinner) findViewById(R.id.itemRating);
        ArrayAdapter<CharSequence> offerSpinner = ArrayAdapter.createFromResource(this,
                R.array.offer_feedback_spinner, android.R.layout.simple_spinner_item);
        offerSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rateSpinner.setAdapter(offerSpinner);

        rateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rate = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rateSpinner2 = (Spinner) findViewById(R.id.itemRating2);
        ArrayAdapter<CharSequence> offerSpinner2 = ArrayAdapter.createFromResource(this,
                R.array.offer_feedback_spinner2, android.R.layout.simple_spinner_item);
        offerSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rateSpinner2.setAdapter(offerSpinner2);

        rateSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rate2 = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rateSpinner3 = (Spinner) findViewById(R.id.itemRating3);
        ArrayAdapter<CharSequence> offerSpinner3 = ArrayAdapter.createFromResource(this,
                R.array.offer_feedback_spinner3, android.R.layout.simple_spinner_item);
        offerSpinner3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rateSpinner3.setAdapter(offerSpinner3);

        rateSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rate3 = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // Submit a rating for the user
    public void submitFeedback(View v) {
        Toast.makeText(getApplicationContext(), "Thanks for leaving feedback!", Toast.LENGTH_SHORT).show();
        Log.d("RATING", String.valueOf(rate));
        String uid = getIntent().getStringExtra("uid");
        String itemKey = getIntent().getStringExtra("itemKey");
        String offerKey = getIntent().getStringExtra("offerKey");
        Log.d("UID", uid);
        database = FirebaseDatabase.getInstance();
        // Update the users database and add a new rating
        myRef = database.getReference().child("users").child(uid);
        myRef.child(String.valueOf(System.currentTimeMillis())).setValue(new Rating(rate+rate2+rate3));
        if(getIntent().getStringExtra("OfferMenu").equals("1")) {
            // This person came from OfferMenu.java
            DatabaseReference myRef2 = database.getReference().child("items").child(itemKey).child("offer").child(offerKey);
            myRef2.child("rated").setValue(1);
        }
        else if(getIntent().getStringExtra("OfferMenu").equals("0")) {
            // This person came from OfferMenu_manageOffers.java
            DatabaseReference myRef2 = database.getReference().child("items").child(itemKey);
            myRef2.child("rated").setValue(1);
        }
        startActivity(new Intent(OfferFeedback.this, MainActivity.class));
    }
    class Rating {
        public int rating;

        public Rating(int rating) {
            this.rating = rating;
        }
    }
}
