package com.example.familyplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MemberAddTimeSlotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_add_time_slot);
    }

    public void addTimeSlotOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);
        String date = received_intent.getStringExtra(MainActivity.DATE_INTENT);

        ValueEventListener post_listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String plan_id = dataSnapshot.child("members").child(member_username).child("planId").getValue().toString();

                EditText edit_time = (EditText) findViewById(R.id.editTextEnterTime);
                EditText edit_description = (EditText) findViewById(R.id.editTextEnterDescription);

                String time = edit_time.getText().toString();
                String description = edit_description.getText().toString();

                Pattern time_pattern = Pattern.compile("(([01][0-9])|(2[0-3])):[0-5][0-9]");
                Matcher time_matcher = time_pattern.matcher(time);
                Pattern description_pattern = Pattern.compile(".{1,25}");
                Matcher description_matcher = description_pattern.matcher(description);

                boolean valid_time = time_matcher.matches();
                boolean valid_description = description_matcher.matches();

                if (!valid_time) {
                    Toast.makeText(MemberAddTimeSlotActivity.this,"Invalid Time (Must be in between 00:00 - 23:59)",Toast.LENGTH_LONG).show();
                }

                else if (!valid_description) {
                    Toast.makeText(MemberAddTimeSlotActivity.this,"Invalid Description (Must be 1-25 characters)",Toast.LENGTH_LONG).show();
                }

                else {
                    Slot timeSlot = new TimeSlot(date, time, description, member_username);
                    Database.ref.child("plans").child(plan_id).child(date).child(time).setValue(timeSlot);

                    Toast.makeText(MemberAddTimeSlotActivity.this,"Time Slot Created Successfully",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());

            }
        };
        Database.ref.addListenerForSingleValueEvent(post_listener);
    }

    public void exitOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);
        String date = received_intent.getStringExtra(MainActivity.DATE_INTENT);

        Intent pass_intent = new Intent(MemberAddTimeSlotActivity.this, MemberCheckPlanInfoActivity.class);
        pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
        pass_intent.putExtra(MainActivity.DATE_INTENT, date);
        startActivity(pass_intent);
    }
}