package com.example.fraser.fndb;


public class ShopItem
{
    String imageURL;
    String name;
    String rarity;
    String storeCategory;
    String vBucks;

    public ShopItem(String theImageURL, String theName, String theRarity, String theStoreCategory, String theVBucks)
    {
        imageURL = theImageURL;
        name = theName;
        rarity = theRarity;
        storeCategory = theStoreCategory;
        vBucks = theVBucks;

        switch (rarity)
        {
            case "Handmade":
                rarity = "Uncommon";
                break;
            case "Sturdy":
                rarity = "Rare";
                break;
            case "Quality":
                rarity = "Epic";
                break;
            case "Fine":
                rarity = "Legendary";
                break;
        }
    }
}
