package com.example.calendar3;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by KamrulHasan on 4/9/2017.
 */

public class SearchDataActivity extends Activity {
    private static final String TAG = "SearchDataActivity";
    private Button btnBack;
    private TextView tv_date,tv_time,tv_title,tv_details;
    dbHelper dbHelper;
    private String selectedTitle,selectedTime,selectedDetail,selectedDate;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        btnBack = (Button) findViewById(R.id.btnBack);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_details = (TextView) findViewById(R.id.tv_details);
        dbHelper = new dbHelper(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //now get the strings we passed as an extra
        selectedDate=receivedIntent.getStringExtra("date");
        selectedTitle = receivedIntent.getStringExtra("title");
        selectedTime = receivedIntent.getStringExtra("time");
        selectedDetail = receivedIntent.getStringExtra("detail");

        //set the text to show the current selected strings
        tv_title.setText(selectedTitle);
        tv_time.setText(selectedTime);
        tv_date.setText(selectedDate);
        tv_details.setText(selectedDetail);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchDataActivity.this,MainActivity.class);
                startActivity(intent);
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
