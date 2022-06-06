package com.example.myfirstapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myfirstapp.controller.EventController;
import com.example.myfirstapp.controller.SaveEventHelper;
import com.example.myfirstapp.controller.SaveEventHelperImpl;
import com.example.myfirstapp.internal_storage_helper.InternalStorageHelper;
import com.example.myfirstapp.internal_storage_helper.InternalStorageHelperImpl;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.model.EventModelImpl;
import com.example.myfirstapp.utils.BitmapToByteArrayHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditEventActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.example.myfirstapp.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.myfirstapp.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.example.myfirstapp.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE = "com.example.myfirstapp.EXTRA_DATE";
    public static final String EXTRA_TIME = "com.example.myfirstapp.EXTRA_TIME";
    public static final String EXTRA_LOCATION = "com.example.myfirstapp.EXTRA_LOCATION";
    public static final String EXTRA_IMAGE_NAME = "com.example.myfirstapp.EXTRA_IMAGE_NAME";
    public static final String EXTRA_IMAGE_URI = "com.example.myfirstapp.EXTRA_IMAGE_URI";
    public static final String EXTRA_URI = "com.example.myfirstapp.EXTRA_URI";

    public static int REQUEST_CODE = 0;


    private EditText editTextTitle;
    private EditText editTextDescription;
    private View pickImgBtn;
    private ImageView previewImg;
    private EditText date_picker;
    private Calendar calendar;
    private EditText time_picker;
    int hour, minute;
    private EditText event_location;
    private Bitmap bitmap;
    private BitmapToByteArrayHelper bitmapToByteArrayHelper;
    private Uri urii;
    private Button btn_save_event;
    private EventController eventController;
    private EventModel eventModel;
    SharedPreferences prefs;
    InternalStorageHelper internalStorageHelper;
    SaveEventHelper saveEventHelper;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        pickImgBtn = findViewById(R.id.pick_img_btn);
        previewImg = findViewById(R.id.previewImg);
        date_picker = findViewById(R.id.event_date_picker);
        time_picker = findViewById(R.id.event_time_picker);
        event_location = findViewById(R.id.edit_event_location);
        bitmapToByteArrayHelper = new BitmapToByteArrayHelper();
        btn_save_event = findViewById(R.id.btn_save_event);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                updateCalendar();
            }
        };

        eventModel = new EventModelImpl(MyApplication.getEventDBAdapter());
        eventController = new EventController(eventModel, this);

        internalStorageHelper = new InternalStorageHelperImpl();
        saveEventHelper = new SaveEventHelperImpl();

        btn_save_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null && REQUEST_CODE == MainActivity.ADD_EVENT_REQUEST) {
                    Event event = saveEventHelper.saveEvent(bitmap, editTextTitle, editTextDescription, date_picker,
                            time_picker, event_location, AddEditEventActivity.this, prefs);
                    setResForParAct(event);
                    //                    saveEvent(bitmap);
                } else if (bitmap != null && REQUEST_CODE == MainActivity.EDIT_EVENT_REQUEST) {
                    Event event = saveEventHelper.saveEventEdit(bitmap, editTextTitle, editTextDescription, date_picker,
                            time_picker, event_location, AddEditEventActivity.this, prefs,
                            getIntent().getIntExtra(EXTRA_ID, -1));
                    setResForParAct(event);
                } else {
                    Toast.makeText(AddEditEventActivity.this, "Please " +
                            "choose an image before continuing.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEditEventActivity.this, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        time_picker.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEditEventActivity.this, onTimeSetListener, hour, minute,
                        true);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();
            }
        });

        if (getIntent().hasExtra(EXTRA_ID)) {
            setTitle("Edit event");
            editTextTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
            time_picker.setText(getIntent().getStringExtra(EXTRA_TIME));
            date_picker.setText(getIntent().getStringExtra(EXTRA_DATE));
            event_location.setText(getIntent().getStringExtra(EXTRA_LOCATION));
            internalStorageHelper.loadImageFromStorage(
                    getIntent().getStringExtra(EXTRA_IMAGE_URI),
                    getIntent().getStringExtra(EXTRA_IMAGE_NAME),
                    previewImg,
                    this);


            REQUEST_CODE = MainActivity.EDIT_EVENT_REQUEST;
        } else {
            REQUEST_CODE = MainActivity.ADD_EVENT_REQUEST;
            setTitle("Create an event");
        }

        pickImgBtn.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });
    }

    private void updateCalendar() {
        String format = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        date_picker.setText(simpleDateFormat.format(calendar.getTime()));
    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    if (uri != null) {
                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(uri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Bitmap image = BitmapFactory.decodeStream(inputStream);
                        bitmap = image;
                        urii = uri;
                        previewImg.setImageBitmap(image);
                    }
                }
            });


    private void setResForParAct(Event event) {

        Intent data = new Intent();

        data.putExtra(MainActivity.REQUEST_CODE, REQUEST_CODE);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        doCall(event, id);
        setResult(RESULT_OK, data);

    }

    private void doCall(Event event, int id) {
        if (id != -1) {
            eventController.onEditButtonClicked(event);
        } else {
            eventController.onAddButtonClicked(event);
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_event:
                Event event = saveEventHelper.saveEvent(bitmap, editTextTitle, editTextDescription, date_picker,
                        time_picker, event_location, AddEditEventActivity.this, prefs);
                setResForParAct(event);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Paused Add", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Resumed Add", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "Stopped Add", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Started Add", Toast.LENGTH_SHORT).show();
    }
}