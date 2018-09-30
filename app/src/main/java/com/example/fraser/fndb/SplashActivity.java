/*
    © 2018 Fraser McFarlane
 */

package com.example.fraser.fndb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this,getString(R.string.ADS_ID));
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
        finish();
    }
}
