package com.example.fraser.fndb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity{

    Toolbar toolbar;
    ListView newsListView;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("News");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


        newsListView = findViewById(R.id.newsList);

        NewsFetch fetch = new NewsFetch();
        fetch.execute();

    }

    private class NewsFetch extends AsyncTask<Void, Void, List<News>> {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(NewsActivity.this);
            dialog.setMessage("Fetching News...");
            dialog.show();
        }


        @Override
        protected List<News> doInBackground(Void... voids)
        {
            List<News> theNewsList = new ArrayList<>();
            try {
                URL endpoint = new URL("https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get");
                HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", getString(R.string.FORTNITE_API_KEY));

                if (connection.getResponseCode() == 200)
                {

                    InputStream responseBody = connection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                    StringBuilder jsonString = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        jsonString.append(line).append('\n');
                    }
                    Log.e("TEST SUCCESS", jsonString.toString());

                    JSONObject rootObject = new JSONObject(String.valueOf(jsonString));
                    JSONArray entryArray = rootObject.getJSONArray("entries");

                    for(int i = 0; i < entryArray.length(); i ++)
                    {
                        JSONObject entry = entryArray.getJSONObject(i);
                        String title = (String) entry.get("title");
                        String body = (String) entry.get("body");
                        String imageUrl = (String) entry.get("image");
                        long time = Long.valueOf((String) entry.get("time"));
                        News newsItem = new News(title,body,imageUrl,time);
                        theNewsList.add(newsItem);

                    }
                }
                else
                {
                    Log.e("TEST FAIL", "FAIL");

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return theNewsList;
        }



        @Override
        protected void onPostExecute(List<News> newsList)
        {
            NewsAdapter adapter = new NewsAdapter(getApplicationContext(),R.layout.list_news_item,newsList);
            newsListView.setAdapter(adapter);

            dialog.dismiss();
        }
    }
}
