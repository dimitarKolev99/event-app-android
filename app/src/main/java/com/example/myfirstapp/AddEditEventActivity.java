package com.example.myfirstapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myfirstapp.controller.EventController;
import com.example.myfirstapp.controller.EventControllerImpl;
import com.example.myfirstapp.controller.SaveEventHelper;
import com.example.myfirstapp.controller.SaveEventHelperImpl;
import com.example.myfirstapp.utils.internal_storage_helper.InternalStorageHelper;
import com.example.myfirstapp.utils.internal_storage_helper.InternalStorageHelperImpl;
import com.example.myfirstapp.model.DBHelper;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.model.EventModelImpl;
import com.example.myfirstapp.network.PostEventRequest;
import com.example.myfirstapp.network.UpdateEvent;
import com.example.myfirstapp.network.WebSocketClient;
import com.example.myfirstapp.utils.BitmapToByteArrayHelper;
import com.example.myfirstapp.utils.ImageEncoder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
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
    public static final String EXTRA_IMAGE_NAME = "com.example.myfirstapp.EXTRA_IMAGE_NAME";
    public static final String EXTRA_IMAGE_URI = "com.example.myfirstapp.EXTRA_IMAGE_URI";
    public static final String EXTRA_URI = "com.example.myfirstapp.EXTRA_URI";
    public static final String EXTRA_ORGANIZER_ID = "com.example.myfirstapp.EXTRA_ORGANIZER_ID";

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
    PostEventRequest postEventRequest;

    private String title;
    private int id;
    private String imgPath = "";

    InternalStorageHelper internalStorageHelper;
    SaveEventHelper saveEventHelper;

    ImageEncoder imageEncoder;

    WebSocketClient webSocketClient;

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
        imageEncoder = new ImageEncoder(bitmapToByteArrayHelper, this);
        btn_save_event = findViewById(R.id.btn_save_event);

        prefs = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);

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

        eventModel = new EventModelImpl(new DBHelper(this));
        eventController = new EventControllerImpl(eventModel, this);

        internalStorageHelper = new InternalStorageHelperImpl();
        saveEventHelper = new SaveEventHelperImpl(eventController);

        btn_save_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null && REQUEST_CODE == MainActivity.ADD_EVENT_REQUEST) {

                    String titleField = editTextTitle.getText().toString();
                    title = titleField;
                    int id1 = new Random().nextInt();

                    id = id1;

                    new SaveImgToInternalStorageAsyncTask(bitmap, id, AddEditEventActivity.this).execute();

                } else if (REQUEST_CODE == MainActivity.EDIT_EVENT_REQUEST) {
                    //edit event local db, make post request
                    String title = editTextTitle.getText().toString();


                    UpdateEvent updateEvent = new UpdateEvent(AddEditEventActivity.this);
                    updateEvent.updateEvent(new Event(
                            getIntent().getIntExtra(EXTRA_ID, 0),
                            getIntent().getIntExtra(EXTRA_ORGANIZER_ID, 0),
                            editTextTitle.getText().toString(),
                            !imgPath.equals("") ? imgPath : null,
                            getIntent().getStringExtra(EXTRA_IMAGE_NAME),
                            0,
                            0
                                            ));
                    setResForParAct();
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

            Log.d("EDIT_EVENT", String.valueOf(getIntent().hasExtra(EXTRA_ID)));
            setTitle("Edit event");
            editTextTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
            time_picker.setText(getIntent().getStringExtra(EXTRA_TIME));
            date_picker.setText(getIntent().getStringExtra(EXTRA_DATE));
            imgPath = getIntent().getStringExtra(EXTRA_IMAGE_URI);
            event_location.setText(getIntent().getStringExtra(EXTRA_LOCATION));
            id = getIntent().getIntExtra(EXTRA_ID, 0);
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
                        imgPath = String.valueOf(uri);
                        urii = uri;
                        previewImg.setImageBitmap(image);
                    }
                }
            });

    private void setResForParAct() {

        Intent data = new Intent();

        data.putExtra(MainActivity.REQUEST_CODE, REQUEST_CODE);
        data.putExtra("changed", true);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        Toast.makeText(this, "Yey!", Toast.LENGTH_SHORT).show();
    }

    private class SaveImgToInternalStorageAsyncTask extends AsyncTask<Void, Void, Void> {
        private Bitmap bitmap;
        private int rand_id;
        private Context context;
        private ProgressDialog dialog = new ProgressDialog(AddEditEventActivity.this);


        private SaveImgToInternalStorageAsyncTask(Bitmap bitmap, int rand_id, Context context) {
            this.bitmap = bitmap;
            this.rand_id = rand_id;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String path = internalStorageHelper.saveToInternalStorage(bitmap, rand_id, context);

            Event event = new Event(new Random().nextInt(),
                    prefs.getInt("UserID", 0),
                    title,
                    path,
                    String.valueOf(id),
                    0,
                    0
            );

            event.setBase64Img(imageEncoder.bitmapToBase64String(bitmap));

            postEventRequest = new PostEventRequest(AddEditEventActivity.this);
            postEventRequest.postEvent(event);

            eventController.onAddButtonClicked(event);

            MainActivity.getWebSocket().send("hi");
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            this.dialog.dismiss();


            setResForParAct();
        }
    }
}
