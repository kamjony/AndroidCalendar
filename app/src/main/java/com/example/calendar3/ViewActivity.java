package com.example.calendar3;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.calendar3.MainActivity.getDate;

/**
 * Created by KamrulHasan on 4/7/2017.
 */

public class ViewActivity extends Activity{
    private static final String TAG = "ViewActivity";

    dbHelper dbHelper;

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        mListView = (ListView) findViewById(R.id.listView);
        dbHelper = new dbHelper(this);

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        String date = getDate.toString();
       Cursor dataDate = dbHelper.getDataDate(date);

//        Cursor dataDate = dbHelper.getData();

        //get the data and append to a list

        // Cursor data = dbHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        int position = dataDate.getPosition()+1;


        while (dataDate.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            // listData.add(data.getString(1) + " " + data.getString(2));
            listData.add(dataDate.getString(2) + " , " + dataDate.getString(3));



        }
        String s = position + " " + listData;
        //create the list adapter and set the adapter

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listData);
        Collections.sort(listData);
        mListView.setAdapter(adapter);
    }



    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
