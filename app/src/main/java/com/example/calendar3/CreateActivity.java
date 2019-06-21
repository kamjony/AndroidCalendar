package com.example.calendar3;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import static com.example.calendar3.MainActivity.getDate;

/**
 * Created by KamrulHasan on 4/7/2017.
 */

public class CreateActivity extends Activity {
    //declaring variables
    dbHelper dbHelper; //database helper
    private static final String TAG = "CreateActivity";
    private EditText titleText, detailText, thesaurusText;
    ;
    private TextView timeText;
    private ImageView timeImage;
    private ListView suggList;
    private Handler guiThread;
    private ExecutorService suggThread;
    private Runnable updateTask;
    private Future<?> suggPending;
    private List<String> items;
    private ArrayAdapter<String> adapter;
    Button btnSave, btnThesaurus;
    static final int DIALOG_ID = 0;
    int hour_x;
    int minute_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        showTimePicker();

        dbHelper = new dbHelper(this);
        //links the xml to java
        titleText = (EditText) findViewById(R.id.edit_text_title);
        detailText = (EditText) findViewById(R.id.edit_text_details);
        timeText = (TextView) findViewById(R.id.edit_text_time);
        btnSave = (Button) findViewById(R.id.btnSave);
        thesaurusText = (EditText) findViewById(R.id.edit_text_thesaurus);
        btnThesaurus = (Button) findViewById(R.id.btnThesaurus);
        suggList = (ListView) findViewById(R.id.listThesaurus);
        initThreading();
//        setListeners();
        setAdapters();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gets texts from the textfields and edittexts
                String date = getDate.toString();
                String time = timeText.getText().toString();
                String title = titleText.getText().toString();
                String details = detailText.getText().toString();

                Cursor dataDate = dbHelper.getDataDate(date);//gets all data from the database at a specific date
                ArrayList list = new ArrayList(); //creates an arrayList
                //adds the title column to the array list
                while (dataDate.moveToNext()) {
                    list.add(dataDate.getString(3));
                }
                //if the arraylist contains the input title then a toast message pops up orelse data is added to the database
                if (list.contains(title)) {
                    toastMessage("Appointment Title already exits!");
                } else {
                    AddData(date, time, title, details);
                    titleText.setText("");
                    timeText.setText("");
                    detailText.setText("");
                }
            }

        });
        btnThesaurus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queueUpdate(1000 /* milliseconds */);

            }
        });
    }

    //Adds data to the database
    public void AddData(String date, String time, String title, String details) {
        boolean insertData = dbHelper.addData(date, time, title, details);
        if (insertData) {
            toastMessage("Data Inserted into database");
        } else {
            toastMessage("Error when inserting data");
        }
    }
    //for toast message
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //gets the dialog time picker when the time image is clicked
    public void showTimePicker() {
        timeImage = (ImageView) findViewById(R.id.image_view_time_picker);
        timeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new TimePickerDialog(CreateActivity.this, kTimePickerListener, hour_x, minute_x, true);
        return null;

    }

    //after setting the time, the textfield gets the time
    protected TimePickerDialog.OnTimeSetListener kTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hour_x = hourOfDay;
                    minute_x = minute;
                    String newHour;
                    String newMinute;
                    if (hourOfDay < 10) {
                        newHour = "0" + hourOfDay;
                    } else {
                        newHour = "" + hourOfDay;
                    }
                    if (minute < 10) {
                        newMinute = "0" + minute;
                    } else {
                        newMinute = "" + minute;
                    }

                    Toast.makeText(CreateActivity.this, newHour + "  :  " + newMinute, Toast.LENGTH_LONG).show();
                    timeText = (TextView) findViewById(R.id.edit_text_time);
                    timeText.setText(newHour + ":" + newMinute);


                }
            };

    @Override
    protected void onDestroy() {
        // Terminate extra threads here
        suggThread.shutdownNow();
        super.onDestroy();
    }

//    private void doSearch(String query) {
//        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//        intent.putExtra(SearchManager.QUERY, query);
//        startActivity(intent);
//    }

    /**
     * Request an update to start after a short delay
     */
    private void queueUpdate(long delayMillis) {
        // Cancel previous update if it hasn't started yet
        guiThread.removeCallbacks(updateTask);
        // Start an update if nothing happens after a few milliseconds
        guiThread.postDelayed(updateTask, delayMillis);
    }

    /**
     * Modify list on the screen (called from another thread)
     */
    public void setSuggestions(List<String> suggestions) {
        guiSetList(suggList, suggestions);
    }

    /**
     * All changes to the GUI must be done in the GUI thread
     */
    private void guiSetList(final ListView view,
                            final List<String> list) {
        guiThread.post(new Runnable() {
            public void run() {
                setList(list);
            }

        });
    }

    private void setList(List<String> list) {
        adapter.clear();
        adapter.addAll(list);
    }

    /**
     * Display a message
     */
    private void setText(int id) {
        adapter.clear();
        adapter.add(getResources().getString(id));
    }

    /**
     * Initialize multi-threading. There are two threads: 1) The main
     * graphical user interface thread already started by Android,
     * and 2) The suggest thread, which we start using an executor.
     */
    private void initThreading() {
        guiThread = new Handler();
        suggThread = Executors.newSingleThreadExecutor();

        // This task gets suggestions and updates the screen
        updateTask = new Runnable() {
            public void run() {
                // Get text to suggest
                String original = thesaurusText.getText().toString().trim();

                // Cancel previous suggestion if there was one
                if (suggPending != null)
                    suggPending.cancel(true);

                // Check to make sure there is text to work on
                if (original.length() != 0) {
                    // Let user know we're doing something
                    setText(R.string.working);

                    // Begin suggestion now but don't wait for it
                    try {
                        SuggestTask suggestTask = new SuggestTask(
                                CreateActivity.this, // reference to activity
                                original // original text
                        );
                        suggPending = suggThread.submit(suggestTask);
                    } catch (RejectedExecutionException e) {
                        // Unable to start new task
                        setText(R.string.error);
                    }
                }
            }
        };
    }

//    private void setListeners() {
//        // Define listener for text change
//        TextWatcher textWatcher = new TextWatcher() {
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            /* Do nothing */
//            }
//
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                queueUpdate(1000 /* milliseconds */);
//            }
//
//            public void afterTextChanged(Editable s) {
//            /* Do nothing */
//            }
//        };
//
//        // Set listener on the original text field
//        thesaurusText.addTextChangedListener(textWatcher);
//
//        // Define listener for clicking on an item
//        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                String query = (String) parent.getItemAtPosition(position);
//                doSearch(query);
//            }
//        };
//
//        // Set listener on the suggestion list
//        suggList.setOnItemClickListener(clickListener);
//
//
//    }

    private void setAdapters() {
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        suggList.setAdapter(adapter);
    }


}

