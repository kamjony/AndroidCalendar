package com.example.calendar3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static com.example.calendar3.MainActivity.getDate;

/**
 * Created by KamrulHasan on 4/7/2017.
 */

public class MoveActivity extends Activity {
    private static final String TAG = "ListDataActivity";
    int year_x,month_x,day_x,mYear, mMonth, mDay;
    static final int DIALOG_ID = 0;
    dbHelper dbHelper;
    private ListView mListView;
    private TextView dateText;
    private Button btnMove;
    private int itemID = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        mListView = (ListView) findViewById(R.id.listView);
        dateText = (TextView) findViewById(R.id.edit_text_date);
        btnMove = (Button) findViewById(R.id.btnMove);
        dbHelper = new dbHelper(this);
//        showDatePicker();
        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        final String date = getDate.toString();
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

        final ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        Collections.sort(listData);
        mListView.setAdapter(adapter);

       mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
               String item = adapterView.getItemAtPosition(position).toString();
               String [] parts = item.split(" , ");
               String time = parts[0];
               final String title = parts[1];


               Log.d(TAG, "onItemClick: You Clicked on " + title);

               Cursor data = dbHelper.getItemID2(time,title); //get the id associated with that name

               while(data.moveToNext()){
                   itemID = data.getInt(0);
               }
               if(itemID > -1){
                //when an item in listview is selected, a date picker shows up
                   showDialog(DIALOG_ID);

                   btnMove.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           String newDate = dateText.getText().toString();
                           dbHelper.updateDate(itemID,newDate);
                           populateListView();
                           toastMessage("event moved to:" + newDate);

                       }
                   });

               }else{
                   toastMessage("No ID associated with that name");
               }
           }
       });


    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID) {
            Calendar c = Calendar.getInstance();
             mYear = c.get(Calendar.YEAR);
             mMonth = c.get(Calendar.MONTH);
             mDay = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(MoveActivity.this, kDatePickerListener, mYear, mMonth, mDay);
        }
        return null;

    }
    protected DatePickerDialog.OnDateSetListener kDatePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    year_x = year;
                    month_x=month+1;
                    day_x=dayOfMonth;
                    dateText.setText(year_x+"/"+month_x+"/"+day_x);
                }
            };
         /**
                 * customizable toast
                 * @param message
                 */

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
