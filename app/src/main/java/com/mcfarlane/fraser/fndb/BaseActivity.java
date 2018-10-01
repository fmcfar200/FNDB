
/*
    © 2018 Fraser McFarlane
 */
package com.mcfarlane.fraser.fndb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public DrawerLayout drawer;
    public Toolbar toolbar;

    protected void onCreateDrawer()
    {
        drawer = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);


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
        else if (id == R.id.drWeapons)
        {
            StartActivity(WeaponsActivity.class);
        }
        else if (id == R.id.drStats)
        {
            StartActivity(PlayerStatsActivity.class);
        }
        else if (id == R.id.drLeaderboards)
        {
            StartActivity(LeaderboardActivity.class);
        }
        else if (id == R.id.drNews)
        {
            StartActivity(NewsActivity.class);
        }
        else if (id == R.id.drAbout)
        {
            StartActivity(AboutActivity.class);
        }
        else if (id == R.id.drRemoveAds)
        {

        }



        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void StartActivity(Class theclass)
    {
        Intent i = new Intent(getApplicationContext(), theclass);
        startActivity(i);
    }
}
