package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class AddEditEventActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.example.myfirstapp.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.myfirstapp.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.example.myfirstapp.EXTRA_DESCRIPTION";

    public static int REQUEST_CODE = 0;


    private EditText editTextTitle;
    private EditText editTextDescription;
    private View pickImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        pickImgBtn = findViewById(R.id.pick_img_btn);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_close);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        if (getIntent().hasExtra(EXTRA_ID)) {
            setTitle("Edit event");
            editTextTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
            REQUEST_CODE = HomeFragment.EDIT_EVENT_REQUEST;
        } else {
            REQUEST_CODE = HomeFragment.ADD_EVENT_REQUEST;
            setTitle("Create an event");
        }

    }

    private void saveEvent() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(HomeFragment.REQUEST_CODE, REQUEST_CODE);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();


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
                saveEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}