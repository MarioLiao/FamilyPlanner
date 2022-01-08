package com.example.familyplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MemberCheckTimeSlotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_check_time_slot);

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);
        String date = received_intent.getStringExtra(MainActivity.DATE_INTENT);
        String detail = received_intent.getStringExtra(MainActivity.DETAIL_INTENT);
        String time = detail.substring(0,5);
        String description = detail.substring(7);

        TextView date_title = (TextView) findViewById(R.id.dateTitle2);
        TextView time_detail = (TextView) findViewById(R.id.timeDetail);
        TextView description_detail = (TextView) findViewById(R.id.descriptionDetail);

        date_title.setText(date);
        time_detail.setText("-" + time + "-");
        description_detail.setText(description);

        ValueEventListener post_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String plan_id = dataSnapshot.child("members").child(member_username).child("planId").getValue().toString();
                String added_by = dataSnapshot.child("plans").child(plan_id).child(date).child(time).child("added_by").getValue().toString();
                String completed_by = dataSnapshot.child("plans").child(plan_id).child(date).child(time).child("completed_by").getValue().toString();
                boolean completion = (boolean) dataSnapshot.child("plans").child(plan_id).child(date).child(time).child("completion").getValue();

                TextView added_by_user = (TextView) findViewById(R.id.addedByUser);
                TextView completed_by_user = (TextView) findViewById(R.id.completedByUser);

                added_by_user.setText(added_by);
                completed_by_user.setText(completed_by);

                if (!completion) {
                    RadioButton incomplete_button = (RadioButton) findViewById(R.id.incompleteButton);
                    incomplete_button.setChecked(true);
                }

                else {
                    RadioButton completed_button = (RadioButton) findViewById(R.id.completedButton);
                    completed_button.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.ref.addListenerForSingleValueEvent(post_listener);
    }

    public void applyOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);
        String date = received_intent.getStringExtra(MainActivity.DATE_INTENT);
        String detail = received_intent.getStringExtra(MainActivity.DETAIL_INTENT);
        String time = detail.substring(0,5);

        RadioGroup completion_group =(RadioGroup) findViewById(R.id.completionGroup);
        int checked_button_id = completion_group.getCheckedRadioButtonId();

        RadioButton checked_button = (RadioButton) findViewById(checked_button_id);

        ValueEventListener post_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String plan_id = dataSnapshot.child("members").child(member_username).child("planId").getValue().toString();

                if (checked_button.getText().toString().equals("Completed")) {
                    Database.ref.child("plans").child(plan_id).child(date).child(time).child("completion").setValue(true);
                    Database.ref.child("plans").child(plan_id).child(date).child(time).child("completed_by").setValue(member_username);
                }

                else {
                    Database.ref.child("plans").child(plan_id).child(date).child(time).child("completion").setValue(false);
                    Database.ref.child("plans").child(plan_id).child(date).child(time).child("completed_by").setValue("None");
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

        Intent pass_intent = new Intent(this, MemberCheckPlanInfoActivity.class);
        pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
        pass_intent.putExtra(MainActivity.DATE_INTENT, date);
        startActivity(pass_intent);
    }
}