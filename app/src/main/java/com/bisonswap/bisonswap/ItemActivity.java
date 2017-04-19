package com.bisonswap.bisonswap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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

    String itemKey;
    String itemName;
    String itemDescription;
    String ownerEmail;
    String imgRef;

    ImageView imageView;
    TextView name;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        itemKey = "-Ki1Vr8l8T2YJEFgZXKI"; // getIntent().getStringExtra("itemKey");
        dRef = FirebaseDatabase.getInstance().getReference();
        iRef = dRef.child("items").child(itemKey);

        name = (TextView) findViewById(R.id.iName);
        description = (TextView) findViewById(R.id.iDescription);
        imageView = (ImageView) findViewById(R.id.iView);

        iRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemName = dataSnapshot.child("itemName").getValue().toString();
                name.setText(itemName);
                itemDescription = dataSnapshot.child("itemDescription").getValue().toString();
                description.setText(itemDescription);
                ownerEmail = dataSnapshot.child("email").getValue().toString();
                imgRef = dataSnapshot.child("pic_1").getValue().toString();
                Glide.with(ItemActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(FirebaseStorage.getInstance().getReference().child(imgRef))
                        .into(imageView);
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
