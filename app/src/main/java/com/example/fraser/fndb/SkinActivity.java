package com.example.fraser.fndb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SkinActivity extends AppCompatActivity {

    ListView listView;
    SkinAdapter adapter;
    List<Skin> skins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);

        listView = findViewById(R.id.listView);
        Skin s1 = new Skin("Aerial Assault Trooper", "Rare", 1);
        Skin s2 = new Skin("Ghoul Trooper", "Epic", 2);
        Skin s3 = new Skin("Renegade Raider", "Rare", 3);
        Skin s4 = new Skin("Skull Trooper", "Epic", 4);



        skins = new ArrayList<Skin>();
        skins.add(s1);
        skins.add(s2);
        skins.add(s3);
        skins.add(s4);
        skins.add(s1);
        skins.add(s2);
        skins.add(s3);
        skins.add(s4);
        skins.add(s1);
        skins.add(s2);
        skins.add(s3);
        skins.add(s4);


        adapter = new SkinAdapter(getApplicationContext(), R.layout.simple_list_item,skins);
        listView.setAdapter(adapter);


    }
}
