/*
    © 2018 Fraser McFarlane
 */
package com.mcfarlane.fraser.fndb;

import java.io.Serializable;

public class Skin implements Serializable
{
    public String id;
    public String name;
    public String rarity;
    public String desc;
    public String imageId;
    public String cost;
    public String imageLinkSmall;

    public Skin(String theId, String theName, String theRarity, String theImageId, String theDesc)
    {
        id = theId;
        name = theName;
        rarity = theRarity;
        imageId = theImageId;
        desc = theDesc;

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

    public void setImageLinkSmall(String imageLinkSmall) {
        this.imageLinkSmall = imageLinkSmall;
    }

    public String getImageLinkSmall() {
        return imageLinkSmall;
    }
}

