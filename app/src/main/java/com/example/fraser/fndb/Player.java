package com.example.fraser.fndb;

import java.util.HashMap;

class Solo
{
    private int totalWins;
    private int totalKills;
    private int totalMatches;

    public int getTotalWins() {
        return totalWins;
    }
    public int getTotalKills() {
        return totalKills;
    }
    public int getTotalMatches() {
        return totalMatches;
    }


    public void setTotalWins(int theTotalWins) {totalWins = theTotalWins;}
    public void setTotalKills(int theTotalKills) {totalKills = theTotalKills;}
    public void setTotalMatches(int theTotalMatches) {totalMatches = theTotalMatches;}
}
class Lifetime extends Solo
{
    Solo lifeTimeSolo = new Solo();


    HashMap<String, String> lifetimeStatsMap = new HashMap<>();

    public HashMap<String, String> getLifetimeStatsMap() {
        return lifetimeStatsMap;
    }
    public void setLifetimeStatsMap(HashMap<String, String> lifetimeStatsMap) {
        this.lifetimeStatsMap = lifetimeStatsMap;
    }

}

public class Player
{
    private String name;
    public Lifetime lifetime = new Lifetime();

    public String getName() {
        return name;
    }
    public void setName(String theName) {name = theName;}






}
