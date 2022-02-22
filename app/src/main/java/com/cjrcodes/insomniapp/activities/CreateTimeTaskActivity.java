package com.cjrcodes.insomniapp.activities;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cjrcodes.insomniapp.R;
import com.cjrcodes.insomniapp.models.AlarmType;
import com.cjrcodes.insomniapp.models.TimeTask;



import static com.cjrcodes.insomniapp.models.AlarmType.CLOCK_TIME;
import static com.cjrcodes.insomniapp.models.HeartRateMeasurementType.AVERAGE;

public class CreateTimeTaskActivity extends WearableActivity {

    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_time_task);

        ScrollView sc = findViewById(R.id.scroll_view_create_time_task);

        sc.requestFocus();

        TimeTask tt = new TimeTask();

        Button alarmTypeButton = findViewById(R.id.button_alarm_type);
        alarmTypeButton.setText(tt.getAlarmType().equals(CLOCK_TIME) ? getString(R.string.alarm_type_clock) : getString(R.string.alarm_type_elapsed));

        Button timeSelectButton = findViewById(R.id.button_time_select);
        timeSelectButton.setText(
                tt.getAlarmType().equals(AlarmType.CLOCK_TIME) ? tt.convertToLocalISOTime() : tt.getTime().toString());

        Button hrMeasurementButton = findViewById(R.id.button_hr_measurement);
        hrMeasurementButton.setText(tt.getHrMeasureType().equals(AVERAGE) ? getString(R.string.hr_measurement_type_average) : getString(R.string.hr_measurement_type_current));
        // Enables Always-on
        setAmbientEnabled();
    }
}