package com.example.myfirstapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myfirstapp.controller.EventAdapter;
import com.example.myfirstapp.controller.EventController;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.model.EventModelImpl;
import com.example.myfirstapp.utils.BitmapToByteArrayHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

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
    public static final String EXTRA_IMAGE = "com.example.myfirstapp.EXTRA_IMAGE";
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
    private EventController eventController;
    private EventModel eventModel;
    SharedPreferences prefs;



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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_close);
        /*
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);

         */

        if (getIntent().hasExtra(EXTRA_ID)) {
            setTitle("Edit event");
            editTextTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
            REQUEST_CODE = HomeFragment.EDIT_EVENT_REQUEST;
        } else {
            REQUEST_CODE = HomeFragment.ADD_EVENT_REQUEST;
            setTitle("Create an event");
        }

        pickImgBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
            mGetContent.launch("image/*");

//            startActivityForResult(Intent.createChooser(intent, "Pick image"), IMAGE_REQUEST_ID);


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

                        Log.d("ADD_EDIT_EVENT_ACT", "PICKED IMAGE");
                    }
                }
            });



    private void saveEvent(Bitmap bitmap) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String date = date_picker.getText().toString();

        String time = time_picker.getText().toString();
        String location = event_location.getText().toString();

        if (title.trim().isEmpty() || description.trim().isEmpty() ||
                date.trim().isEmpty() || time.trim().isEmpty() || location.trim().isEmpty()) {
            Toast.makeText(this, "Please insert all necessary information", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
//        data.putExtra(EXTRA_IMAGE, bitmap);
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_URI, urii);
        data.putExtra(EXTRA_TIME, time);
        data.putExtra(EXTRA_LOCATION, location);
        data.putExtra(HomeFragment.REQUEST_CODE, REQUEST_CODE);


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date datee = new Date(System.currentTimeMillis());

        Random random = new Random();
        int rand_id = random.nextInt();
        String imgName =  "img" + String.valueOf(rand_id) + ".jpg";
        String path = saveToInternalStorage(bitmap, rand_id);

        Event event = new Event(random.nextInt(),  prefs.getInt(String.valueOf(R.string.pref_user_id), 0),
                title, description, path, imgName,
                0, location, date, time, formatter.format(datee),
                formatter.format(datee));

        eventModel = new EventModelImpl(MyApplication.getEventDBAdapter());
        eventController = new EventController(eventModel, this);
        new InsertEventAsyncTask(eventController).execute(event);


        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
//        finish();
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
                saveEvent(bitmap);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, int id){
        ContextWrapper cw = new ContextWrapper(AddEditEventActivity.this);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        String name = "img" + String.valueOf(id) + ".jpg";
        File mypath = new File(directory, name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    private static class InsertEventAsyncTask extends AsyncTask<Event, Void, List<Event>> {
        private EventController eventController;
//        private EventAdapter eventAdapter;

        private InsertEventAsyncTask(EventController eventController) {
            this.eventController = eventController;
//            this.eventAdapter = eventAdapter;
        }

        @Override
        protected List<Event> doInBackground(Event... events) {
            eventController.onAddButtonClicked(events[0]);
            return eventController.onViewLoaded();
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);
            List<Event> events1 = new ArrayList<Event>();
            events1.addAll(events);
            for (int i = 0; i < events1.size(); i++) {
                Log.d("TAG", events1.get(i).getTitle());
            }
//            eventAdapter.notifyDataSetChanged();

//            events1.addAll(events);
//            eventAdapter.submitList(events1);
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