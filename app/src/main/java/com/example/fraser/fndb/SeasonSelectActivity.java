package com.example.fraser.fndb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

public class SeasonSelectActivity extends AppCompatActivity implements View.OnClickListener
{


    private Button s1Button, s2Button, s3Button, s4Button, s5Button,
    itemShopButton,miscButton,uncommonButton, rareButton, epicButton, legendaryButton;

    private ViewFlipper layoutFlipper;
    private Toolbar toolbar;

    private int season = 1;

    public enum SearchType
    {
        BP,UNCOMMON, RARE, EPIC, LEGENDARY
    }
    private SearchType searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Battle Pass Skins");

        layoutFlipper = findViewById(R.id.viewFlipper);
        s1Button = findViewById(R.id.s1Skins);
        s2Button = findViewById(R.id.s2Skins);
        s3Button = findViewById(R.id.s3Skins);
        s4Button = findViewById(R.id.s4Skins);
        s5Button = findViewById(R.id.s5Skins);
        itemShopButton = findViewById(R.id.shopSkins);
        miscButton = findViewById(R.id.miscSkins);

        uncommonButton = findViewById(R.id.uncommonSkins);
        rareButton = findViewById(R.id.rareSkins);
        epicButton = findViewById(R.id.epicSkins);
        legendaryButton = findViewById(R.id.legendarySkins);

        s1Button.setOnClickListener(this);
        s2Button.setOnClickListener(this);
        s3Button.setOnClickListener(this);
        s4Button.setOnClickListener(this);
        s5Button.setOnClickListener(this);
        itemShopButton.setOnClickListener(this);
        miscButton.setOnClickListener(this);

        uncommonButton.setOnClickListener(this);
        rareButton.setOnClickListener(this);
        epicButton.setOnClickListener(this);
        legendaryButton.setOnClickListener(this);


        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Skins");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layoutFlipper.getCurrentView() == layoutFlipper.getChildAt(0)) {
                        finish();
                    }
                    else {
                        layoutFlipper.setDisplayedChild(0);
                    }
                }
            });
        }
    }


    @Override
    public void onClick(View v)
    {
        searchType = SearchType.BP;
        if(v==s1Button) {
            season = 1;
            StartListActivity(season, searchType);
        }
        else if (v == s2Button) {
            season = 2;
            StartListActivity(season, searchType);
        }
        else if (v == s3Button) {
            season = 3;
            StartListActivity(season, searchType);
        }
        else if (v == s4Button) {
            season = 4;
            StartListActivity(season,searchType);
        }
        else if (v == s5Button) {
            season = 5;
            StartListActivity(season,searchType);
        }
        else if (v == itemShopButton){
            layoutFlipper.setDisplayedChild(1);
        }
        else if(v == uncommonButton)
        {
            season = 0;
            searchType = SearchType.UNCOMMON;
            StartListActivity(season,searchType);
        }

    }

    private void StartListActivity(int seasonNo, SearchType type)
    {
        Intent intent = new Intent(getApplicationContext(),SkinActivity.class);
        intent.putExtra("seasonNo", seasonNo);
        intent.putExtra("type", type);
        startActivity(intent);
    }

}
