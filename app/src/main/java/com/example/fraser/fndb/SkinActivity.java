package com.example.fraser.fndb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

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

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);

        listView = findViewById(R.id.listView);
        skins = new ArrayList<Skin>();

        Bundle extras = getIntent().getExtras();
        final int seasonNo = extras.getInt("seasonNo");

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Battle Pass S" + seasonNo);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            });
        }



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("SPSkin").child("SP_S"+ seasonNo +"_Skins");

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot item: dataSnapshot.getChildren())
                {

                    String id = String.valueOf(item.child("id").getValue());
                    String name = String.valueOf(item.child("name").getValue());
                    String rarity = String.valueOf(item.child("rarity").getValue());
                    String imageID = String.valueOf(item.child("imageId").getValue());
                    //int iImageID = Integer.parseInt(imageID);

                    Skin theSkin = new Skin(id,name,rarity,imageID);
                    skins.add(theSkin);
                }

                adapter = new SkinAdapter(getApplicationContext(), R.layout.simple_list_item,skins, seasonNo);
                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
        try
        {

            JSONObject jsonObject = new JSONObject(GetJSONLocal());
            JSONArray jsonSkinsArray = jsonObject.getJSONArray("SPSkin");

            for(int i = 0; i < jsonSkinsArray.length(); i++)
            {
                JSONObject s = jsonSkinsArray.getJSONObject(i);

                String id = s.getString("id");
                String name = s.getString("name");
                String rarity = s.getString("rarity");
                int imageID = s.getInt("imageId");

                Skin theSkin = new Skin(id,name,rarity,imageID);
                skins.add(theSkin);


            }





        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        */



    }

    public String GetJSONLocal()
    {
        String json = null;

        try
        {
            InputStream stream = getAssets().open("data/FNSData.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return json;

    }
}
