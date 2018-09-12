package com.example.fraser.fndb;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button skinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        skinBtn = findViewById(R.id.skinButton);
        skinBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v ==skinBtn)
        {
            StartActivity(SeasonSelectActivity.class);
        }
    }

    void StartActivity(Class theclass)
    {
        Intent i = new Intent(getApplicationContext(), theclass);
        startActivity(i);
    }
}
