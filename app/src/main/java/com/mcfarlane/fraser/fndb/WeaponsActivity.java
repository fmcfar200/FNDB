/*
    © 2018 Fraser McFarlane
 */
package com.mcfarlane.fraser.fndb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;

import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    ExpandableHeightGridView assaultGridView, machineGridView, shotgunGridView,
    smgGridView, pistolGridView, sniperGridView, explosiveGridView;

    List<Weapon> assaultWeaponsList;
    List<Weapon> machineWeaponList;
    List<Weapon> shotgunWeaponList;
    List<Weapon> smgWeaponList;
    List<Weapon> pistolWeaponsList;
    List<Weapon> sniperWeaponsList;
    List<Weapon> explosiveWeaponsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapons);

        assaultWeaponsList = new ArrayList<Weapon>();
        machineWeaponList = new ArrayList<Weapon>();
        shotgunWeaponList = new ArrayList<Weapon>();
        smgWeaponList = new ArrayList<Weapon>();
        pistolWeaponsList = new ArrayList<Weapon>();
        sniperWeaponsList = new ArrayList<Weapon>();
        explosiveWeaponsList = new ArrayList<Weapon>();


        List<AdView> adsList = new ArrayList<>();
        AdView adView = findViewById(R.id.adView);
        AdView adView1 = findViewById(R.id.adView1);
        AdView adView2 = findViewById(R.id.adView2);
        adsList.add(adView);
        adsList.add(adView1);
        adsList.add(adView2);

        boolean adsBool;
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.LOCAL),Context.MODE_PRIVATE);
        adsBool = sharedPreferences.getBoolean(getString(R.string.SP_ADS),true);
        AdManager adManager = new AdManager(this, adsBool);
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

        machineGridView = findViewById(R.id.machineGrid);
        machineGridView.setOnItemClickListener(this);
        machineGridView.setExpanded(true);

        shotgunGridView = findViewById(R.id.shotgunGrid);
        shotgunGridView.setOnItemClickListener(this);
        shotgunGridView.setExpanded(true);

        smgGridView = findViewById(R.id.smgGrid);
        smgGridView.setOnItemClickListener(this);
        smgGridView.setExpanded(true);

        pistolGridView = findViewById(R.id.pistolGrid);
        pistolGridView.setOnItemClickListener(this);
        pistolGridView.setExpanded(true);

        sniperGridView = findViewById(R.id.sniperGrid);
        sniperGridView.setOnItemClickListener(this);
        sniperGridView.setExpanded(true);

        explosiveGridView = findViewById(R.id.explosiveGrid);
        explosiveGridView.setOnItemClickListener(this);
        explosiveGridView.setExpanded(true);



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dataRef;
        dataRef = database.getReference("Weapons");

        final DatabaseReference assaultReference = dataRef.child("Assault Weapons");
        final DatabaseReference explosiveReference = dataRef.child("Explosive Weapons");
        final DatabaseReference machineReference = dataRef.child("Machine Guns");
        final DatabaseReference pistolReference = dataRef.child("Pistols");
        final DatabaseReference smgReference = dataRef.child("SMGs");
        final DatabaseReference shotgunReference = dataRef.child("Shotguns");
        final DatabaseReference sniperReference = dataRef.child("Sniper Rifles");

        GetFirebaseWeapons(assaultReference,assaultWeaponsList,assaultGridView);
        GetFirebaseWeapons(explosiveReference,explosiveWeaponsList,explosiveGridView);
        GetFirebaseWeapons(machineReference,machineWeaponList,machineGridView);
        GetFirebaseWeapons(pistolReference,pistolWeaponsList,pistolGridView);
        GetFirebaseWeapons(smgReference,smgWeaponList,smgGridView);
        GetFirebaseWeapons(shotgunReference,shotgunWeaponList,shotgunGridView);
        GetFirebaseWeapons(sniperReference,sniperWeaponsList,sniperGridView);




        /*
        WeaponsFetch fetch = new WeaponsFetch();
        fetch.execute();
        */

        /*
        assaultGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        */
    }

    private void GetFirebaseWeapons(DatabaseReference reference, final List<Weapon> weaponArrayList, final ExpandableHeightGridView gridView)
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item: dataSnapshot.getChildren())
                {

                    String name = String.valueOf(item.child("name").getValue());
                    String rarity = String.valueOf(item.child("rarity").getValue());
                    String imageLinkSmall = String.valueOf(item.child("images").child("image").getValue());

                    double DPS = Double.valueOf(item.child("stats").child("DPS").getValue().toString());
                    int damage = Integer.valueOf(item.child("stats").child("Damage").getValue().toString());
                    Double fireRate = Double.valueOf(item.child("stats").child("Fire Rate").getValue().toString());
                    int magSize = Integer.valueOf(item.child("stats").child("Magazine Size").getValue().toString());
                    double reloadTime = Double.valueOf(item.child("stats").child("Reload Time").getValue().toString());
                    int structureDamage = Integer.valueOf(item.child("stats").child("Structure Damage").getValue().toString());


                    //test stats
                    WeaponStats stats = new WeaponStats(new Damage(damage,damage),DPS,fireRate,new Magazine(reloadTime,magSize),1);

                    //test weapon
                    Weapon weapon = new Weapon(name,rarity,imageLinkSmall,imageLinkSmall,stats);

                    weaponArrayList.add(weapon);
                }

                WeaponsAdapter testAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,weaponArrayList);
                gridView.setAdapter(testAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
            machineGridView.setAdapter(heavyAdapter);

            WeaponsAdapter shotgunAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,shotgun);
            shotgunGridView.setAdapter(shotgunAdapter);

            WeaponsAdapter smgAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,smg);
            smgGridView.setAdapter(smgAdapter);

            WeaponsAdapter pistolAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,pistol);
            pistolGridView.setAdapter(pistolAdapter);

            WeaponsAdapter sniperAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,sniper);
            sniperGridView.setAdapter(sniperAdapter);

            WeaponsAdapter launcherAdapter = new WeaponsAdapter(getApplicationContext(),R.layout.grid_weapon_item,launcher);
            explosiveGridView.setAdapter(launcherAdapter);



            dialog.dismiss();
        }
    }
}
