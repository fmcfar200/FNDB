package com.example.fraser.fndb;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemSearch
{
    public static void SearchDatabase(final String theQuery, final SeasonSelectActivity.SearchType theSearchType, final int theSeason )
    {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String query = theQuery;
                SeasonSelectActivity.SearchType searchType = theSearchType;
                int season = theSeason;
                for (DataSnapshot typeSnap: dataSnapshot.getChildren())
                {
                    for (DataSnapshot objSnap: typeSnap.getChildren())
                    {
                        for (DataSnapshot item: objSnap.getChildren())
                        {
                            if (item.child("name").getValue().equals(query))
                            {
                                String id = String.valueOf(item.child("id").getValue());
                                String name = String.valueOf(item.child("name").getValue());
                                String rarity = String.valueOf(item.child("rarity").getValue());
                                String imageID = String.valueOf(item.child("imageId").getValue());
                                //int iImageID = Integer.parseInt(imageID);

                                Skin theSkin = new Skin(id,name,rarity,imageID);

                                if (typeSnap.getKey().contains("SP"))
                                {
                                    searchType = SeasonSelectActivity.SearchType.BP;
                                    String collectionString = objSnap.getKey().toLowerCase();
                                    String[] array1 = collectionString.split("_");
                                    String[] array2 = array1[1].split("s");
                                    season = Integer.parseInt(array2[1]);

                                    SeasonSelectActivity activity = new SeasonSelectActivity();
                                    activity.StartDetail(theSkin,season,searchType);
                                }
                                else if (typeSnap.getKey().contains("IS"))
                                {
                                    season = 0;
                                    searchType = SeasonSelectActivity.SearchType.valueOf(rarity.toUpperCase());

                                    SeasonSelectActivity activity = new SeasonSelectActivity();
                                    activity.StartDetail(theSkin,season,searchType);

                                }

                            }

                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
