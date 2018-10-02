
/*
    © 2018 Fraser McFarlane
 */
package com.mcfarlane.fraser.fndb;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

public class AdManager extends Activity
{
    private Activity activity;
    private boolean adsBool;

    public AdManager(Activity activity, boolean adsBool)
    {
        this.activity = activity;
        this.adsBool = adsBool;

    }

    public void CreateAndLoadBanner()
    {
        if (adsBool)
        {
            AdView adView = activity.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            adView.loadAd(adRequest);
        }
        else
        {
           RemoveAd();
        }

    }

    public void CreateAndLoadBannerMultiple(List<AdView> adsList)
    {
        if(adsBool)
        {
            for (AdView adView: adsList)
            {
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

                adView.loadAd(adRequest);
            }
        }

    }

    public void RemoveAd()
    {
        AdView adView = activity.findViewById(R.id.adView);
        ((ViewGroup)adView.getParent()).removeView(adView);

    }


}
