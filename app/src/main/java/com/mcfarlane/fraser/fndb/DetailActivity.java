
/*
    © 2018 Fraser McFarlane
 */

package com.mcfarlane.fraser.fndb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView;
    ImageView currencyIcon;
    TextView nameText;
    TextView costText;
    TextView descText;

    int seasonNo;
    SeasonSelectActivity.SearchType searchType;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        boolean adsBool;
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.LOCAL),Context.MODE_PRIVATE);
        adsBool = sharedPreferences.getBoolean(getString(R.string.SP_ADS),true);

        AdManager adManager = new AdManager(this, adsBool);
        adManager.CreateAndLoadBanner();

        imageView = findViewById(R.id.itemImage);
        currencyIcon = findViewById(R.id.currencyIcon);
        nameText = findViewById(R.id.itemName);
        costText = findViewById(R.id.itemCost);
        descText = findViewById(R.id.itemDesc);

        Bundle extras = getIntent().getExtras();
        Skin skin = (Skin) extras.get("Skin");
        seasonNo = (int) extras.get("seasonNo");
        searchType = (SeasonSelectActivity.SearchType) extras.get("type");


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

        /*
        RelativeLayout layout = findViewById(R.id.rootLayout);

        switch (skin.rarity)
        {
            case "Uncommon":
                layout.setBackgroundResource(R.color.uncommonColor);
                break;
            case "Rare":
                layout.setBackgroundResource(R.color.rareColor);

                break;

            case "Epic":
                layout.setBackgroundResource(R.color.epicColor);

                break;

            case "Legendary":
                layout.setBackgroundResource(R.color.legendaryColor);

                break;
        }
        */


        nameText.setText(skin.name);
        descText.setText(skin.desc.toString());
        if(searchType == SeasonSelectActivity.SearchType.BP)
        {
            currencyIcon.setImageResource(R.drawable.ic_battlepass);
            costText.setText("");
        }
        else
        {
            currencyIcon.setImageResource(R.drawable.ic_vbucks);
            costText.setText(skin.cost);

        }

        Picasso.with(context).load(skin.getImageLinkSmall()).into(imageView);
        switch(skin.rarity)
        {
            case "Epic":
                imageView.setBackgroundResource(R.color.epicColor);
                break;
            case "Legendary":
                imageView.setBackgroundResource(R.color.legendaryColor);
                break;
            case "Rare":
                imageView.setBackgroundResource(R.color.rareColor);
                break;
            case "Uncommon":
                imageView.setBackgroundResource(R.color.uncommonColor);
                break;
        }

    }
}