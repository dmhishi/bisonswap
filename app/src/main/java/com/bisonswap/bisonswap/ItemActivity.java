package com.bisonswap.bisonswap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
    DatabaseReference dRef;
    DatabaseReference iRef;
    private FirebaseAuth mFirebaseAuth;

    String itemKey;
    String itemName;
    String itemDescription;
    String ownerEmail;
    String imgRef;
    String ownerUid;
    String offer_img;

    ImageView imageView;
    TextView name;
    TextView description;
    Button makeOffer;
    ListView offerView;

    ArrayList<String> offeredItemKey;
    ArrayList<String> offeredItemName;
    ArrayList<String> offeredItemUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // Use authentication to determine if the current user owns the item
        mFirebaseAuth = FirebaseAuth.getInstance();
        final String currentUid = mFirebaseAuth.getCurrentUser().getUid().toString();

        itemKey = getIntent().getStringExtra("itemKey");
        dRef = FirebaseDatabase.getInstance().getReference();
        iRef = dRef.child("items").child(itemKey);

        name = (TextView) findViewById(R.id.iName);
        description = (TextView) findViewById(R.id.iDescription);
        imageView = (ImageView) findViewById(R.id.iView);
        makeOffer = (Button) findViewById(R.id.makeOffer);

        offeredItemKey = new ArrayList<>();
        offeredItemName = new ArrayList<>();
        offeredItemUid = new ArrayList<>();

        iRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(currentUid.equals(dataSnapshot.child("uid").getValue().toString())) {
                    // This will trigger if the current user owns the item they are viewing
                    itemName = dataSnapshot.child("itemName").getValue().toString();
                    name.setText(itemName);
                    itemDescription = dataSnapshot.child("itemDescription").getValue().toString();
                    description.setText(itemDescription);
                    ownerEmail = dataSnapshot.child("email").getValue().toString();
                    imgRef = dataSnapshot.child("pic_1").getValue().toString();
                    Glide.with(ItemActivity.this)
                            //.using(new FirebaseImageLoader())
                            // This load will have to change based on URL...
                            .load("http://bisonswap.com/uploads/" + imgRef)
                            .into(imageView);
                    // Remove the make offer button if the user owns the item
                    makeOffer.setVisibility(View.GONE);

                    // Populate the offers list view
                    if(dataSnapshot.child("offer").exists()) {
                        // If there are offers for this item display them
                        for(DataSnapshot offer: dataSnapshot.child("offer").getChildren()) {
                            String offer_itemName = offer.child("itemName").getValue().toString();
                            String offer_itemKey = offer.child("item").getValue().toString();
                            String offer_uid = offer.child("uid").getValue().toString();
                            offeredItemName.add(offer_itemName);
                            offeredItemKey.add(offer_itemKey);
                            offeredItemUid.add(offer_uid);
                            // TODO: Get the offered items picture
                        }
                        String[] nameArray = new String[offeredItemName.size()];
                        for(int i = 0; i < offeredItemName.size(); i++) {
                            // Populate nameArray with item names
                            nameArray[i] = offeredItemName.get(i);
                        }
                        // TODO: Make a custom activity to accept or reject offers
                        ListAdapter bisonAdapter = new CustomAdapter(ItemActivity.this, nameArray);
                        ListView bisonListView = (ListView) findViewById(R.id.offer_view);
                        bisonListView.setAdapter(bisonAdapter);
                        bisonListView.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        // TODO: Fix this and have it properly load thumbnails
                                        ImageView imageView = (ImageView) findViewById(R.id.bisonImage);
                                        // TODO: imgRef is just a placeholder, it needs to actually get an image of the offered item
                                        String imgRef = "http://bisonswap.com/uploads/images~ZxNPU2WLthcretRrOrp0b86bmKc2~apple%20watch.png";
                                        //Log.d("IMGREF", imgRef);
                                        Glide.with(ItemActivity.this)
                                                .load("http://bisonswap.com/uploads/" + imgRef)
                                                .into(imageView);
                                        String o_itemKey = offeredItemKey.get(position);
                                        String stuff = String.valueOf(parent.getItemAtPosition(position));
                                        startActivity((new Intent(ItemActivity.this, ItemActivity.class)).putExtra("itemKey", o_itemKey));
                                    }

                                }
                        );
                    }
                }
                else {
                    itemName = dataSnapshot.child("itemName").getValue().toString();
                    name.setText(itemName);
                    itemDescription = dataSnapshot.child("itemDescription").getValue().toString();
                    description.setText(itemDescription);
                    ownerEmail = dataSnapshot.child("email").getValue().toString();
                    imgRef = dataSnapshot.child("pic_1").getValue().toString();
                    Glide.with(ItemActivity.this)
                            //.using(new FirebaseImageLoader())
                            // This load will have to change based on URL...
                            .load("http://bisonswap.com/uploads/" + imgRef)
                            .into(imageView);
                    // Do not display the offers this item has received if the user does not own it
                    offerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void chat(View v) {
        Intent chat = new Intent(this, Chat.class);
        chat.putExtra("ownerEmail", ownerEmail);
        startActivity(chat);
    }
}
