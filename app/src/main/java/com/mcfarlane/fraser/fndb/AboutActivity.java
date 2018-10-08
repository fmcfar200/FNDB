
/*
    © 2018 Fraser McFarlane
 */

package com.mcfarlane.fraser.fndb;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.TransactionDetails;



public class AboutActivity extends AppCompatActivity implements View.OnClickListener, BillingProcessor.IBillingHandler {

    Toolbar toolbar;
    Button supportButton, removeAdsButton, rateButton;

    BillingProcessor bp;

    boolean adsExtra;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            adsExtra = extras.getBoolean("ads");
        }

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("About");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bp.consumePurchase(getString(R.string.testProductID));
                    finish();
                }
            });
        }


        supportButton = findViewById(R.id.supportButton);
        removeAdsButton = findViewById(R.id.removeAdsButton);
        rateButton = findViewById(R.id.rateUsButton);
        supportButton.setOnClickListener(this);
        removeAdsButton.setOnClickListener(this);
        rateButton.setOnClickListener(this);

        bp = new BillingProcessor(this, getString(R.string.BASE64KEY), this);
        bp.initialize();



        PurchaseInfo purchaseInfo = null;
        boolean loadedPurchases = bp.loadOwnedPurchasesFromGoogle();
        TransactionDetails details = bp.getPurchaseTransactionDetails(getString(R.string.testProductID));

        if (details != null)
        {
            Log.e("TransDetails", "onCreate: " + details.toString());

        }
        else
        {
            Log.e("TransDetails", "onCreate: " + "No Details");

        }


        if (adsExtra)
        {
            StartRemoveAds();
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == supportButton){StartSupportIntent();}
        if(v == removeAdsButton)
        {
            StartRemoveAds();
        }
        if(v == rateButton){StartRateIntent();}

    }

    private void StartRateIntent()
    {

    }

    private void StartRemoveAds()
    {
        Log.e("Ads", "StartRemoveAds: " + "Start Ads Remove" );
        bp.purchase(this,getString(R.string.testProductID));

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
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!bp.handleActivityResult(requestCode,resultCode,data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details)
    {

        Intent mStartActivity = new Intent(getApplicationContext(), SplashActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error)
    {
        finish();

    }

    @Override
    public void onBillingInitialized()
    {
        Log.e("HELLO", "onBillingInitialized: " + "INITIALISED" );

    }


}
