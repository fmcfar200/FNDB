package com.example.fraser.fndb;

import java.io.Serializable;

public class LeaderboardItem implements Serializable {
    private String username;
    private int rank;
    private int kills;
    private int wins;
    private int matches;
    private int score;
    private double kd;
    private String platform;

    private int value;

    public LeaderboardItem(String username, int rank, int value) {
        this.username = username;
        this.rank = rank;
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public int getRank() {
        return rank;
    }

    public int getKills() {
        return kills;
    }

    public int getWins() {
        return wins;
    }

    public int getMatches() {
        return matches;
    }

    public int getScore() {
        return score;
    }

    public double getKd() {
        return kd;
    }

    public String getPlatform() {
        return platform;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setKd(double kd) {
        this.kd = kd;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
