package com.bisonswap.bisonswap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatPick extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<String> references;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_pick);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("messages");
        references = new ArrayList<>();
        Query queryRef = myRef.orderByKey();
//        final ListView refView = (ListView) findViewById(R.id.ref_view);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the emails
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    Log.d("KEY:", d.getKey());
                    references.add(d.getKey());
                }
                // Get the right emails from the keys
                ArrayList<String> emails = new ArrayList<>();
                for(int i = 0; i < references.size(); i++) {
                    if(references.get(i).contains("_BISONSWAP")) {
                        String[] keySplit = references.get(i).split("_BISONSWAP_");
                        keySplit[0] = keySplit[0].replaceAll("\\(", "\\.");
                        keySplit[1] = keySplit[1].replaceAll("\\(", "\\.");
                        Log.d("Keysplit 0", keySplit[0]);
                        Log.d("Keysplit 1", keySplit[1]);
                        if(keySplit[0].equals(mFirebaseUser.getEmail())) {
                            emails.add(keySplit[1]);
                        }
                        else if(keySplit[1].equals(mFirebaseUser.getEmail())){
                            emails.add(keySplit[0]);
                        }
                    }
                }
                ItemData[] refArray = new ItemData[emails.size()];
                for(int i = 0; i < emails.size(); i++) {
                    // Populate refArray with the emails
                    refArray[i] = new ItemData(emails.get(i), mFirebaseAuth.getCurrentUser().getPhotoUrl().toString());
                }
                // Populate the list view with emails
                ListAdapter bisonAdapter = new CustomAdapter(ChatPick.this, refArray);
                ListView bisonListView = (ListView) findViewById(R.id.ref_view);
                bisonListView.setAdapter(bisonAdapter);
                bisonListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ItemData stuff = (ItemData) parent.getItemAtPosition(position);
                                // Create an intent that passes the owner email parameter to
                                // Chat
//                                Intent ChatListIntent = new Intent(test.this, ChatList.class);
//                                ChatListIntent.putExtra("ownerEmail", String.valueOf(parent.getItemAtPosition(position)));
                                startActivity(new Intent(ChatPick.this, Chat.class).putExtra("ownerEmail", stuff.name));
                            }

                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_pick);
    }
}
