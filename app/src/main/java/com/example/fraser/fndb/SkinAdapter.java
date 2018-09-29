/*
    © 2018 Fraser McFarlane
 */
package com.example.fraser.fndb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkinAdapter extends BaseAdapter implements Filterable{

    Context context;
    int layoutId;
    List<Skin> data;
    List<Skin> filterData;
    Filter mFilter;

    SeasonSelectActivity.SearchType type;
    int seasonNo;

    public SkinAdapter (Context context, int layoutId, List<Skin> data, int theSeasonNo , SeasonSelectActivity.SearchType theType)
    {
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
        seasonNo = theSeasonNo;
        type = theType;

        filterData = data;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        StorageReference imageStore = FirebaseStorage.getInstance().getReference();
        final StorageReference imageRef;

        if (type == SeasonSelectActivity.SearchType.BP)
        {
            imageRef = imageStore.child("s"+seasonNo+"_images");
        }
        else
        {
            imageRef = imageStore.child(type.toString().toLowerCase()+"_images");

        }


        View view = View.inflate(context,layoutId,null);

        TextView nameText = view.findViewById(R.id.itemHeading);
        ImageView currencyIcon = view.findViewById(R.id.currencyIcon);
        TextView costText = view.findViewById(R.id.costText);
        final ImageView icon = view.findViewById(R.id.itemIcon);

        nameText.setText(data.get(position).name);
        if(type == SeasonSelectActivity.SearchType.BP)
        {
            currencyIcon.setImageResource(R.drawable.ic_battlepass);
            costText.setText("");
        }
        else
        {
            currencyIcon.setImageResource(R.drawable.ic_vbucks);
            costText.setText(data.get(position).cost);

        }


        try {
            StorageReference fileRef = imageRef.child(data.get(position).imageId + ".JPG");
            final File localFile = File.createTempFile(data.get(position).imageId,".jpg");
            fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    icon.setImageBitmap(bmp);
                    return; }});
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }




        view.setTag(data.get(position));
        return view;
    }

    @Override
    public Filter getFilter()
    {
        if (mFilter == null)
        {
            mFilter = new ItemFilter();
        }
        return mFilter;
    }

    private class ItemFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0)
            {
                List<Skin> filteredList = new ArrayList<>();
                for(int i = 0; i < filterData.size(); i++)
                {
                    if (filterData.get(i).name.toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        Skin item = filterData.get(i);
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }
            else
            {
                results.count = filterData.size();
                results.values = filterData;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            data = (List<Skin>) results.values;
            notifyDataSetChanged();
        }
    }
}