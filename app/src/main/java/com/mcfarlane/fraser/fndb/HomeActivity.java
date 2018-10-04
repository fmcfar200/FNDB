/*
    © 2018 Fraser McFarlane
 */

package com.mcfarlane.fraser.fndb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends BaseActivity implements View.OnClickListener{

    Button skinBtn;
    Button statsButton;
    Button newsButton;
    Button weaponButton;
    Button challengeButton;
    Button leaderboardButton;

    TextView timerTextView;

    ExpandableHeightGridView itemShopGrid;
    List<ShopItem> shopItems = new ArrayList<>();
    ShopGridAdapter adapter;
    ProgressDialog dialog;

    ShopFetch fetch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        super.onCreateDrawer();

        getSupportActionBar().setTitle("Home");

        boolean adsBool;
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.LOCAL),Context.MODE_PRIVATE);
        adsBool = sharedPreferences.getBoolean(getString(R.string.SP_ADS),true);

        AdManager adManager = new AdManager(this, adsBool);
        adManager.CreateAndLoadBanner();


        skinBtn = findViewById(R.id.skinButton);
        statsButton = findViewById(R.id.statsButton);
        newsButton = findViewById(R.id.newsButton);
        weaponButton = findViewById(R.id.weaponsButton);
        challengeButton = findViewById(R.id.challengesButton);
        leaderboardButton = findViewById(R.id.leaderboardButton);


        skinBtn.setOnClickListener(this);
        statsButton.setOnClickListener(this);
        newsButton.setOnClickListener(this);
        weaponButton.setOnClickListener(this);
        challengeButton.setOnClickListener(this);
        leaderboardButton.setOnClickListener(this);

        timerTextView = findViewById(R.id.shopTimerText);
        itemShopGrid = findViewById(R.id.itemShopGrid);
        itemShopGrid.setExpanded(true);

        fetch = new ShopFetch();
        fetch.execute();

    }


    @Override
    public void onClick(View v)
    {
        if (v ==skinBtn) { StartActivity(SeasonSelectActivity.class); }
        if (v == statsButton) { StartActivity(PlayerStatsActivity.class); }
        if (v == newsButton) { StartActivity(NewsActivity.class); }
        if (v == weaponButton) { StartActivity(WeaponsActivity.class); }
        if (v == challengeButton) { StartActivity(ChallengesActivity.class); }
        if (v == leaderboardButton) { StartActivity(LeaderboardActivity.class); }





    }

    void StartActivity(Class theclass)
    {
        Intent i = new Intent(getApplicationContext(), theclass);
        startActivity(i);
    }


    private void StartShopTimer()
    {
        int endHour = 1;
        int endMinute = 4;
        int endSecond = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
        Date currentDate = calendar.getTime();
        Date end = new Date();

        if (calendar.get(Calendar.HOUR_OF_DAY) >= 0 && calendar.get(Calendar.HOUR_OF_DAY) < endHour + 1)
        {
            if ( calendar.get(Calendar.HOUR_OF_DAY) == endHour && calendar.get(Calendar.MINUTE) >= 0 && calendar.get(Calendar.MINUTE) < endMinute)
            {
                calendar.set(Calendar.HOUR_OF_DAY, endHour);
                calendar.set(Calendar.MINUTE,endMinute);
                calendar.set(Calendar.SECOND, endSecond);
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
                end = calendar.getTime();

            }
            else
            {
                calendar.add(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, endHour);
                calendar.set(Calendar.MINUTE,endMinute);
                calendar.set(Calendar.SECOND, endSecond);
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
                end = calendar.getTime();
            }
        }
        else if (calendar.get(Calendar.HOUR_OF_DAY) == endHour && calendar.get(Calendar.MINUTE) == endMinute && calendar.get(Calendar.SECOND) >= endSecond)
        {
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, endHour);
            calendar.set(Calendar.MINUTE,endMinute);
            calendar.set(Calendar.SECOND, endSecond);
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
            end = calendar.getTime();
        }
        else
        {
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, endHour);
            calendar.set(Calendar.MINUTE,endMinute);
            calendar.set(Calendar.SECOND, endSecond);
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
            end = calendar.getTime();
        }


        long diffInMS = end.getTime() - currentDate.getTime();

        MyCount counter = new MyCount(diffInMS,1000);
        counter.start();

    }

    public class MyCount extends CountDownTimer
    {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            long millis = millisUntilFinished;
            long diffInHours = TimeUnit.MILLISECONDS.toHours(millis);
            millis -= TimeUnit.HOURS.toMillis(diffInHours);

            long diffInMins = TimeUnit.MILLISECONDS.toMinutes(millis);
            millis -= TimeUnit.MINUTES.toMillis(diffInMins);

            long diffInSecs = TimeUnit.MILLISECONDS.toSeconds(millis);
            //millis -= TimeUnit.SECONDS.toMillis(diffInSecs);


            timerTextView.setText(String.valueOf(diffInHours) + ":" +
            String.valueOf(diffInMins) + ":" + String.valueOf(diffInSecs));
        }

        @Override
        public void onFinish()
        {
            ShopFetch fetch = new ShopFetch();
            fetch.execute();
        }
    }


    class ShopFetch extends AsyncTask<Void, Void, List<ShopItem>>{


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(HomeActivity.this);
            dialog.setMessage("Getting Shop Items...");
            dialog.show();
        }

        @Override
        protected List<ShopItem> doInBackground(Void... voids)
        {
            shopItems = new ArrayList<>();

            try {
                URL endpoint = new URL("https://api.fortnitetracker.com/v1/store");
                HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
                connection.setRequestProperty("TRN-Api-Key", getString(R.string.TRACKER_API_KEY));

                if (connection.getResponseCode() == 200)
                {

                    InputStream responseBody = connection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                    StringBuilder jsonString = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        jsonString.append(line).append('\n');
                    }

                    JSONArray array = new JSONArray(jsonString.toString());
                    for (int i = 0; i < array.length(); i ++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        String imageURL = object.get("imageUrl").toString();
                        String name = object.get("name").toString();
                        String rarity = object.get("rarity").toString();
                        String storeCategory = object.get("storeCategory").toString();
                        String vBucks = object.get("vBucks").toString();
                        ShopItem item = new ShopItem(imageURL,name,rarity,storeCategory,vBucks);
                        shopItems.add(item);


                    }
                }
                else
                {
                    Log.e("Response", "Error");

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*
                TEST CODE
            */

            return shopItems;

        }


        @Override
        protected void onPostExecute(List<ShopItem> shopItems) {
            adapter = new ShopGridAdapter(getApplicationContext(),R.layout.grid_shoplist_item,shopItems);
            itemShopGrid.setAdapter(adapter);
            dialog.dismiss();
            StartShopTimer();
        }
    }
}
