package com.cjrcodes.insomniapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.cjrcodes.insomniapp.R;
import com.cjrcodes.insomniapp.models.TimeTask;
import com.cjrcodes.insomniapp.views.TimeTaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends WearableActivity {

    ArrayList<TimeTask> timeTasks;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lookup the recyclerview in activity layout
        final WearableRecyclerView rvTimeTasks = findViewById(R.id.recycler_view_TimeTasks);
        final FloatingActionButton createTimeTaskButton = findViewById(R.id.fab_create_time_task);

        rvTimeTasks.setEdgeItemsCenteringEnabled(true);
        rvTimeTasks.setCircularScrollingGestureEnabled(true);

        CustomScrollingLayoutCallback cslc = new CustomScrollingLayoutCallback();

        final SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(rvTimeTasks);
        rvTimeTasks.setLayoutManager(new WearableLinearLayoutManager(this,
                cslc));

        // Initialize TimeTask
        timeTasks = TimeTask.createTimeTaskList(3);
        // Create adapter passing in the sample user data
        final TimeTaskAdapter adapter = new TimeTaskAdapter(this,
                timeTasks);
        // Attach the adapter to the recyclerview to populate items
        rvTimeTasks.setAdapter(adapter);
        // Set layout manager to position the items

        rvTimeTasks.requestFocus();

        createTimeTaskButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //rvTimeTasks.setVisibility(View.GONE);
                openCreateTimeTaskActivity();
                System.out.println("Create Button Clicked");
            }
        });


        rvTimeTasks.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                openTimeTaskActivity();
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        }
    );


//        rvTimeTasks.addOnItemClickListener(new RecyclerItemClickListener(){
//           /* @Override
//            public void onClick(View view) {
//                //rvTimeTasks.setVisibility(View.GONE);
//                openTimeTaskActivity();
//                System.out.println("Time Task " + view.getTag() + "Clicked");
//            }*/
//        });

        rvTimeTasks.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                System.out.println("Cur " + position);
                if (position > 0) {
                    //rvTimeTasks.setTranslationY(45);
                    createTimeTaskButton.setVisibility(View.GONE);

                } else {
                    //createTimeTaskButton.requestFocus();
                    //rvTimeTasks.setTranslationY(-100);

                    createTimeTaskButton.setVisibility(View.VISIBLE);
                }

            }
        });

        rvTimeTasks.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent ev) {

                if (position > 0 && timeTasks.size() > 1) {
                    createTimeTaskButton.setVisibility(View.GONE);

                } else {
                    createTimeTaskButton.setVisibility(View.VISIBLE);

                }

                if (ev.getAction() == MotionEvent.ACTION_SCROLL &&
                        ev.isFromSource(InputDeviceCompat.SOURCE_ROTARY_ENCODER)) {
                    float delta = -ev.getAxisValue(MotionEventCompat.AXIS_SCROLL) *
                            ViewConfigurationCompat.getScaledVerticalScrollFactor(ViewConfiguration.get(getBaseContext()),
                                    getBaseContext());

                    if (delta < 0) {
                        rvTimeTasks.getLayoutManager().smoothScrollToPosition(rvTimeTasks,
                                new RecyclerView.State(),
                                position > 0 ? --position : 0);
                        System.out.println("ROT " + position);
                    } else {
                        if (position < timeTasks.size() - 1) {

                            rvTimeTasks.getLayoutManager().smoothScrollToPosition(rvTimeTasks,
                                    new RecyclerView.State(),
                                    ++position);
                            System.out.println("ROT " + position);

                        }
                    }

                    return true;
                }

                return false;
            }
        });

        // Enables Always-on
        setAmbientEnabled();

    }

    private void openCreateTimeTaskActivity() {
        Intent intent = new Intent(this,
                CreateTimeTaskActivity.class);
        startActivity(intent);
    }

    private void openTimeTaskActivity() {
        Intent intent = new Intent(this,
                TimeTaskActivity.class);
        startActivity(intent);
    }

    public static class CustomScrollingLayoutCallback extends WearableLinearLayoutManager.LayoutCallback {
        /**
         * How much should we scale the icon at most.
         */
        private static final float MAX_ICON_PROGRESS = 0.65f;

        @Override
        public void onLayoutFinished(View child, RecyclerView parent) {

            // Figure out % progress from top to bottom
            float centerOffset = ((float) child.getHeight() / 2.0f) / (float) parent.getHeight();
            float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

            // Normalize for center
            float progressToCenter = Math.abs(0.5f - yRelativeToCenterOffset);
            // Adjust to the maximum scale
            progressToCenter = Math.min(progressToCenter,
                    MAX_ICON_PROGRESS);

            child.setScaleX(1 - progressToCenter);
            child.setScaleY(1 - progressToCenter);
        }
    }


}