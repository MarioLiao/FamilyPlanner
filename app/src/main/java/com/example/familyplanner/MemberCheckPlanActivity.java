package com.example.familyplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.Date;

public class MemberCheckPlanActivity extends AppCompatActivity {

    public static boolean passed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_check_plan);

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);

        TextView planIdTitle = (TextView) findViewById(R.id.planIdTitle);
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);

        ValueEventListener post_listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planIdTitle.setText(dataSnapshot.child("members").child(member_username).child("planId").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.ref.addListenerForSingleValueEvent(post_listener);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                try {
                    calendar.setDate(Slot.dateFormatter.parse(day + "/" + (month + 1) + "/" + year).getTime());
                    passed = true;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String selected_date = Slot.dateFormatter.format(new Date(calendar.getDate()));
                String[] selected_date_split = selected_date.split("/");
                String new_day = selected_date_split[0];
                String new_month = selected_date_split[1];
                String new_year = selected_date_split[2];

                Intent pass_intent = new Intent(MemberCheckPlanActivity.this, MemberCheckPlanInfoActivity.class);
                pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
                pass_intent.putExtra(MainActivity.DATE_INTENT, new_month + "-" + new_day + "-" + new_year);
                startActivity(pass_intent);
            }
        });

        if (passed) {
            String selected_date = Slot.dateFormatter.format(new Date(calendar.getDate()));
            String[] selected_date_split = selected_date.split("/");
            String day = selected_date_split[0];
            String month = selected_date_split[1];
            String year = selected_date_split[2];

            Intent pass_intent = new Intent(MemberCheckPlanActivity.this, MemberCheckPlanInfoActivity.class);
            pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
            pass_intent.putExtra(MainActivity.DATE_INTENT, month + "-" + day + "-" + year);
            startActivity(pass_intent);
        }
    }

}