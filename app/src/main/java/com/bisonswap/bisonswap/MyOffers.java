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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
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
// TODO: Currently this will display all the items that a user has made an offer on.
// TODO: It needs to display more information, like what item the user offered.
public class MyOffers extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference itemRef;
    private ArrayList<String> references;
    private ArrayList<String> itemNames;
    private ArrayList<String> imgRefArrayList;
    private ArrayList<String> itemKeys;
    private ArrayList<String> offerKeys;
    private ArrayList<String> offeredItems;
    private ArrayList<String> offerBaseKeys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);
        mFirebaseAuth = FirebaseAuth.getInstance();
        final String currentUid = mFirebaseAuth.getCurrentUser().getUid();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        itemRef = database.getReference("items");
        imgRefArrayList = new ArrayList<>();
        itemNames = new ArrayList<>();
        itemKeys = new ArrayList<>();
        references = new ArrayList<>();
        offerKeys = new ArrayList<>();
        offeredItems = new ArrayList<>();
        offerBaseKeys = new ArrayList<>();
        Query queryRef = itemRef.orderByKey();

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    // Iterate through item offers and find your offers
                    if(d.child("offer").exists()) {
//                        Log.d("OFFER", "OFFER EXISTS");
                        // If you have made an offer set this to true
                        boolean hasMadeOffer = false;
                        for(DataSnapshot offer: d.child("offer").getChildren()) {
//                            Log.d("OFFER INFO", offer.getKey());
                            if(offer.child("uid").getValue().toString().equals(currentUid)) {
                                // If you made this offer it should be displayed
                                hasMadeOffer = true;
                                // Add the item key and the offered item's name to arraylists
                                offerKeys.add(offer.child("item").getValue().toString());
                                offeredItems.add(offer.child("itemName").getValue().toString());
                                offerBaseKeys.add(offer.getKey());
                            }
                        }
                        String itemName = d.child("itemName").getValue().toString();
                        String itemDescription = d.child("itemDescription").getValue().toString();
                        String imgRef = d.child("pic_1").getValue().toString();
                        String uid = d.child("uid").getValue().toString();
                        if(hasMadeOffer) {
                            itemKeys.add(d.getKey());
                            itemNames.add(itemName);
                            imgRefArrayList.add(imgRef);
                            references.add(d.child("itemName").getValue().toString());
                        }
                    }
                }
                Log.d("offers", offeredItems.toString());
                ArrayList<String> items = new ArrayList<>();
                for(int i = 0; i < references.size(); i++) {
                    items.add(references.get(i));

                }

                String[] nameArray = new String[items.size()];
                for(int i = 0; i < items.size(); i++) {
                    // Populate refArray with the emails
                    nameArray[i] = items.get(i);
                }

                ListAdapter bisonAdapter = new CustomAdapter(MyOffers.this, nameArray);
                ListView bisonListView = (ListView) findViewById(R.id.item_view);
                bisonListView.setAdapter(bisonAdapter);
                bisonListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // TODO: Fix this and have it properly load thumbnails
                                ImageView imageView = (ImageView) findViewById(R.id.bisonImage);
                                String imgRef = imgRefArrayList.get(position);
                                //Log.d("IMGREF", imgRef);
                                Glide.with(MyOffers.this)
                                        .load("http://bisonswap.com/uploads/" + imgRef)
                                        .into(imageView);
                                String itemKey = itemKeys.get(position);
                                String stuff = String.valueOf(parent.getItemAtPosition(position));
                                Intent o_Menu = new Intent(MyOffers.this, OfferMenu.class);
                                o_Menu.putExtra("ownsItem", "1");
                                o_Menu.putExtra("offerItemKey", offerBaseKeys.get(position));
                                o_Menu.putExtra("itemKey", itemKey);
                                startActivity(o_Menu);
//                                startActivity((new Intent(MyOffers.this, ItemActivity.class)).putExtra("itemKey", itemKey));
                            }

                        }
                );

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
