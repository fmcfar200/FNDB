/*
    © 2018 Fraser McFarlane
 */

package com.example.fraser.fndb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

public class ChallengesActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView challengeListView;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        AdManager adManager = new AdManager(this);
        adManager.CreateAndLoadBanner();

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Challenges");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        challengeListView = findViewById(R.id.challengeListView);

        ChallengeFetch fetch = new ChallengeFetch();
        fetch.execute();

    }

    class ChallengeFetch extends AsyncTask<Void,Void,List<Challenge>>
    {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ChallengesActivity.this);
            dialog.setMessage("Getting Challenges...");
            dialog.show();
        }

        @Override
        protected List<Challenge> doInBackground(Void... voids) {
            List<Challenge> challenges = new ArrayList<>();

            try {
                URL endpoint = new URL("https://api.fortnitetracker.com/v1/challenges");
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

                    JSONObject object = new JSONObject(jsonString.toString());
                    JSONArray itemsArray = object.getJSONArray("items");
                    for(int i = 0; i < itemsArray.length();i++)
                    {
                        JSONObject itemObject = itemsArray.getJSONObject(i);
                        JSONArray metadataArray = itemObject.getJSONArray("metadata");
                        String name = null, questTotal = null, rewardAmount = null, rewardPictureURL = null;
                        for (int j = 0; j < metadataArray.length(); j++)
                        {
                            JSONObject KVObject = metadataArray.getJSONObject(j);
                            String key = KVObject.get("key").toString();
                            if(key.equals("name"))
                            {
                                name = KVObject.getString("value");
                            }
                            if(key.equals("questsTotal"))
                            {
                                questTotal = KVObject.getString("value");
                            }
                            if(key.equals("rewardPictureUrl"))
                            {
                                rewardPictureURL = KVObject.getString("value");
                            }
                            if(key.equals("rewardName"))
                            {
                                rewardAmount = KVObject.getString("value");
                            }
                        }

                        Challenge challenge = new Challenge(name,questTotal,rewardAmount);
                        challenge.setRewardImageURL(rewardPictureURL);
                        Log.e("TAG", "onPostExecute: " + challenge.getRewardAmount());

                        challenges.add(challenge);
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

            return challenges;
        }

        @Override
        protected void onPostExecute(List<Challenge> challenges)
        {
            ChallengeAdapter adapter = new ChallengeAdapter(getApplicationContext(),R.layout.challenge_list_item,challenges);
            challengeListView.setAdapter(adapter);

            dialog.dismiss();
        }
    }
}
