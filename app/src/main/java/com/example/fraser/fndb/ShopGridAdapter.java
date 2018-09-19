package com.example.fraser.fndb;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

        FrameLayout costLayout = view.findViewById(R.id.costLayout);
        TextView costText = view.findViewById(R.id.costText);

        String rarity = data.get(position).rarity;
        if (rarity == "Uncommon")
        {
            rootLayout.setBackgroundResource(R.color.uncommonColor);
            //costLayout.setBackgroundResource(R.color.uncommonColor);

        }
        else if (rarity == "Rare")
        {
            rootLayout.setBackgroundResource(R.color.rareColor);
            //costLayout.setBackgroundResource(R.color.rareColor);

        }
        else if (rarity == "Epic")
        {
            rootLayout.setBackgroundResource(R.color.epicColor);
            //costLayout.setBackgroundResource(R.color.rareColor);

        }
        else if (rarity == "Legendary")
        {
            rootLayout.setBackgroundResource(R.color.legendaryColor);
            //costLayout.setBackgroundResource(R.color.rareColor);

        }

        nameText.setBackgroundColor(getColorWithAlpha(R.color.blackAlpha,0.7f));

        Picasso.with(mContext).load(data.get(position).imageURL).into(imageView);
        nameText.setText(data.get(position).name);
        costText.setText(data.get(position).vBucks);



        view.setTag(data.get(position));
        return view;
    }

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }
}
