/*
    © 2018 Fraser McFarlane
 */

package com.example.fraser.fndb;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class StatsListAdapter extends BaseAdapter
{
    private Context mContext;
    private int layoutId;
    private HashMap<String,String> data;
    private ArrayList<String> keys;

    public StatsListAdapter(Context c, int theLayoutID, HashMap<String, String> theData)
    {

        this.mContext = c;
        this.layoutId = theLayoutID;
        this.data = theData;
        this.keys = new ArrayList<>(data.keySet());
        Collections.sort(keys,Collections.reverseOrder());
    }
    public String getKey(int position)
    {
        return keys.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(keys.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String key = getKey(position);
        String value = getItem(position);

        View view = View.inflate(mContext,layoutId,null);


        TextView titleText = view.findViewById(R.id.statTitle);
        TextView dataText = view.findViewById(R.id.statData);

        titleText.setText(key);
        dataText.setText(value);

        view.setTag(data.get(getKey(position)));
        return view;
    }

}
