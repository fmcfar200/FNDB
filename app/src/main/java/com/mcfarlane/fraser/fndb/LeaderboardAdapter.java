/*
    © 2018 Fraser McFarlane
 */

package com.mcfarlane.fraser.fndb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LeaderboardAdapter extends BaseAdapter {

    Context context;
    int layoutId;
    List<LeaderboardItem> data;

    public LeaderboardAdapter(Context context, int layoutId, List<LeaderboardItem> data) {
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public LeaderboardItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, layoutId, null);

        TextView titleText = view.findViewById(R.id.statTitle);
        TextView dataText = view.findViewById(R.id.statData);

        titleText.setText(String.valueOf(data.get(position).getRank()) + "  " + data.get(position).getUsername());
        dataText.setText(String.valueOf(data.get(position).getValue()));

        return view;
    }

}
