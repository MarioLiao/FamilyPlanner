package com.example.familyplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MemberMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_main);
        setTitle(MainActivity.APP_NAME);

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);

        TextView member_display = findViewById(R.id.displayUsername);
        member_display.setText(member_username);
    }

    public void createPlanOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);

        ValueEventListener post_listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("members").child(member_username).hasChild("planId")) {
                    Toast.makeText(MemberMainActivity.this,"Please leave your already existing plan to create a plan",Toast.LENGTH_LONG).show();
                }

                else {
                    Intent pass_intent = new Intent(MemberMainActivity.this, MemberCreatePlanActivity.class);
                    pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
                    startActivity(pass_intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.ref.addListenerForSingleValueEvent(post_listener);
    }

    public void joinPlanOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);

        ValueEventListener post_listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("members").child(member_username).hasChild("planId")) {
                    Toast.makeText(MemberMainActivity.this,"Please leave your already existing plan to join another plan",Toast.LENGTH_LONG).show();
                }

                else {
                    Intent pass_intent = new Intent(MemberMainActivity.this, MemberJoinAnotherPlanActivity.class);
                    pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
                    startActivity(pass_intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.ref.addListenerForSingleValueEvent(post_listener);
    }

    public void logOutOnClick(View view) {

        Intent pass_intent = new Intent(MemberMainActivity.this, MainActivity.class);
        startActivity(pass_intent);
    }

    public void checkPlanOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);

        ValueEventListener post_listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("members").child(member_username).hasChild("planId")) {
                    Toast.makeText(MemberMainActivity.this,"Checking Plan",Toast.LENGTH_LONG).show();

                    Intent pass_intent = new Intent(MemberMainActivity.this, MemberCheckPlanActivity.class);
                    pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
                    startActivity(pass_intent);
                }

                else {
                    Toast.makeText(MemberMainActivity.this,"You are currently not in a plan",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.ref.addListenerForSingleValueEvent(post_listener);
    }

    public void leavePlanOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);

        ValueEventListener post_listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("members").child(member_username).hasChild("planId")) {
                    dataSnapshot.child("members").child(member_username).child("planId").getRef().removeValue();
                    Toast.makeText(MemberMainActivity.this,"Plan left successfully",Toast.LENGTH_LONG).show();
                }

                else {
                    Toast.makeText(MemberMainActivity.this,"You are currently not in a plan",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.ref.addListenerForSingleValueEvent(post_listener);
    }
}