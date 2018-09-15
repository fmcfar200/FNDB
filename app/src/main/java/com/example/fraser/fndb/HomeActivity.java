package com.example.fraser.fndb;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    Button skinBtn;
    Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_open, R.string.nav_closed)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.bringToFront();
            }
        };



        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        skinBtn = findViewById(R.id.skinButton);
        skinBtn.setOnClickListener(this);

        testButton = findViewById(R.id.statsButton);
        testButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        if (v ==skinBtn)
        {
            StartActivity(SeasonSelectActivity.class);
        }
        else if (v == testButton)
        {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TestAPI();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    void StartActivity(Class theclass)
    {
        Intent i = new Intent(getApplicationContext(), theclass);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.drHome)
        {

        }
        else if (id == R.id.drSkins)
        {
            StartActivity(SeasonSelectActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void TestAPI() throws MalformedURLException {
        URL endpoint = new URL("https://api.fortnitetracker.com/v1/store");

        try {
            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
            connection.setRequestProperty("TRN-Api-Key", "246a06d4-9ecc-443f-bd96-67e18bb94e4d");

            if (connection.getResponseCode() == 200)
            {

                InputStream responseBody = connection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line).append('\n');
                }

                Log.e("Response", "200: " + total);


                //InputStreamReader reader = new InputStreamReader(responseBody, "UTF-8");


                //JsonReader jsonReader = new JsonReader(reader);




            }
            else
            {
                Log.e("Response", "Error");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
