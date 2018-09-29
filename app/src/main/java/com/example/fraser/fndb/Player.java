/*
    © 2018 Fraser McFarlane
 */

package com.example.fraser.fndb;

import java.util.HashMap;

class Solo
{
    private int totalWins;
    private int totalKills;
    private int totalMatches;
    private HashMap<String, String> soloStatsMap = new HashMap<>();

    public int getTotalWins() {
        return totalWins;
    }
    public int getTotalKills() {
        return totalKills;
    }
    public int getTotalMatches() {
        return totalMatches;
    }
    public HashMap<String, String> getSoloStatsMap() {
        return soloStatsMap;
    }



    public void setTotalWins(int theTotalWins) {totalWins = theTotalWins;}
    public void setTotalKills(int theTotalKills) {totalKills = theTotalKills;}
    public void setTotalMatches(int theTotalMatches) {totalMatches = theTotalMatches;}
    public void setSoloStatsMap(HashMap<String, String> soloStatsMap) {
        this.soloStatsMap = soloStatsMap;
    }
}
class Duo
{
    private HashMap<String, String> duoStatsMap = new HashMap<>();
    public HashMap<String, String> getDuoStatsMap() {
        return duoStatsMap;
    }
    public void setDuoStatsMap(HashMap<String, String> theduoStatsMap) {
        this.duoStatsMap = theduoStatsMap;
    }



}
class Squad
{
    private HashMap<String, String> squadStatsMap = new HashMap<>();
    public HashMap<String, String> getSquadStatsMap() {
        return squadStatsMap;
    }
    public void setSquadStatsMap(HashMap<String, String> theSquadStatsMap) {
        this.squadStatsMap = theSquadStatsMap;
    }



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
    public Solo solo = new Solo();
    public Duo duo = new Duo();
    public Squad squad = new Squad();

    public String getName() {
        return name;
    }
    public void setName(String theName) {name = theName;}






}
