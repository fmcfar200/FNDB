package com.example.fraser.fndb;

import android.content.Intent;
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

import java.io.IOException;
import java.io.InputStream;
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

        AdManager adManager = new AdManager(this);
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
                    //int iImageID = Integer.parseInt(imageID);
                    Skin theSkin = new Skin(id,name,rarity,imageID, desc);
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
