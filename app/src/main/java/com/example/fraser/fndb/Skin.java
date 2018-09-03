package com.example.fraser.fndb;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

public class Skin
{
    public String id;
    public String name;
    public String rarity;
    public String desc;
    public int iconID;

    public Skin(String theId, String theName, String theRarity, int theIconID)
    {
        id = theId;
        name = theName;
        rarity = theRarity;
        iconID = theIconID;
    }

}

