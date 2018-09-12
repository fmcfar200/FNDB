package com.example.fraser.fndb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SeasonSelectActivity extends AppCompatActivity implements View.OnClickListener
{


    private Button s1Button, s2Button, s3Button, s4Button, s5Button;
    private Toolbar toolbar;

    private int season = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        s1Button = findViewById(R.id.s1Skins);
        s2Button = findViewById(R.id.s2Skins);
        s3Button = findViewById(R.id.s3Skins);
        s4Button = findViewById(R.id.s4Skins);
        s5Button = findViewById(R.id.s5Skins);

        s1Button.setOnClickListener(this);
        s2Button.setOnClickListener(this);
        s3Button.setOnClickListener(this);
        s4Button.setOnClickListener(this);
        s5Button.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Battle Pass Skins");


    }


    @Override
    public void onClick(View v)
    {
        if(v==s1Button)
        {
            season = 1;
        }
        else if (v == s2Button)
        {
            season = 2;
        }
        else if (v == s3Button)
        {
            season = 3;
        }
        else if (v == s4Button)
        {
            season = 4;
        }
        else if (v == s5Button)
        {
            season = 5;
        }

        StartListActivity(season);


    }

    private void StartListActivity(int seasonNo)
    {
        Intent intent = new Intent(getApplicationContext(),SkinActivity.class);
        intent.putExtra("seasonNo", seasonNo);
        startActivity(intent);
    }
}
