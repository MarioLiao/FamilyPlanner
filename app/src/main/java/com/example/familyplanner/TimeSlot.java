package com.example.familyplanner;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.text.ParseException;

public class TimeSlot implements Slot{

    String date;
    String time;
    String description;
    boolean completion;
    boolean missing;
    String added_by;
    String completed_by;

    public TimeSlot() {

    }

    public TimeSlot(String date, String time, String description, String added_by) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.added_by = added_by;
        this.completed_by = "None";
        this.completion = false;
        this.missing = false;
    }

    @Override
    public void updateSlot(String date, String time, String plan_id) throws ParseException {
        Date current_date = new Date(System.currentTimeMillis());
        Date given_date = Slot.dateParser.parse(date + " " + time + ":00");

        ValueEventListener post_listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean isCompleted = (boolean) dataSnapshot.child("plans").child(plan_id).child(date).child(time).child("completion").getValue(boolean.class);
                if (current_date.after(given_date) && !isCompleted) {
                    Database.ref.child("plans").child(plan_id).child(date).child(time).child("missing").setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.ref.addListenerForSingleValueEvent(post_listener);

    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompletion() {
        return completion;
    }

    @Override
    public void setCompletion(boolean completion) {
        this.completion = completion;
    }

    @Override
    public boolean isMissing() {
        return missing;
    }

    @Override
    public void setMissing(boolean missing) {
        this.missing = missing;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getCompleted_by() {
        return completed_by;
    }

    public void setCompleted_by(String completed_by) {
        this.completed_by = completed_by;
    }
}
