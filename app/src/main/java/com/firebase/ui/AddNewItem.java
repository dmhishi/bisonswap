package com.firebase.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bisonswap.bisonswap.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.net.URI;

public class AddNewItem extends AppCompatActivity {

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String itemName;
    private String itemCategory;
    private String itemRating;
    private String itemDescription;

    private Uri[] imgUriArray = new Uri[5];
    private int IMAGE_INDEX;

    private static final String STORAGE_PATH = "image/";
    private static final String DATABASE_PATH = "image";
    private static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        Spinner categorySpinner = (Spinner) findViewById(R.id.itemCategory);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        Spinner ratingSpinner = (Spinner) findViewById(R.id.itemCategory);
        ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(this,
                R.array.rating_array, android.R.layout.simple_spinner_item);
        ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemCategory = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemRating = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUriArray[IMAGE_INDEX] = data.getData();
        }
    }

    private String getImageExt(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    public void sendData(View v) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Submitting...");
        dialog.show();
        //remember to handle all images chosen
        StorageReference sRef = storageReference.child(STORAGE_PATH + System.currentTimeMillis()+getImageExt(imgUriArray[0]));
        itemName = ((EditText) findViewById(R.id.itemName)).getText().toString();
        itemDescription = ((EditText) findViewById(R.id.itemDescription)).getText().toString();
    }

    public void browseImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
    }
}
