package com.example.fraser.fndb;

public class News
{
    private String title;
    private String body;
    private String imageURL;
    private long timeMillis;

    public News(String title, String body, String imageURL, long timeMillis) {
        this.title = title;
        this.body = body;
        this.imageURL = imageURL;
        this.timeMillis = timeMillis;
    }


    public void setTitle(String title) {
        this.title = title;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public void setTimeMilis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }
    public String getImageURL() {
        return imageURL;
    }
    public long getTimeMilis() {
        return timeMillis;
    }



}
