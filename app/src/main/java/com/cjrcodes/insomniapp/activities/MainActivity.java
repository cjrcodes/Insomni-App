package com.cjrcodes.insomniapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.ViewPager2;
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
        final WearableRecyclerView rvTimeTasks = findViewById(R.id.recycler_view_TimeTasks);
        rvTimeTasks.setEdgeItemsCenteringEnabled(true);
        rvTimeTasks.setCircularScrollingGestureEnabled(true);
        CustomScrollingLayoutCallback cslc = new CustomScrollingLayoutCallback();
        ViewPager2 vp = new ViewPager2(this);

        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(rvTimeTasks);

        rvTimeTasks.setLayoutManager(new WearableLinearLayoutManager(this, cslc));


        // Initialize TimeTask
        timeTasks = TimeTask.createTimeTaskList(10);
        // Create adapter passing in the sample user data
        final TimeTaskAdapter adapter = new TimeTaskAdapter(this, timeTasks);
        // Attach the adapter to the recyclerview to populate items
        rvTimeTasks.setAdapter(adapter);
        // Set layout manager to position the items


        rvTimeTasks.requestFocus();



        // Enables Always-on
        setAmbientEnabled();

    }

    private void openCreateTimeTaskActivity() {
        Intent intent = new Intent(this, CreateTimeTaskActivity.class);
        startActivity(intent);
    }

    public class CustomScrollingLayoutCallback extends WearableLinearLayoutManager.LayoutCallback {
        /** How much should we scale the icon at most. */
        private static final float MAX_ICON_PROGRESS = 0.65f;

        private float progressToCenter;

        @Override
        public void onLayoutFinished(View child, RecyclerView parent) {

            // Figure out % progress from top to bottom
            float centerOffset = ((float) child.getHeight() / 2.0f) / (float) parent.getHeight();
            float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

            // Normalize for center
            progressToCenter = Math.abs(0.5f - yRelativeToCenterOffset);
            // Adjust to the maximum scale
            progressToCenter = Math.min(progressToCenter, MAX_ICON_PROGRESS);

            child.setScaleX(1 - progressToCenter);
            child.setScaleY(1 - progressToCenter);
        }
    }


}