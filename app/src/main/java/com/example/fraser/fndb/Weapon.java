/*
    © 2018 Fraser McFarlane
 */

package com.example.fraser.fndb;

import android.os.Parcelable;

import java.io.Serializable;

public class Weapon implements Serializable
{
    private String name;
    private String rarity;
    private String imageURL;
    private String backgroundURL;
    private WeaponStats stats;

    public Weapon(String name, String rarity, String imageURL, String backgroundURL, WeaponStats stats) {
        this.name = name;
        this.rarity = rarity;
        this.imageURL = imageURL;
        this.backgroundURL = backgroundURL;
        this.stats = stats;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setRarity(String rarity) {
        this.rarity = rarity;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public void setBacgroundURL(String backgroundURL) {
        this.backgroundURL = backgroundURL;
    }
    public void setStats(WeaponStats stats) {
        this.stats = stats;
    }

    public String getName() {
        return name;
    }
    public String getRarity() {
        return rarity;
    }
    public String getImageURL() {
        return imageURL;
    }
    public String getBacgroundURL() {
        return backgroundURL;
    }
    public WeaponStats getStats() {
        return stats;
    }

}

class WeaponStats implements Serializable
{
    private Damage damage;
    private double dps;
    private double firerate;

    public Damage getDamage() {
        return damage;
    }

    public double getDps() {
        return dps;
    }

    public double getFirerate() {
        return firerate;
    }

    public Magazine getMagazine() {
        return magazine;
    }

    public int getAmmoCost() {
        return ammoCost;
    }

    private Magazine magazine;
    private int ammoCost;

    public WeaponStats(Damage damage, double dps, double firerate, Magazine magazine, int ammoCost) {
        this.damage = damage;
        this.dps = dps;
        this.firerate = firerate;
        this.magazine = magazine;
        this.ammoCost = ammoCost;
    }
}
class Damage implements Serializable
{
    private double body;

    public Damage(double body, double head) {
        this.body = body;
        this.head = head;
    }

    public double getBody() {
        return body;
    }

    public double getHead() {
        return head;
    }

    private double head;
}
class Magazine implements Serializable
{
    private double reload;
    private int magSize;

    public Magazine(double reload, int magSize) {
        this.reload = reload;
        this.magSize = magSize;
    }

    public double getReload() {
        return reload;
    }

    public int getMagSize() {
        return magSize;
    }


}
