package com.example.fraser.fndb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView;
    TextView nameText;
    TextView costText;
    TextView descText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.itemImage);
        nameText = findViewById(R.id.itemName);
        costText = findViewById(R.id.itemCost);
        descText = findViewById(R.id.itemDesc);

        Bundle extras = getIntent().getExtras();
        Skin skin = (Skin) extras.get("Skin");

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(skin.name);
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



        nameText.setText(skin.name);


    }
}
