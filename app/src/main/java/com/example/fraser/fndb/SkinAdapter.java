package com.example.fraser.fndb;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SkinAdapter extends BaseAdapter {

    Context context;
    int layoutId;
    List<Skin> data;

    public SkinAdapter (Context context, int layoutId, List<Skin> data)
    {
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Skin getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context,layoutId,null);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "font/LuckiestGuy.ttf");
        TextView nameText = view.findViewById(R.id.itemHeading);
        ImageView icon = view.findViewById(R.id.itemIcon);

        nameText.setText(data.get(position).name);
        nameText.setTypeface(font);

        int id = context.getResources().getIdentifier("ic" + data.get(position).iconID,
                "drawable", context.getPackageName());

        icon.setImageResource(id);

        view.setTag(data.get(position));
        return view;
    }
}