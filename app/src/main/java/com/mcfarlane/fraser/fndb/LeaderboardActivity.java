/*
    © 2018 Fraser McFarlane
 */
package com.mcfarlane.fraser.fndb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

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
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressDialog dialog;

    RadioGroup radioGroup;
    TextView headingText;
    ListView leaderboardList;

    String[] leaderboardTypes = {"kills","wins","score","minutes"};
    List<LeaderboardItem> killsList = new ArrayList<>();
    List<LeaderboardItem> winsList = new ArrayList<>();
    List<LeaderboardItem> scoreList = new ArrayList<>();
    List<LeaderboardItem> minutesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // TEST ID ca-app-pub-3940256099942544/1033173712

        boolean adsBool;
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.LOCAL),Context.MODE_PRIVATE);
        adsBool = sharedPreferences.getBoolean(getString(R.string.SP_ADS),true);

        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        if (adsBool)
        {
            mInterstitialAd.setAdUnitId(getString(R.string.LEADERBOARD_UNIT_ID));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }


        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                finish();
            }
        });

        leaderboardList = findViewById(R.id.top10ListView);

        headingText = findViewById(R.id.leaderboardHeading);
        radioGroup = findViewById(R.id.leaderboardRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.leaderboardKillsButton:
                        headingText.setText("Highest Kills");
                        LeaderboardAdapter killsAdapter = new LeaderboardAdapter(getApplicationContext(),R.layout.stats_list_item,killsList);
                        leaderboardList.setAdapter(killsAdapter);
                        break;
                    case R.id.leaderboardWinsButton:
                        headingText.setText("Highest Wins");

                        LeaderboardAdapter winsAdapter = new LeaderboardAdapter(getApplicationContext(),R.layout.stats_list_item,winsList);
                        leaderboardList.setAdapter(winsAdapter);
                        break;
                    case R.id.leaderboardScoreButton:
                        headingText.setText("Highest Score");

                        LeaderboardAdapter scoreAdapter = new LeaderboardAdapter(getApplicationContext(),R.layout.stats_list_item,scoreList);
                        leaderboardList.setAdapter(scoreAdapter);

                        break;
                    case R.id.leaderboardHoursButton:
                        headingText.setText("Highest Minutes");

                        LeaderboardAdapter minutesAdapter = new LeaderboardAdapter(getApplicationContext(),R.layout.stats_list_item,minutesList);
                        leaderboardList.setAdapter(minutesAdapter);
                        break;
                }
            }
        });

        leaderboardList.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                LeaderboardItem item = (LeaderboardItem) adapter.getItemAtPosition(position);
                StartStatsActivity(item);
            }
        });

        LeaderboardFetch fetch = new LeaderboardFetch();
        fetch.execute();

    }

    private void StartStatsActivity(LeaderboardItem item)
    {
        Intent intent = new Intent(getApplicationContext(),PlayerStatsActivity.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    class LeaderboardFetch extends AsyncTask<Void,Void,List<LeaderboardItem>>
    {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(LeaderboardActivity.this);
            dialog.setMessage("Getting Leaderboard...");
            dialog.show();
        }

        @Override
        protected List<LeaderboardItem> doInBackground(Void... voids) {


            for(int i = 0; i < leaderboardTypes.length; i++)
            {
                try {
                    URL endpoint = new URL("https://fortnite-public-api.theapinetwork.com/prod09/leaderboards/get?window=top_10_" + leaderboardTypes[i]);
                    HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Authorization", "a4587bc8429ba5f7e2be4d869fddf5ff");



                    if (connection.getResponseCode() == 200)
                    {

                        InputStream responseBody = connection.getInputStream();
                        BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                        StringBuilder jsonString = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            jsonString.append(line).append('\n');
                        }

                        if(jsonString.toString().contains("numericErrorCode"))
                        {
                            jsonString = new StringBuilder();
                            jsonString.append(GetJSONBackup());
                        }

                        JSONObject root = new JSONObject(jsonString.toString());
                        JSONArray entries = root.getJSONArray("entries");
                        for(int j = 0; j < entries.length();j++)
                        {
                            JSONObject entry = entries.getJSONObject(j);
                            String username = entry.getString("username");
                            int rank = Integer.valueOf(entry.getString("rank"));
                            int value;

                            if(leaderboardTypes[i].equals("kills"))
                            {
                                value = Integer.valueOf(entry.getString("kills"));
                                LeaderboardItem item = new LeaderboardItem(username,rank,value);
                                item.setPlatform(entry.getString("platform"));
                                killsList.add(item);
                            }
                            else if(leaderboardTypes[i].equals("wins"))
                            {
                                value = Integer.valueOf(entry.getString("wins"));
                                LeaderboardItem item = new LeaderboardItem(username,rank,value);
                                item.setPlatform(entry.getString("platform"));
                                winsList.add(item);
                            }
                            else if(leaderboardTypes[i].equals("score"))
                            {
                                value = Integer.valueOf(entry.getString("score"));
                                LeaderboardItem item = new LeaderboardItem(username,rank,value);
                                item.setPlatform(entry.getString("platform"));
                                scoreList.add(item);
                            }
                            else if (leaderboardTypes[i].equals("minutes"))
                            {
                                value = Integer.valueOf(entry.getString("minutes"));
                                LeaderboardItem item = new LeaderboardItem(username,rank,value);
                                item.setPlatform(entry.getString("platform"));
                                minutesList.add(item);
                            }


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
            }


            return killsList;
        }

        @Override
        protected void onPostExecute(List<LeaderboardItem> leaderboardItems)
        {
            LeaderboardAdapter adapter = new LeaderboardAdapter(getApplicationContext(),R.layout.stats_list_item,leaderboardItems);
            leaderboardList.setAdapter(adapter);

            dialog.dismiss();
        }
    }

    public String GetJSONBackup()
    {
        String json = null;

        try
        {
            InputStream stream = getAssets().open("data/TempLeaderboardData.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return json;

    }
}
