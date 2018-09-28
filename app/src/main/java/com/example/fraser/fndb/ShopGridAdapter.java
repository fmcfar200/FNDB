package com.example.fraser.fndb;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        LinearLayout rootLayout = view.findViewById(R.id.itemRootLayout);
        ImageView imageView = view.findViewById(R.id.sItemImage);
        TextView nameText = view.findViewById(R.id.sItemName);

        TextView costText = view.findViewById(R.id.costText);

        String rarity = data.get(position).rarity;
        if (rarity == "Uncommon")
        {
            rootLayout.setBackgroundResource(R.color.uncommonColor);

        }
        else if (rarity == "Rare")
        {
            rootLayout.setBackgroundResource(R.color.rareColor);

        }
        else if (rarity == "Epic")
        {
            rootLayout.setBackgroundResource(R.color.epicColor);

        }
        else if (rarity == "Legendary")
        {
            rootLayout.setBackgroundResource(R.color.legendaryColor);

        }

        Picasso.with(mContext).load(data.get(position).imageURL).into(imageView);
        nameText.setText(data.get(position).name);
        costText.setText(data.get(position).vBucks);



        view.setTag(data.get(position));
        return view;
    }

    /*
    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }
    */
}
