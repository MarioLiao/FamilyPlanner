package com.example.familyplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;

public class MemberCheckPlanInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_check_plan_info);

        TextView textView = (TextView) findViewById(R.id.dateTitle);

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);
        String date = received_intent.getStringExtra(MainActivity.DATE_INTENT);

        textView.setText(date);

        ListView list_view_time_slots = (ListView) findViewById(R.id.listViewTimeSlot);
        ArrayList<String> time_slots = new ArrayList<String>();

        ValueEventListener post_listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String plan_id = dataSnapshot.child("members").child(member_username).child("planId").getValue(String.class);

                if (dataSnapshot.child("plans").child(plan_id).hasChild(date)) {

                    for (DataSnapshot child: dataSnapshot.child("plans").child(plan_id).child(date).getChildren()) {

                        String time = child.getKey();
                        String description = dataSnapshot.child("plans").child(plan_id).child(date).child(time).child("description").getValue(String.class);

                        time_slots.add(time + ", " + description);

                        Slot slot = new TimeSlot();
                        try {
                            slot.updateSlot(date, time, plan_id);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(MemberCheckPlanInfoActivity.this, android.R.layout.simple_list_item_1, time_slots) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                            View view = super.getView(position, convertView, parent);
                            String time = time_slots.get(position).substring(0, 5);
                            boolean isCompleted = (boolean) dataSnapshot.child("plans").child(plan_id).child(date).child(time).child("completion").getValue(boolean.class);
                            boolean isMissing = (boolean) dataSnapshot.child("plans").child(plan_id).child(date).child(time).child("missing").getValue(boolean.class);

                            if (isCompleted) {
                                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                            }
                            else if (isMissing) {
                                view.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            }
                            else {
                                view.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                            }
                            return view;
                        }
                    };

                    list_view_time_slots.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.ref.addListenerForSingleValueEvent(post_listener);

        list_view_time_slots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent pass_intent = new Intent(MemberCheckPlanInfoActivity.this, MemberCheckTimeSlotActivity.class);
                pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
                pass_intent.putExtra(MainActivity.DATE_INTENT, date);
                pass_intent.putExtra(MainActivity.DETAIL_INTENT, time_slots.get(i));
                startActivity(pass_intent);
            }
        });

    }

    public void addOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);
        String date = received_intent.getStringExtra(MainActivity.DATE_INTENT);

        Intent pass_intent = new Intent(MemberCheckPlanInfoActivity.this, MemberAddTimeSlotActivity.class);
        pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
        pass_intent.putExtra(MainActivity.DATE_INTENT, date);
        startActivity(pass_intent);
    }

    public void logOutOnClick(View view) {

        Intent pass_intent = new Intent(MemberCheckPlanInfoActivity.this, MainActivity.class);
        startActivity(pass_intent);
    }

    public void calendarOnClick(View view) {

        MemberCheckPlanActivity.passed = false;

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);

        Intent pass_intent = new Intent(MemberCheckPlanInfoActivity.this, MemberCheckPlanActivity.class);
        pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
        startActivity(pass_intent);
    }

    public void refreshOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);
        String date = received_intent.getStringExtra(MainActivity.DATE_INTENT);

        Intent pass_intent = new Intent(MemberCheckPlanInfoActivity.this, MemberCheckPlanInfoActivity.class);
        pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
        pass_intent.putExtra(MainActivity.DATE_INTENT, date);
        startActivity(pass_intent);
    }
}