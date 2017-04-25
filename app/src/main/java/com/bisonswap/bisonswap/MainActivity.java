package com.bisonswap.bisonswap;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<String> references;
    private ArrayList<String> itemNames;
    private ArrayList<String> imgRefArrayList;
    private ArrayList<String> itemKeys;
    private ArrayList<ItemData> itemDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignIn.class));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemDataList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("items");
        references = new ArrayList<>();
        imgRefArrayList = new ArrayList<>();
        itemNames = new ArrayList<>();
        itemKeys = new ArrayList<>();
        Query queryRef = myRef.orderByKey();

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    Log.d("KEY:", d.getKey());
                    String itemName = d.child("itemName").getValue().toString();
                    String itemDescription = d.child("itemDescription").getValue().toString();
                    String itemRating = d.child("rating").getValue().toString();
                    String imgRef = d.child("pic_1").getValue().toString();
//
                    itemKeys.add(d.getKey());
                    itemNames.add(itemName);
                    imgRefArrayList.add(imgRef);
                    references.add(d.child("itemName").getValue().toString());

                    itemDataList.add(new ItemData(itemName, "http://bisonswap.com/uploads/"+imgRef, itemDescription, itemRating));
                }
                ArrayList<ItemData> items = new ArrayList<>();
                for(int i = 0; i < references.size(); i++) {
                    items.add(itemDataList.get(i));

                }

                ItemData[] itemArray = new ItemData[items.size()];
                for(int i = 0; i < items.size(); i++) {
                    // Populate refArray with the emails
                    itemArray[i] = items.get(i);
                }

                ListAdapter bisonAdapter = new CustomAdapter(MainActivity.this, itemArray);
                ListView bisonListView = (ListView) findViewById(R.id.bison_listview);
                bisonListView.setAdapter(bisonAdapter);
                //TODO: Change this to have to go to page
                bisonListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ImageView imageView = (ImageView) findViewById(R.id.bisonImage);
                                String imgRef = imgRefArrayList.get(position);
//                                Glide.with(MainActivity.this)
//                                        .using(new FirebaseImageLoader())
//                                        .load(FirebaseStorage.getInstance().getReference().child(imgRef))
//                                        .into(imageView);
                                String itemKey = itemKeys.get(position);
                                String stuff = String.valueOf(parent.getItemAtPosition(position));
                                startActivity((new Intent(MainActivity.this, ItemActivity.class)).putExtra("itemKey", itemKey));
                            }
                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //useful if we need to add a floating button
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_add_new_item) {
            startActivity(new Intent(this, AddItem.class));
        } else if (id == R.id.my_items) {
            startActivity(new Intent(this, MyItems.class));
        } else if (id == R.id.nav_manage) {

        }
        else if (id == R.id.nav_signout) {
            // TODO: Let users choose a different account to sign in with
            mFirebaseAuth.signOut();
            startActivity(new Intent(this, SignIn.class));
        }
        else if (id == R.id.chat) {
            startActivity(new Intent(this, Chat.class));
        }
        else if(id == R.id.chat_pick) {
            startActivity(new Intent(this, ChatPick.class));
        }
        else if(id == R.id.my_offers) {
            startActivity(new Intent(this, MyOffers.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

class ItemData
{
    public String name;
    public String ref;
    public String description;
    public String rating;

    ItemData(String name, String ref)
    {
        this.name = name;
        this.ref = ref;
        this.description = "";
        this.rating = "";
    }

    ItemData(String name, String ref, String description, String rating)
    {
        this.name = name;
        this.ref = ref;
        this.description = description;
        this.rating = rating;
    }

}