
/*
    © 2018 Fraser McFarlane
 */

package com.mcfarlane.fraser.fndb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mcfarlane.fraser.fndb.util.IabHelper;
import com.mcfarlane.fraser.fndb.util.IabResult;



public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Button supportButton, removeAdsButton, rateButton;

    IabHelper iabHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("About");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        supportButton = findViewById(R.id.supportButton);
        removeAdsButton = findViewById(R.id.removeAdsButton);
        rateButton = findViewById(R.id.rateUsButton);
        supportButton.setOnClickListener(this);
        removeAdsButton.setOnClickListener(this);
        rateButton.setOnClickListener(this);


        iabHelper = new IabHelper(this,getString(R.string.BASE64KEY));
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if(!result.isSuccess())
                {
                    Log.e("TAG", "Problem with billing");
                }
                else
                {
                    Log.e("TAG", "SETUP DONE");
                }

            }
        });


    }

    @Override
    public void onClick(View v)
    {
        if (v == supportButton){StartSupportIntent();}
        if(v == removeAdsButton){
            StartRemoveAds();
        }
        if(v == rateButton){StartRateIntent();}

    }

    private void StartRateIntent()
    {

    }

    private void StartRemoveAds()
    {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(iabHelper != null)
        {
            try {
                iabHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
        iabHelper = null;
    }


}
