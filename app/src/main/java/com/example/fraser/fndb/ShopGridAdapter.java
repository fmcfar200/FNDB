package com.example.fraser.fndb;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ShopGridAdapter extends BaseAdapter
{
    private Context mContext;
    private int layoutId;
    private List<ShopItem> data;

    public ShopGridAdapter(Context c, int theLayoutID, List<ShopItem> theData)
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
    public ShopItem getItem(int position) {
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

        RelativeLayout layout = view.findViewById(R.id.itemRootLayout);
        ImageView imageView = view.findViewById(R.id.sItemImage);
        TextView textView = view.findViewById(R.id.sItemName);

        Picasso.with(mContext).load(data.get(position).imageURL).into(imageView);
        textView.setText(data.get(position).name);

        String rarity = data.get(position).rarity;

        if (rarity == "Uncommon")
        {
            layout.setBackgroundResource(R.color.uncommonColor);
        }
        else if (rarity == "Rare")
        {
            layout.setBackgroundResource(R.color.rareColor);
        }
        else if (rarity == "Epic")
        {
            layout.setBackgroundResource(R.color.epicColor);
        }
        else if (rarity == "Legendary")
        {
            layout.setBackgroundResource(R.color.legendaryColor);
        }


        view.setTag(data.get(position));
        return view;
    }
}
