/*
    © 2018 Fraser McFarlane
 */
package com.example.fraser.fndb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class NewsAdapter extends BaseAdapter
{
    private Context mContext;
    private int layoutId;
    private List<News> data;

    public NewsAdapter(Context c, int theLayoutID, List<News> theData)
    {

        this.mContext = c;
        this.layoutId = theLayoutID;
        this.data = theData;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public News getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = View.inflate(mContext,layoutId,null);

        ImageView imageView = view.findViewById(R.id.newsImage);
        TextView titleText = view.findViewById(R.id.newsTitle);
        TextView bodyText = view.findViewById(R.id.newsBody);
        TextView timeText = view.findViewById(R.id.newsTime);

        Picasso.with(mContext).load(data.get(position).getImageURL()).into(imageView);
        titleText.setText(data.get(position).getTitle());
        bodyText.setText(data.get(position).getBody());

        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        Date currentTime = Calendar.getInstance().getTime();
        Date newsTime;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data.get(position).getTimeMilis() * 1000);
        calendar.setTimeZone(TimeZone.getDefault());
        newsTime = calendar.getTime();

        long difference = currentTime.getTime() - newsTime.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = difference / daysInMilli;
        difference = difference % daysInMilli;

        long elapsedHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        long elapsedMinutes = difference / minutesInMilli;
        difference = difference % minutesInMilli;


        timeText.setText(getTimeOutput(elapsedDays,elapsedHours,elapsedMinutes));


        view.setTag(data.get(position));
        return view;
    }

    private String getTimeOutput(long elapsedDays, long elapsedHours, long elapsedMinutes)
    {
        String output = "";

        if (elapsedDays >= 1)
        {
            output = String.valueOf(elapsedDays) + " days ago";
            if (elapsedDays == 1)
            {
                output = String.valueOf(elapsedDays) + " day ago";
            }
        }
        else if (elapsedDays == 0 && elapsedHours < 24 && elapsedHours > 0)
        {
            output = String.valueOf(elapsedHours) + " hours ago";
            if (elapsedHours == 1)
            {
                output = String.valueOf(elapsedHours) + " hour ago";
            }

        }
        else if (elapsedHours < 1 && elapsedMinutes <=59 && elapsedMinutes > 0 )
        {
            output = String.valueOf(elapsedMinutes) + " minutes ago";
            if (elapsedMinutes == 1)
            {
                output = String.valueOf(elapsedMinutes) + " minute ago";
            }
        }
        else
        {
            output = "Just now";
        }

        return output;
    }
}
