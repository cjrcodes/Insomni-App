package com.cjrcodes.insomniapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.cjrcodes.insomniapp.R;
import com.cjrcodes.insomniapp.models.TimeTask;
import com.cjrcodes.insomniapp.views.TimeTaskAdapter;

import java.util.ArrayList;

public class MainActivity extends WearableActivity {

   ArrayList<TimeTask> timeTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lookup the recyclerview in activity layout
        WearableRecyclerView rvTimeTasks = findViewById(R.id.recycler_view_TimeTasks);

        rvTimeTasks.setEdgeItemsCenteringEnabled(true);


        rvTimeTasks.setLayoutManager(
                new WearableLinearLayoutManager(this));

        // Initialize TimeTask
        timeTasks = TimeTask.createTimeTaskList(10);
        // Create adapter passing in the sample user data
        TimeTaskAdapter adapter = new TimeTaskAdapter(timeTasks);
        // Attach the adapter to the recyclerview to populate items
        rvTimeTasks.setAdapter(adapter);
        // Set layout manager to position the items
        rvTimeTasks.setLayoutManager(new LinearLayoutManager(this));




        // Enables Always-on
        setAmbientEnabled();

    }

    private void openCreateTimeTaskActivity() {
        Intent intent = new Intent(this, CreateTimeTaskActivity.class);
        startActivity(intent);
    }


}