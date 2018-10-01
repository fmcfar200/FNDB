
/*
    © 2018 Fraser McFarlane
 */

package com.example.fraser.fndb;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.Purchase.PurchasesResult;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener, PurchasesUpdatedListener {

    Toolbar toolbar;
    Button supportButton, removeAdsButton, rateButton;

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


        BillingClient mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                    Log.e("RESPONSE", "onBillingSetupFinished: " + "OK" );
                }
                else
                {
                    Log.e("RESPONSE", "onBillingSetupFinished: " + billingResponseCode);

                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });


    }

    @Override
    public void onClick(View v)
    {
        if (v == supportButton){StartSupportIntent();}
        if(v == removeAdsButton){StartRemoveAds();}
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
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

    }
}
