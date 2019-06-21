package com.example.calendar3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.calendar3.MainActivity.getDate;

/**
 * Created by KamrulHasan on 4/7/2017.
 */

/**
 * Class similar to DeleteActivity.class, please check comments
 */
public class EditActivity extends Activity {
    //declaring variables
    private static final String TAG = "EditActivity";
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
        String date = getDate.toString();
        Cursor dataDate = dbHelper.getDataDate(date);
        //get the data and append to a list
        // Cursor data = dbHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while (dataDate.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            // listData.add(data.getString(1) + " " + data.getString(2));
            listData.add(dataDate.getString(2) + " , " + dataDate.getString(3));
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        Collections.sort(listData);
        mListView.setAdapter(adapter);

        // set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                String[] parts = item.split(" , ");
                String time = parts[0];
                String title = parts[1];
                Log.d(TAG, "onItemClick: You Clicked on " + title);
                Cursor data = dbHelper.getItemID2(time, title); //get the id associated with that name
                int itemID = -1;
                while (data.moveToNext()) {
                    itemID = data.getInt(0);
                }
                //when the item in the list view is clicked, the id,title,time is passed to the EditDataActivity.class
                if (itemID > -1) {
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(EditActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id", itemID);
                    editScreenIntent.putExtra("title", title);
                    editScreenIntent.putExtra("time", time);
                    startActivity(editScreenIntent);

                } else {
                    toastMessage("No ID associated with that name");
                }
            }
        });
    }

    /**
     * customizable toast
     *
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
