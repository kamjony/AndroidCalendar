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
 * Created by KamrulHasan on 4/7/2017.
 */

public class EditDataActivity extends Activity {

    private static final String TAG = "EditDataActivity";

    private ImageView timeImage;
    Button btnUpdate;
    static final int DIALOG_ID = 0;
    int hour_x;
    int minute_x;
    private EditText editable_title,editable_detail;
    private TextView editable_time;

    dbHelper dbHelper;

    private String selectedTitle,selectedTime,selectedDetail;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        showTimePicker();
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        editable_title = (EditText) findViewById(R.id.editable_title);
        editable_time = (TextView) findViewById(R.id.editable_time);
        editable_detail = (EditText) findViewById(R.id.editable_detail);
        dbHelper = new dbHelper(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        selectedTitle = receivedIntent.getStringExtra("title");

        selectedTime = receivedIntent.getStringExtra("time");

        //set the text to show the current selected name
        editable_title.setText(selectedTitle);
        editable_time.setText(selectedTime);

        //gets the input and updates the database
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editable_title.getText().toString();
                String time = editable_time.getText().toString();
                String detail = editable_detail.getText().toString();
                if(!title.equals("")&&!time.equals("")&&!detail.equals("")){
                    dbHelper.updateData(selectedID,time,title,detail);
                    toastMessage("Appointment Edited to: " + title + " " + time);
                    Intent intent = new Intent(EditDataActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    toastMessage("You must fill in everything");
                }
            }
        });
    }

    //shows the time picker
    public void showTimePicker(){
        timeImage = (ImageView) findViewById(R.id.image_view_time_picker);
        timeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);

            }
        });
    }
    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID)
            return new TimePickerDialog(EditDataActivity.this, kTimePickerListener, hour_x,minute_x,true);
        return null;

    }



    protected TimePickerDialog.OnTimeSetListener kTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hour_x = hourOfDay;
                    minute_x = minute;
                    String newHour;
                    String newMinute;
                    if (hourOfDay<10){
                        newHour = "0"+hourOfDay;
                    }else{
                        newHour=""+hourOfDay;
                    }
                    if (minute<10){
                        newMinute = "0"+minute;
                    }else{
                        newMinute=""+minute;
                    }

                    Toast.makeText(EditDataActivity.this,newHour + "  :  " + newMinute,Toast.LENGTH_LONG).show();

                    editable_time.setText(newHour + ":" + newMinute);

                }
            };

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
