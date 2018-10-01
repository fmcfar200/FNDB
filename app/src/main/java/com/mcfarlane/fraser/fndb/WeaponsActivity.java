/*
    © 2018 Fraser McFarlane
 */
package com.mcfarlane.fraser.fndb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.TextView;

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
import java.util.List;

public class WeaponsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    Toolbar toolbar;
    ProgressDialog dialog;

    ExpandableHeightGridView assaultGridView, heavyGridView, shotgunGrid,
    smgGrid, pistolGrid, sniperGrid, launcherGrid, otherGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapons);

        List<AdView> adsList = new ArrayList<>();
        AdView adView = findViewById(R.id.adView);
        AdView adView1 = findViewById(R.id.adView1);
        AdView adView2 = findViewById(R.id.adView2);
        adsList.add(adView);
        adsList.add(adView1);
        adsList.add(adView2);

        AdManager adManager = new AdManager(this);
        adManager.CreateAndLoadBannerMultiple(adsList);

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

        assaultGridView = findViewById(R.id.assaultGrid);
        assaultGridView.setOnItemClickListener(this);
        assaultGridView.setExpanded(true);

        heavyGridView = findViewById(R.id.heavyGrid);
        heavyGridView.setOnItemClickListener(this);
        heavyGridView.setExpanded(true);

        shotgunGrid = findViewById(R.id.shotgunGrid);
        shotgunGrid.setOnItemClickListener(this);
        shotgunGrid.setExpanded(true);

        smgGrid = findViewById(R.id.smgGrid);
        smgGrid.setOnItemClickListener(this);
        smgGrid.setExpanded(true);

        pistolGrid = findViewById(R.id.pistolGrid);
        pistolGrid.setOnItemClickListener(this);
        pistolGrid.setExpanded(true);

        sniperGrid = findViewById(R.id.sniperGrid);
        sniperGrid.setOnItemClickListener(this);
        sniperGrid.setExpanded(true);

        launcherGrid = findViewById(R.id.launcherGrid);
        launcherGrid.setOnItemClickListener(this);
        launcherGrid.setExpanded(true);

        otherGrid = findViewById(R.id.otherGrid);
        otherGrid.setOnItemClickListener(this);
        otherGrid.setExpanded(true);

        WeaponsFetch fetch = new WeaponsFetch();
        fetch.execute();

        /*
        assaultGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        */
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

            Weapon weapon = (Weapon) parent.getItemAtPosition(position);
            Intent i = new Intent(getApplicationContext(),PopActivity.class);
            i.putExtra("weapon", weapon);
            startActivity(i);

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
            List<Weapon> assault = new ArrayList<>();
            List<Weapon> heavy = new ArrayList<>();
            List<Weapon> shotgun = new ArrayList<>();
            List<Weapon> smg = new ArrayList<>();
            List<Weapon> pistol = new ArrayList<>();
            List<Weapon> sniper = new ArrayList<>();
            List<Weapon> launcher = new ArrayList<>();
            List<Weapon> other = new ArrayList<>();



            for(Weapon w: weapons)
            {
                if (w.getName().contains("Assault"))
                {
                    assault.add(w);
                }
                else if (w.getName().toLowerCase().contains("minigun") || w.getName().toLowerCase().contains("light machine"))
                {
                    heavy.add(w);
                }
                else if(w.getName().toLowerCase().contains("shotgun"))
                {
                    shotgun.add(w);
                }
                else if (w.getName().toLowerCase().contains("compact")||w.getName().toLowerCase().contains("submachine"))
                {
                    smg.add(w);
                }
                else if (w.getName().toLowerCase().contains("pistol") || w.getName().toLowerCase().contains("cannon") ||
                        w.getName().toLowerCase().contains("revolver"))
                {
                    pistol.add(w);
                }
                else if(w.getName().toLowerCase().contains("sniper") || w.getName().toLowerCase().contains("hunting"))
                {
                    sniper.add(w);
                }
                else if(w.getName().toLowerCase().contains("launcher") || w.getName().toLowerCase().contains("missile"))
                {
                    launcher.add(w);
                }
                else
                {
                    other.add(w);
                }


            }

            WeaponsAdapter assaultAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,assault);
            assaultGridView.setAdapter(assaultAdapter);

            WeaponsAdapter heavyAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,heavy);
            heavyGridView.setAdapter(heavyAdapter);

            WeaponsAdapter shotgunAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,shotgun);
            shotgunGrid.setAdapter(shotgunAdapter);

            WeaponsAdapter smgAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,smg);
            smgGrid.setAdapter(smgAdapter);

            WeaponsAdapter pistolAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,pistol);
            pistolGrid.setAdapter(pistolAdapter);

            WeaponsAdapter sniperAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,sniper);
            sniperGrid.setAdapter(sniperAdapter);

            WeaponsAdapter launcherAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,launcher);
            launcherGrid.setAdapter(launcherAdapter);

            if (other.size() != 0)
            {
                WeaponsAdapter otherAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,other);
                otherGrid.setAdapter(otherAdapter);
            }
            else if (other.isEmpty())
            {
                View otherText = findViewById(R.id.otherText);
                otherText.setVisibility(View.INVISIBLE);
            }


            dialog.dismiss();
        }
    }
}
