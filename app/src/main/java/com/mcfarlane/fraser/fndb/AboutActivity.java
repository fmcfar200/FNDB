
/*
    © 2018 Fraser McFarlane
 */

package com.mcfarlane.fraser.fndb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.TransactionDetails;



public class AboutActivity extends AppCompatActivity implements View.OnClickListener, BillingProcessor.IBillingHandler {

    Toolbar toolbar;
    TextView versionText;
    Button supportButton, removeAdsButton, rateButton, privacyButton;

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
                    finish();
                }
            });
        }

        versionText = findViewById(R.id.versionText);
        supportButton = findViewById(R.id.supportButton);
        removeAdsButton = findViewById(R.id.removeAdsButton);
        privacyButton = findViewById(R.id.privacyPolicyButton);
        rateButton = findViewById(R.id.rateUsButton);
        supportButton.setOnClickListener(this);
        removeAdsButton.setOnClickListener(this);
        rateButton.setOnClickListener(this);
        privacyButton.setOnClickListener(this);

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

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int versionCode = pInfo.versionCode;
            versionText.setText("Version: " + version + "(" + versionCode + ")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == supportButton){StartSupportIntent();}
        if(v == removeAdsButton){ StartRemoveAds(); }
        if(v == rateButton){StartRateIntent();}
        if (v == privacyButton){OpenPrivacyPolicy();}

    }

    private void OpenPrivacyPolicy()
    {
        Uri uri = Uri.parse("https://sites.google.com/view/ultimate-fortnite-companion/privacy-policy");
        Intent goToURL = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(goToURL);
    }

    private void StartRateIntent()
    {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
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
