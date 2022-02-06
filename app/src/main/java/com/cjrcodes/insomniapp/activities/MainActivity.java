package com.cjrcodes.insomniapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.Button;
import android.widget.TextView;

import com.cjrcodes.insomniapp.R;
import com.cjrcodes.insomniapp.TimeTask;

public class MainActivity extends WearableActivity {

    private Button addTimeTaskButton;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);


        TimeTask tt = new TimeTask();


        // Enables Always-on
        setAmbientEnabled();

    }

    private void openCreateTimeTaskActivity() {
        Intent intent = new Intent(this, CreateTimeTaskActivity.class);
        startActivity(intent);
    }


}