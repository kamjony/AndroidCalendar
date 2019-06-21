package com.example.calendar3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.calendar3.MainActivity.getDate;

/**
 * Created by KamrulHasan on 4/7/2017.
 */

public class SearchActivity extends Activity {
    private static final String TAG = "SearchActivity";
    dbHelper dbHelper;
    EditText searchText;
    private ListView mListView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mListView = (ListView) findViewById(R.id.listView);
        searchText = (EditText) findViewById(R.id.edit_text_search);

        dbHelper = new dbHelper(this);
        populateListView();

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }
            //when typing in search, matched titles and details are visible in the list view
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if search string is empty
                if (s.toString().equals("")) {
                    populateListView();
                } else {
                    adapter.getFilter().filter(s); //filters the adapter to the input string
                    mListView.setVisibility(View.VISIBLE); // listview set to visible

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });
    }


    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

//        String date = getDate.toString();
//        Cursor dataDate = dbHelper.getDataDate(date);
        final Cursor dataAll = dbHelper.getData();
        //get the data and append to a list
        // Cursor data = dbHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while (dataAll.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            // listData.add(data.getString(1) + " " + data.getString(2));
            listData.add(dataAll.getString(3) +" , "+ dataAll.getString(4));
//            listData.add(dataAll.getString(3));
        }
        //create the list adapter and set the adapter

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);
        mListView.setVisibility(View.INVISIBLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                String item = adapterView.getItemAtPosition(i).toString();
                String[] split = item.split(" , ");
                String head = split[0];
                Log.d(TAG, "onItemClick: You Clicked on " + item);
                Cursor getID = dbHelper.getItemID(head); //get the id associated with that name
                Cursor data = dbHelper.getData2(head); //getting everything related to the title of the item
                ArrayList<String> dataArray = new ArrayList<>();
                while (data.moveToNext()){
                    dataArray.add(data.getString(1) + "," + data.getString(2) + "," + data.getString(3) + "," + data.getString(4));
                }
                String s = dataArray.toString();
                String[] parts = s.split(",");
                String date = parts[0].substring(1, parts[0].length());
                String time = parts[1];
                String title = parts[2];
                String detail = parts[3].substring(0, parts[3].length()-1);
                int itemID = -1;
                while (getID.moveToNext()) {

                    itemID = getID.getInt(0);
                }
                if (itemID > -1) {
                    //passes the strings to SearchDataActivity.class
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(SearchActivity.this, SearchDataActivity.class);
                    editScreenIntent.putExtra("id", itemID);
                    editScreenIntent.putExtra("date", date);
                    editScreenIntent.putExtra("time", time);
                    editScreenIntent.putExtra("title", title);
                    editScreenIntent.putExtra("detail", detail);
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
