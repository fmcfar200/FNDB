/*
    © 2018 Fraser McFarlane
 */

package com.example.fraser.fndb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class PopActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_activity);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width * 0.8),(int)(height*0.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        Bundle extras = getIntent().getExtras();
        Weapon weapon = (Weapon) extras.get("weapon");
        if (weapon!=null)
        {
            ImageView imageView = findViewById(R.id.popWeaponImage);
            ImageView backgroundView = findViewById(R.id.popWeaponBackground);
            TextView nameText = findViewById(R.id.popWeaponName);
            TextView rarityText = findViewById(R.id.popWeaponRarity);
            TextView damageText = findViewById(R.id.popWeaponDamage);
            TextView dpsText = findViewById(R.id.popWeaponDPS);
            TextView fireRateText = findViewById(R.id.popWeaponFireRate);
            TextView reloadTimeText = findViewById(R.id.popWeaponReloadTime);
            TextView magSizeText = findViewById(R.id.popWeaponMagSize);
            TextView ammoCostText = findViewById(R.id.popWeaponAmmoCost);

            Picasso.with(getApplicationContext()).load(weapon.getImageURL()).into(imageView);
            Picasso.with(getApplicationContext()).load(weapon.getBacgroundURL()).into(backgroundView);


            nameText.setText(weapon.getName());
            rarityText.setText(weapon.getRarity());
            double body = weapon.getStats().getDamage().getBody();
            double head = weapon.getStats().getDamage().getHead();
            damageText.setText("Body: " + String.valueOf(body) + "  Head: " + String.valueOf(head));
            dpsText.setText(String.valueOf(weapon.getStats().getDps()));
            fireRateText.setText(String.valueOf(weapon.getStats().getFirerate()));
            reloadTimeText.setText(String.valueOf(weapon.getStats().getMagazine().getReload()));
            magSizeText.setText(String.valueOf(weapon.getStats().getMagazine().getMagSize()));
            ammoCostText.setText(String.valueOf(weapon.getStats().getAmmoCost()));





        }
    }
}
