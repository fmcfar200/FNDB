/*
    © 2018 Fraser McFarlane
 */

package com.mcfarlane.fraser.fndb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.MobileAds;

public class SplashActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler{

    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.LOCAL),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.SP_ADS),true);
        editor.commit();

        bp = new BillingProcessor(this, getString(R.string.BASE64KEY), this);
        bp.initialize();

        PurchaseInfo purchaseInfo = null;
        boolean loadedPurchases = bp.loadOwnedPurchasesFromGoogle();
        TransactionDetails details = bp.getPurchaseTransactionDetails(getString(R.string.testProductID));

        if (details != null)
        {
            Log.e("TransDetails", "onCreate: " + details.toString());
            editor.putBoolean(getString(R.string.SP_ADS),false);
            editor.commit();
        }

        MobileAds.initialize(this,getString(R.string.ADS_ID));
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
