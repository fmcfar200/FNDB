package com.example.fraser.fndb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class PlayerStatsActivity extends AppCompatActivity {

    ViewFlipper vFlipper;
    Toolbar toolbar;

    RadioGroup rGroup;

    EditText inputText;
    View winsBox, killsBox, matchesBox;
    TextView winBoxTitle, killBoxTitle, matchesBoxTitle;

    ListView lifetimeStatsList;
    StatsListAdapter lifeTimeStatsAdapter;

    ProgressDialog dialog;

    String platform = "pc";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);


        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        vFlipper = findViewById(R.id.statsFlipper);
        vFlipper.setDisplayedChild(0);

        rGroup = findViewById(R.id.radioGroup);
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.pcPlatformButton:
                        platform = "pc";
                        break;
                    case R.id.xboxPlatformButton:
                        platform = "xbox";
                        break;
                    case R.id.psnPlatformButton:
                        platform = "psn";
                        break;
                }
            }
        });

        inputText = findViewById(R.id.usernameEdit);
        inputText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    hideKeyboard(PlayerStatsActivity.this);
                    PlayerFetch fetch = new PlayerFetch();
                    fetch.execute();
                    return true;
                }
                return false;
            }
        });

        winsBox = findViewById(R.id.totalWinsBox);
        killsBox = findViewById(R.id.totalKillsBox);
        matchesBox = findViewById(R.id.totalMatchesBox);

        winBoxTitle = winsBox.findViewById(R.id.dataTitle);
        winBoxTitle.setText("Wins");

        killBoxTitle = killsBox.findViewById(R.id.dataTitle);
        killBoxTitle.setText("Kills");

        matchesBoxTitle = matchesBox.findViewById(R.id.dataTitle);
        matchesBoxTitle.setText("Matches Played");

        lifetimeStatsList = findViewById(R.id.lifetimeStatsListView);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Player Stats");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (vFlipper.getCurrentView() == vFlipper.getChildAt(0)) {
                        finish();
                    }
                    else {
                        vFlipper.setDisplayedChild(0);
                    }

                }
            });
        }
    }

    class PlayerFetch extends AsyncTask<Void, Void, Player>
    {
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(PlayerStatsActivity.this);
            dialog.setMessage("Getting " + inputText.getText().toString() + "'s Stats...");
            dialog.show();
        }

        @Override
        protected Player doInBackground(Void... voids) {


            try
            {
                URL statsEndPoint = new URL("https://api.fortnitetracker.com/v1/profile/" + platform + "/" + inputText.getText().toString());
                HttpURLConnection connection = (HttpURLConnection) statsEndPoint.openConnection();
                connection.setRequestProperty("TRN-Api-Key", getString(R.string.TRACKER_API_KEY));

                if (connection.getResponseCode() == 200) {

                    InputStream responseBody = connection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                    StringBuilder jsonString = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        jsonString.append(line).append('\n');

                    }

                    String name = "";
                    int wins = 0;
                    int kills = 0;
                    int matches = 0;
                    HashMap<String, String> lifetimeStatsMap = new HashMap<>();

                    JSONObject rootObject = new JSONObject(jsonString.toString());
                    name = rootObject.get("epicUserHandle").toString();

                    //LIFETIME OBJECTS
                    JSONArray lifeTimeStatsArray = rootObject.getJSONArray("lifeTimeStats");
                    for (int i = 0; i < lifeTimeStatsArray.length(); i++)
                    {
                        JSONObject lifeTimeObject = lifeTimeStatsArray.getJSONObject(i);
                        if (lifeTimeObject.get("key").toString().equals("Wins"))
                        {
                            wins = Integer.valueOf(lifeTimeObject.get("value").toString());
                        }
                        if (lifeTimeObject.get("key").toString().equals("Kills"))
                        {
                            kills = Integer.valueOf(lifeTimeObject.get("value").toString());
                        }
                        if (lifeTimeObject.get("key").toString().equals("Matches Played"))
                        {
                            matches = Integer.valueOf(lifeTimeObject.get("value").toString());
                        }

                        lifetimeStatsMap.put(lifeTimeObject.get("key").toString(),lifeTimeObject.get("value").toString());
                    }

                    lifetimeStatsMap.remove("Wins");
                    lifetimeStatsMap.remove("Kills");
                    lifetimeStatsMap.remove("Matches Played");


                    Player player = new Player();
                    player.setName(name);
                    player.lifetime.setTotalKills(kills);
                    player.lifetime.setTotalWins(wins);
                    player.lifetime.setTotalMatches(matches);
                    player.lifetime.setLifetimeStatsMap(lifetimeStatsMap);
                    return player;

                }
                else
                {
                    cancel(true);
                    Log.e("Error Tag", "Not Connected");

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }



        @Override
        protected void onPostExecute(Player player) {
            //Log.e("String Tag", " " + jsonString.toString());

            if (player != null)
            {
                toolbar.setTitle(player.getName());

                TextView winsText = winsBox.findViewById(R.id.dataText);
                winsText.setText(String.valueOf(player.lifetime.getTotalWins()));

                TextView killsText = killsBox.findViewById(R.id.dataText);
                killsText.setText(String.valueOf(player.lifetime.getTotalKills()));

                TextView matchesText = matchesBox.findViewById(R.id.dataText);
                matchesText.setText(String.valueOf(player.lifetime.getTotalMatches()));

                lifeTimeStatsAdapter = new StatsListAdapter(getApplicationContext(),R.layout.stats_list_item, player.lifetime.getLifetimeStatsMap());
                lifetimeStatsList.setAdapter(lifeTimeStatsAdapter);

                vFlipper.setDisplayedChild(1);
                dialog.dismiss();


            }
            else
            {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"No Player Found", Toast.LENGTH_SHORT).show();
            }



        }
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
