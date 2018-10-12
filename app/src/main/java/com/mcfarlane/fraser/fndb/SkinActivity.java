/*
    © 2018 Fraser McFarlane
 */
package com.mcfarlane.fraser.fndb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SkinActivity extends AppCompatActivity {

    ListView listView;
    SkinAdapter adapter;
    List<Skin> skins;

    SeasonSelectActivity.SearchType searchType;
    int seasonNo;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);

        boolean adsBool;
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.LOCAL),Context.MODE_PRIVATE);
        adsBool = sharedPreferences.getBoolean(getString(R.string.SP_ADS),true);
        AdManager adManager = new AdManager(this, adsBool);
        adManager.CreateAndLoadBanner();

        listView = findViewById(R.id.listView);
        skins = new ArrayList<Skin>();

        Bundle extras = getIntent().getExtras();
        seasonNo = extras.getInt("seasonNo");
        searchType = (SeasonSelectActivity.SearchType) extras.get("type");


        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            if (searchType == SeasonSelectActivity.SearchType.BP)
            {
                getSupportActionBar().setTitle("Battle Pass S" + seasonNo);

            }
            else if (searchType == SeasonSelectActivity.SearchType.HOLIDAY)
            {
                getSupportActionBar().setTitle("Seasonal Skins");

            }
            else if (searchType == SeasonSelectActivity.SearchType.PROMOTIONAL)
            {
                getSupportActionBar().setTitle("Promo Skins");

            }
            else
            {
                getSupportActionBar().setTitle(searchType.toString().toUpperCase() + " Skins");

            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }



        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference dataRef;
        if (searchType == SeasonSelectActivity.SearchType.BP)
        {
            dataRef = database.getReference("SPSkin").child("SP_S"+ seasonNo +"_Skins");
        }
        else
        {
            dataRef = database.getReference("ISSkin").child(searchType.toString().toLowerCase() + "_Skins");
        }

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot item: dataSnapshot.getChildren())
                {

                    String id = String.valueOf(item.child("id").getValue());
                    String name = String.valueOf(item.child("name").getValue());
                    String rarity = String.valueOf(item.child("rarity").getValue());
                    String imageID = String.valueOf(item.child("imageId").getValue());
                    String desc = String.valueOf(item.child("dsc").getValue());
                    String imageLinkSmall = String.valueOf(item.child("imageLinkSmall").getValue());

                    //int iImageID = Integer.parseInt(imageID);
                    Skin theSkin = new Skin(id,name,rarity,imageID, desc);
                    theSkin.setImageLinkSmall(imageLinkSmall);

                    skins.add(theSkin);
                }

                adapter = new SkinAdapter(getApplicationContext(), R.layout.simple_list_item,skins, seasonNo, searchType);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Skin item = (Skin) adapter.getItemAtPosition(position);
                StartFullDetail(item);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.toolbarSearch));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void StartFullDetail(Skin skin)
    {
        Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
        intent.putExtra("Skin", skin);
        intent.putExtra("seasonNo",seasonNo);
        intent.putExtra("type", searchType);
        startActivity(intent);
    }

}
