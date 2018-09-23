package com.example.fraser.fndb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PlayerStatsActivity extends AppCompatActivity {

    ViewFlipper vFlipper;
    ViewFlipper modeFlipper;
    Toolbar toolbar;

    RadioGroup platformGroup;
    RadioGroup modeGroup;
    EditText inputText;
    Button submitButton;

    View lifetimeLayout, soloLayout, duoLayout, squadLayout;


    ListView lifetimeStatsList, soloStatsList, duoStatsList, squadStatsList;
    StatsListAdapter lifeTimeStatsAdapter, soloStatsAdapter, duoStatsAdapter, squadStatsAdapter;

    ProgressDialog dialog;

    String platform = "pc";

    HashMap<String,String> soloStatsMap = new HashMap<>();
    HashMap<String,String> dueStatsMap = new HashMap<>();
    HashMap<String,String> squadStatsMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);


        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);

        vFlipper = findViewById(R.id.statsFlipper);
        vFlipper.setDisplayedChild(0);
        modeFlipper = findViewById(R.id.modeFlipper);
        modeFlipper.setDisplayedChild(0);

        lifetimeLayout = findViewById(R.id.lifetimeLayout);
        lifetimeStatsList = lifetimeLayout.findViewById(R.id.statsListView);

        soloLayout = findViewById(R.id.soloLayout);
        soloStatsList = soloLayout.findViewById(R.id.statsListView);

        duoLayout = findViewById(R.id.duoLayout);
        duoStatsList = duoLayout.findViewById(R.id.statsListView);

        squadLayout = findViewById(R.id.squadLayout);
        squadStatsList = squadLayout.findViewById(R.id.statsListView);

        platformGroup = findViewById(R.id.radioGroup);
        platformGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        modeGroup = findViewById(R.id.modeRadioGroup);
        modeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.lifetimeButton:
                        modeFlipper.setDisplayedChild(0);
                        break;
                    case R.id.soloButton:
                        modeFlipper.setDisplayedChild(1);
                        break;
                    case R.id.duoButton:
                        modeFlipper.setDisplayedChild(2);
                        break;
                    case R.id.squadButton:
                        modeFlipper.setDisplayedChild(3);
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
                    GetPlayerStats();
                    return true;
                }
                return false;
            }
        });

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == submitButton && !inputText.getText().equals("") || inputText.length() > 0)
                {
                    GetPlayerStats();
                }
            }
        });




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

    private void GetPlayerStats()
    {
        hideKeyboard(PlayerStatsActivity.this);
        PlayerFetch fetch = new PlayerFetch();
        fetch.execute();
    }

    private void saveSearch(String key, Object object)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        editor.putString(key,json);
        editor.apply();

    }

    public HashMap<String,String> getSavedHashMap(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = prefs.getString(key,"");
        java.lang.reflect.Type type = new TypeToken<HashMap<String,String>>(){}.getType();
        HashMap<String,String> obj = gson.fromJson(json, type);
        return obj;
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

                    Player player = new Player();
                    String name = "";
                    int wins = 0;
                    int kills = 0;
                    int matches = 0;
                    HashMap<String, String> lifetimeStatsMap = new HashMap<>();

                    JSONObject rootObject = new JSONObject(jsonString.toString());
                    name = rootObject.get("epicUserHandle").toString();

                    //LIFETIME OBJECTS
                    JSONArray lifeTimeStatsArray = rootObject.getJSONArray("lifeTimeStats");
                    for (int i = 0; i < lifeTimeStatsArray.length(); i++) {
                        JSONObject lifeTimeObject = lifeTimeStatsArray.getJSONObject(i);
                        if (lifeTimeObject.get("key").toString().equals("Wins")) {
                            wins = Integer.valueOf(lifeTimeObject.get("value").toString());
                        }
                        if (lifeTimeObject.get("key").toString().equals("Kills")) {
                            kills = Integer.valueOf(lifeTimeObject.get("value").toString());
                        }
                        if (lifeTimeObject.get("key").toString().equals("Matches Played")) {
                            matches = Integer.valueOf(lifeTimeObject.get("value").toString());
                        }

                        lifetimeStatsMap.put(lifeTimeObject.get("key").toString(), lifeTimeObject.get("value").toString());
                    }


                    lifetimeStatsMap.remove("Wins");
                    lifetimeStatsMap.remove("Kills");
                    lifetimeStatsMap.remove("Matches Played");

                    player.setName(name);
                    player.lifetime.setTotalKills(kills);
                    player.lifetime.setTotalWins(wins);
                    player.lifetime.setTotalMatches(matches);
                    player.lifetime.setLifetimeStatsMap(lifetimeStatsMap);

                    //SOLO PARSE
                    soloStatsMap = parseModeStats("p2",rootObject);
                    player.solo.setSoloStatsMap(soloStatsMap);

                    //DUO PARSE
                    dueStatsMap = parseModeStats("p10", rootObject);
                    player.duo.setDuoStatsMap(dueStatsMap);

                    //SQUAD PARSE
                    squadStatsMap = parseModeStats("p9",rootObject);
                    player.squad.setSquadStatsMap(squadStatsMap);

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

                //LIFETIME SCREEN
                View lifetimeWinBox = lifetimeLayout.findViewById(R.id.totalWinsBox);
                TextView winsText = lifetimeWinBox.findViewById(R.id.dataText);
                winsText.setText(String.valueOf(player.lifetime.getTotalWins()));
                TextView winsTitle = lifetimeWinBox.findViewById(R.id.dataTitle);
                winsTitle.setText("Wins");

                View lifetimeKillbox = lifetimeLayout.findViewById(R.id.totalKillsBox);
                TextView killsText = lifetimeKillbox.findViewById(R.id.dataText);
                killsText.setText(String.valueOf(player.lifetime.getTotalKills()));
                TextView killsTitle = lifetimeKillbox.findViewById(R.id.dataTitle);
                killsTitle.setText("Kills");

                View lifetimeMatchBox = lifetimeLayout.findViewById(R.id.totalMatchesBox);
                TextView matchesText = lifetimeMatchBox.findViewById(R.id.dataText);
                matchesText.setText(String.valueOf(player.lifetime.getTotalMatches()));
                TextView matchesTitle = lifetimeMatchBox.findViewById(R.id.dataTitle);
                matchesTitle.setText("Matches Played");

                lifeTimeStatsAdapter = new StatsListAdapter(getApplicationContext(),R.layout.stats_list_item, player.lifetime.getLifetimeStatsMap());
                lifetimeStatsList.setAdapter(lifeTimeStatsAdapter);

                //SOLO SCREEN
                View soloWinBox = soloLayout.findViewById(R.id.totalWinsBox);
                TextView soloWinsText = soloWinBox.findViewById(R.id.dataText);
                soloWinsText.setText(String.valueOf(player.solo.getSoloStatsMap().get("Wins")));
                TextView soloWinsTitle = soloWinBox.findViewById(R.id.dataTitle);
                soloWinsTitle.setText("Wins");

                View soloKillbox = soloLayout.findViewById(R.id.totalKillsBox);
                TextView soloKillsText = soloKillbox.findViewById(R.id.dataText);
                soloKillsText.setText(String.valueOf(player.solo.getSoloStatsMap().get("Kills")));
                TextView soloKillsTitle = soloKillbox.findViewById(R.id.dataTitle);
                soloKillsTitle.setText("Kills");

                View soloMatchBox = soloLayout.findViewById(R.id.totalMatchesBox);
                TextView soloMatchesText = soloMatchBox.findViewById(R.id.dataText);
                soloMatchesText.setText(String.valueOf(player.solo.getSoloStatsMap().get("Matches")));
                TextView soloMatchesTitle = soloMatchBox.findViewById(R.id.dataTitle);
                soloMatchesTitle.setText("Matches Played");


                soloStatsAdapter = new StatsListAdapter(getApplicationContext(),R.layout.stats_list_item, player.solo.getSoloStatsMap());
                soloStatsList.setAdapter(soloStatsAdapter);

                //DUO SCREEN
                View duoWinBox = duoLayout.findViewById(R.id.totalWinsBox);
                TextView duoWinsText = duoWinBox.findViewById(R.id.dataText);
                duoWinsText.setText(String.valueOf(player.duo.getDuoStatsMap().get("Wins")));
                TextView duoWinTitle = duoWinBox.findViewById(R.id.dataTitle);
                duoWinTitle.setText("Wins");

                View duoKillbox = duoLayout.findViewById(R.id.totalKillsBox);
                TextView duoKillsText = duoKillbox.findViewById(R.id.dataText);
                duoKillsText.setText(String.valueOf(player.duo.getDuoStatsMap().get("Kills")));
                TextView duoKillsTitle = duoKillbox.findViewById(R.id.dataTitle);
                duoKillsTitle.setText("Kills");

                View duoMatchBox = duoLayout.findViewById(R.id.totalMatchesBox);
                TextView duoMatchesText = duoMatchBox.findViewById(R.id.dataText);
                duoMatchesText.setText(String.valueOf(player.duo.getDuoStatsMap().get("Matches")));
                TextView duoMatchesTitle = duoMatchBox.findViewById(R.id.dataTitle);
                duoMatchesTitle.setText("Matches Played");


                duoStatsAdapter = new StatsListAdapter(getApplicationContext(),R.layout.stats_list_item, player.duo.getDuoStatsMap());
                duoStatsList.setAdapter(duoStatsAdapter);

                //Squad SCREEN
                View squadWinBox = squadLayout.findViewById(R.id.totalWinsBox);
                TextView squadWinsText = squadWinBox.findViewById(R.id.dataText);
                squadWinsText.setText(String.valueOf(player.squad.getSquadStatsMap().get("Wins")));
                TextView squadWinTitle = squadWinBox.findViewById(R.id.dataTitle);
                squadWinTitle.setText("Wins");

                View squadKillbox = squadLayout.findViewById(R.id.totalKillsBox);
                TextView squadKillsText = squadKillbox.findViewById(R.id.dataText);
                squadKillsText.setText(String.valueOf(player.squad.getSquadStatsMap().get("Kills")));
                TextView squadKillsTitle = squadKillbox.findViewById(R.id.dataTitle);
                squadKillsTitle.setText("Kills");

                View squadMatchBox = squadLayout.findViewById(R.id.totalMatchesBox);
                TextView squadMatchesText = squadMatchBox.findViewById(R.id.dataText);
                squadMatchesText.setText(String.valueOf(player.squad.getSquadStatsMap().get("Matches")));
                TextView squadMatchesTitle = squadMatchBox.findViewById(R.id.dataTitle);
                squadMatchesTitle.setText("Matches Played");


                squadStatsAdapter = new StatsListAdapter(getApplicationContext(),R.layout.stats_list_item, player.squad.getSquadStatsMap());
                squadStatsList.setAdapter(squadStatsAdapter);


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

    HashMap<String, String> parseModeStats(String type, JSONObject root) throws JSONException {

        HashMap<String,String> thereturnMap = new HashMap<>();
        JSONObject soloStatsObject = root.getJSONObject("stats").getJSONObject(type);

        for (Iterator<String> iterator = soloStatsObject.keys(); iterator.hasNext();)
        {
            String theKey = null;
            String theVal = null;
            String key =  iterator.next();
            Object value = soloStatsObject.get(key);
            JSONObject jsonValue = null;
            if (value instanceof JSONObject)
            {
                jsonValue = (JSONObject) value;
                for (Iterator<String> it = jsonValue.keys(); it.hasNext(); )
                {
                    String attKey = it.next();
                    String val = jsonValue.get(attKey).toString();
                    if (attKey.equals("label"))
                    {
                        theKey = val;
                        for (Iterator<String> i = jsonValue.keys(); i.hasNext();)
                        {
                            String attKey2 = i.next();
                            String val2 = jsonValue.get(attKey2).toString();
                            if (attKey2.equals("value"))
                            {
                                theVal = val2;
                            }
                        }
                    }

                }

            }

            if (theKey != null && theVal != null)
            {
                thereturnMap.put(theKey,theVal);
            }
        }

        return thereturnMap;

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
