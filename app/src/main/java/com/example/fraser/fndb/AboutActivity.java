
/*
    © 2018 Fraser McFarlane
 */

package com.example.fraser.fndb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    Button supportButton, removeAdsButton, rateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        supportButton = findViewById(R.id.supportButton);
        removeAdsButton = findViewById(R.id.removeAdsButton);
        rateButton = findViewById(R.id.rateUsButton);
        supportButton.setOnClickListener(this);
        removeAdsButton.setOnClickListener(this);
        rateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == supportButton){StartSupportIntent();}
        if(v == removeAdsButton){StartRemoveAds();}
        if(v == rateButton){StartRateIntent();}

    }

    private void StartRateIntent() {
    }

    private void StartRemoveAds() {
    }

    private void StartSupportIntent()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "ultimatecompanion.help@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Ultimate Companion Support");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(intent, ""));
    }
}
