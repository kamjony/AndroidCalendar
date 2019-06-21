package com.example.calendar3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.List;

import static com.example.calendar3.MainActivity.getDate;

/**
 * Created by KamrulHasan on 4/7/2017.
 */

public class DeleteActivity extends Activity{
    //declaring variables
    private static final String TAG = "DeleteActivity";
    dbHelper dbHelper;
    private ListView mListView;
    private EditText deleteText;
    private Button btnDelete,btndelAll;
    private int itemID = -1;
    private ListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        mListView = (ListView) findViewById(R.id.listView);
        deleteText = (EditText) findViewById(R.id.edit_text_delete);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btndelAll = (Button) findViewById(R.id.btnDeleteAll);
        dbHelper = new dbHelper(this);

        populateListView();

        btndelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteAll(getDate);
                toastMessage("deleted");
                populateListView();

            }
        });


    }
    //populates the listview with data
    private void populateListView() {
        String date = getDate.toString(); //gets the date from the main screen calendar
        Cursor dataDate = dbHelper.getDataDate(date); //gets data from the selected date
        //get the data and append to a list
        ArrayList<String> listData = new ArrayList<>();
        while(dataDate.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            //sets the time and title to the list array
            listData.add(dataDate.getString(2) + " , " + dataDate.getString(3));
        }
        //create the list adapter and set the adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listData);
        Collections.sort(listData); //sorts the listdata array
        mListView.setAdapter(adapter); //sets the listview to the adapter
        // set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString(); //gets the position of the item from listview
                String [] splitString = item.split(" , "); //splits the item
                String time = splitString[0]; //first part of the splitted string which is the time
                final String title = splitString[1]; //second part of the splitted string which is the title
                Cursor data = dbHelper.getItemID2(time,title); //get the id associated with that name

                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    deleteText.setText(title); //sets the title of the selected item to the textfield
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeleteActivity.this);
                            //Set Dialog Title
                            builder.setTitle("Alert Dialog");
                            //Set alert icon
                            //builder.setIcon(R.drawable.androidhappy);
                            //Set Dialog Message
                            builder.setMessage("Would you like to delete the event?");
                            //Button function
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbHelper.deleteData(itemID,title); //deletes the selected data
                                    toastMessage("removed from database");
//                                    Intent intent = new Intent(DeleteActivity.this,MainActivity.class);
//                                    startActivity(intent);
                                    populateListView();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do nothing
                                }
                            });

                            AlertDialog alertDialog = builder.create(); //create alert dialog
                            alertDialog.show();//show alert dialog

                        }
                    });
                }
                else{
                    toastMessage("No ID associated with that name");
                }
            }
        });
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
