package com.example.fraser.fndb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

public class WeaponsActivity extends AppCompatActivity
{
    Toolbar toolbar;
    ProgressDialog dialog;

    GridView weaponsGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapons);

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Weapons");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        weaponsGridView = findViewById(R.id.weaponsGrid);

        WeaponsFetch fetch = new WeaponsFetch();
        fetch.execute();
    }

    private class WeaponsFetch extends AsyncTask<Void, Void, List<Weapon>> {

        @Override
        protected void onPreExecute()
        {
            dialog = new ProgressDialog(WeaponsActivity.this);
            dialog.setMessage("Getting Armory...");
            dialog.show();
        }

        @Override
        protected List<Weapon> doInBackground(Void... voids)
        {
            List<Weapon> weaponsList = new ArrayList<>();

            try {
                URL endpoint = new URL("https://fortnite-public-api.theapinetwork.com/prod09/weapons/get");
                HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", getString(R.string.FORTNITE_API_KEY));

                if (connection.getResponseCode() == 200)
                {

                    InputStream responseBody = connection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                    StringBuilder jsonString = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        jsonString.append(line).append('\n');
                    }
                    Log.e("TEST SUCCESS", jsonString.toString());

                    JSONObject rootObject = new JSONObject(String.valueOf(jsonString));
                    JSONArray weaponsArray = (JSONArray) rootObject.get("weapons");

                    for(int i = 0; i < weaponsArray.length(); i++)
                    {
                        JSONObject object = weaponsArray.getJSONObject(i);
                        String name = String.valueOf(object.get("name"));
                        String rarity = String.valueOf(object.get("rarity"));

                        JSONObject imageObject = object.getJSONObject("images");
                        String imageURL = (String) imageObject.get("image");
                        String backgroundURL = (String) imageObject.get("background");

                        JSONObject statsObject = object.getJSONObject("stats");
                        double dps = Double.valueOf((String) statsObject.get("dps"));
                        double firerate = Double.valueOf(statsObject.get("firerate").toString());
                        int ammoCost = Integer.valueOf(statsObject.get("ammocost").toString());


                        JSONObject damageObject = statsObject.getJSONObject("damage");
                        int body = Integer.valueOf(damageObject.get("body").toString());
                        double head = Double.valueOf(damageObject.get("head").toString());
                        Damage damage = new Damage(body,head);

                        JSONObject magazineObject = statsObject.getJSONObject("magazine");
                        double reload = Double.valueOf(magazineObject.get("reload").toString());
                        int size = Integer.valueOf(magazineObject.get("size").toString());
                        Magazine magazine = new Magazine(reload,size);

                        WeaponStats stats = new WeaponStats(damage,dps,firerate,magazine,ammoCost);

                        Weapon weapon = new Weapon(name,rarity,imageURL,backgroundURL,stats);
                        weaponsList.add(weapon);

                    }
                }
                else
                {
                    Log.e("TEST FAIL", "FAIL");

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return weaponsList;
        }

        @Override
        protected void onPostExecute(List<Weapon> weapons)
        {
            WeaponsAdapter adapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,weapons);
            weaponsGridView.setAdapter(adapter);

            dialog.dismiss();
        }
    }
}
