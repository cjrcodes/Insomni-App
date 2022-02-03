package com.example.insomniapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private Button addTimeTaskButton;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);




        // Enables Always-on
        setAmbientEnabled();

        addTimeTaskButton = (Button) findViewById(R.id.addTimeTask);
        addTimeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openCreateTimeTaskActivity();
            }
        });
    }

    private void openCreateTimeTaskActivity() {
        Intent intent = new Intent(this, CreateTimeTaskActivity.class);
        startActivity(intent);
    }


}