package com.example.fraser.fndb;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class AdManager extends Activity
{
    private Activity activity;

    public AdManager(Activity activity)
    {
        this.activity = activity;
    }

    public void CreateAndLoadBanner()
    {
        AdView adView = activity.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView.loadAd(adRequest);
    }

    public void CreateAndLoadBannerMultiple(List<AdView> adsList)
    {
        for (AdView adView: adsList)
        {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            adView.loadAd(adRequest);
        }
    }

    public void RemoveAd()
    {
        AdView adView = activity.findViewById(R.id.adView);
        ((ViewGroup)adView.getParent()).removeView(adView);

    }
}
