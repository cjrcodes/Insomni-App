package com.cjrcodes.insomniapp.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import com.cjrcodes.insomniapp.R;

import java.util.concurrent.TimeUnit;

public class TimeTaskActivity extends WearableActivity {

    private TextView mTextView;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_task);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();

        //Default set to 15 minutes (900,000 milliseconds)
        this.timer = new CountDownTimer(900000, 1000) {
            @Override
            public void onTick(long l) {
                System.out.println(TimeUnit.MICROSECONDS.toHours(l) + ":" + TimeUnit.MICROSECONDS.toMinutes(l) + ":" + TimeUnit.MILLISECONDS.toSeconds(l));
            }

            @Override
            public void onFinish() {
                System.out.println("Time task completed");
            }
        };
    }
}