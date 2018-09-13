package com.example.fraser.fndb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView;
    TextView nameText;
    TextView costText;
    TextView descText;

    int seasonNo;
    SeasonSelectActivity.SearchType searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.itemImage);
        nameText = findViewById(R.id.itemName);
        costText = findViewById(R.id.itemCost);
        descText = findViewById(R.id.itemDesc);

        Bundle extras = getIntent().getExtras();
        Skin skin = (Skin) extras.get("Skin");
        seasonNo = (int) extras.get("seasonNo");
        searchType = (SeasonSelectActivity.SearchType) extras.get("searchType");

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(skin.name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


        nameText.setText(skin.name);

        StorageReference imageStore = FirebaseStorage.getInstance().getReference();
        final StorageReference imageRef;
        if (searchType == SeasonSelectActivity.SearchType.BP)
        {
            imageRef = imageStore.child("s" + seasonNo + "_images");
        }
        else
        {
            imageRef = imageStore.child(searchType.toString().toLowerCase() + "_images");
        }

        try {
            final StorageReference fileRef = imageRef.child(skin.imageId + ".JPG");
            final File localFile = File.createTempFile(skin.imageId.toString(), ".jpg");

            fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bmp);

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}