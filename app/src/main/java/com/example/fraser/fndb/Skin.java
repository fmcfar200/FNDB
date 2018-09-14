package com.example.fraser.fndb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.io.Serializable;

public class Skin implements Serializable
{
    public String id;
    public String name;
    public String rarity;
    public String desc;
    public String imageId;
    public String cost;

    public Skin(String theId, String theName, String theRarity, String theImageId)
    {
        id = theId;
        name = theName;
        rarity = theRarity;
        imageId = theImageId;

        switch (rarity)
        {
            case "Uncommon":
                cost = "800";
                break;
            case "Rare":
                cost = "1200";
                break;

            case "Epic":
                cost = "1500";
                break;

            case "Legendary":
                cost = "2000";
                break;
        }
    }

}

