/*
    © 2018 Fraser McFarlane
 */

package com.example.fraser.fndb;

class Challenge
{
    private String title;
    private String questAmount;
    private String rewardAmount;
    private String rewardImageURL;

    public Challenge(String title, String questAmount, String rewardAmount) {
        this.title = title;
        this.questAmount = questAmount;
        this.rewardAmount = rewardAmount;
    }


    public String getRewardImageURL() {
        return rewardImageURL;
    }

    public void setRewardImageURL(String rewardImageURL) {
        this.rewardImageURL = rewardImageURL;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuestAmount(String questAmount) {
        this.questAmount = questAmount;
    }

    public void setRewardAmount(String rewardAmount) {
        this.rewardAmount = rewardAmount;
    }



    public String getTitle() {
        return title;
    }

    public String getQuestAmount() {
        return questAmount;
    }

    public String getRewardAmount() {
        return rewardAmount;
    }


}
