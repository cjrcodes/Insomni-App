package com.cjrcodes.insomniapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

import com.cjrcodes.insomniapp.R;
import com.cjrcodes.insomniapp.models.TimeTask;

import java.util.ArrayList;
import java.util.List;

public class TimeTaskAdapter extends RecyclerView.Adapter<TimeTaskAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<TimeTask> mTimeTasks = new ArrayList<TimeTask>();
    private Context mContext;

    // Pass in the contact array into the constructor
    public TimeTaskAdapter(List<TimeTask> timeTasks) {
        mTimeTasks = timeTasks;
    }

    public TimeTaskAdapter(Context context, ArrayList<TimeTask> timeTasks) {
        mTimeTasks = timeTasks;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public Button editButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.button_time_task);
            //editButton = (Button) itemView.findViewById(R.id.edit_button);
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TimeTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View timeTaskView = inflater.inflate(R.layout.item_time_task, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(timeTaskView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TimeTaskAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        TimeTask timeTask = mTimeTasks.get(position);
        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        //timeTask.convertMillisecondsToHourMinuteSecond(timeTask.getTime()) + " " + timeTask.getMaxHeartRate()
        textView.setText("Time Task Placeholder");
        //Button button = holder.editButton;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTimeTasks.size();
    }
}
