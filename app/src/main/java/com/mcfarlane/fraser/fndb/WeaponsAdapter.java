/*
    © 2018 Fraser McFarlane
 */

package com.mcfarlane.fraser.fndb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class WeaponsAdapter extends BaseAdapter
{
    private Context mContext;
    private int layoutId;
    private List<Weapon> data;

    public WeaponsAdapter(Context c, int theLayoutID, List<Weapon> theData)
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
    public Weapon getItem(int position) {
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
        ImageView imageView = view.findViewById(R.id.weaponImage);
        Picasso.with(mContext).load(data.get(position).getBacgroundURL()).into(imageView);

        switch(data.get(position).getRarity())
        {
            case "Epic":
                imageView.setBackgroundResource(R.color.epicColor);
                break;
            case "Legendary":
                imageView.setBackgroundResource(R.color.legendaryColor);
                break;
            case "Rare":
                imageView.setBackgroundResource(R.color.rareColor);
                break;
            case "Uncommon":
                imageView.setBackgroundResource(R.color.uncommonColor);
                break;
            case "Common":
                imageView.setBackgroundResource(R.color.darkGrey);
                break;
        }

        view.setTag(data.get(position));
        return view;
    }

}
