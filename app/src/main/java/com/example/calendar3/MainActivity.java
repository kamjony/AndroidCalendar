package com.example.calendar3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button btnCreate,btnView,btnDel,btnEdit,btnMove,btnSearch;

    public static String getDate;
    dbHelper dbHelper;


    public static CalendarView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = (CalendarView) findViewById(R.id.calendar);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnView = (Button) findViewById(R.id.btnView);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnMove = (Button) findViewById(R.id.btnMove);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayofMonth) {
                int month_x=month+1;
                Toast.makeText(getBaseContext(), "Selected date " + year + "/" + month_x + "/" + dayofMonth, Toast.LENGTH_LONG).show();
                getDate = year + "/" + month_x + "/" + dayofMonth;
            }

        });
        //for all buttons except for search if no date is selected a pop up toast
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getDate != null) {
                    Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                    startActivity(intent);
                }else{
                    toastMessage("select a date");
                }
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getDate != null) {
                    Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                    startActivity(intent);
                }else{
                    toastMessage("select a date");
                }
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getDate != null) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    startActivity(intent);
                }else{
                    toastMessage("select a date");
                }
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDate != null) {
                    Intent intent = new Intent(MainActivity.this,DeleteActivity.class);
                    startActivity(intent);
                } else {
                    toastMessage("select a date");
                }
            }
        });
        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getDate!=null) {
                    Intent intent = new Intent(MainActivity.this, MoveActivity.class);
                    startActivity(intent);
                }else {
                    toastMessage("select a date");
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
