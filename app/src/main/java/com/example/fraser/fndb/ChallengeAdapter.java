package com.example.fraser.fndb;

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

public class ChallengeAdapter extends BaseAdapter {

    Context context;
    int layoutId;
    List<Challenge> data;

    public ChallengeAdapter(Context context, int layoutId, List<Challenge> data) {
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Challenge getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, layoutId, null);

        TextView titleText = view.findViewById(R.id.challengeText);
        TextView amountText = view.findViewById(R.id.challengeAmountText);
        TextView rewardText = view.findViewById(R.id.rewardText);
        ImageView imageIcon = view.findViewById(R.id.battleStarIcon);

        titleText.setText(data.get(position).getTitle());
        amountText.setText("0/" + data.get(position).getQuestAmount());
        rewardText.setText(data.get(position).getRewardAmount());

        Picasso.with(context).load(data.get(position).getRewardImageURL()).into(imageIcon);


        view.setTag(data.get(position));
        return view;
    }

}
