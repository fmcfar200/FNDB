/*
    © 2018 Fraser McFarlane
 */

package com.mcfarlane.fraser.fndb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.net.URI;
import java.net.URISyntaxException;
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

        boolean adsBool;
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.LOCAL),Context.MODE_PRIVATE);
        adsBool = sharedPreferences.getBoolean(getString(R.string.SP_ADS),true);
        AdManager adManager = new AdManager(this, adsBool);
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
            List<Challenge> challengeArrayList = new ArrayList<>();

            try {
                String urlString = "https://fortnite-public-api.theapinetwork.com/prod09/challenges/get?season=current";
                URL url = new URL(urlString);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                URL endpoint = uri.toURL();


                HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();

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
                    int currentWeek = object.getInt("currentweek");
                    String rewardPictureURL = object.getString("star");

                    JSONObject challenges = object.getJSONObject("challenges");
                    JSONArray itemsArray = challenges.getJSONArray("week"+String.valueOf(currentWeek));
                    Log.d("Week OBJECT", " " + itemsArray.toString());

                    for(int i = 0; i < itemsArray.length();i++)
                    {
                        String name = null, questTotal = null, rewardAmount = null;
                        JSONObject theChallenge = itemsArray.getJSONObject(i);
                        name = theChallenge.getString("challenge");
                        questTotal = String.valueOf(theChallenge.getInt("total"));
                        rewardAmount = String.valueOf(theChallenge.getInt("stars"));


                        Challenge challenge = new Challenge(name,questTotal,rewardAmount);
                        challenge.setRewardImageURL(rewardPictureURL);
                        Log.e("TAG", "onPostExecute: " + challenge.getRewardAmount());

                        challengeArrayList.add(challenge);
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
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return challengeArrayList;
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
