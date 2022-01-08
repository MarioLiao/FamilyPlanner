package com.example.familyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberCreatePlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_create_plan);
        setTitle(MainActivity.APP_NAME);
    }

    public void enterOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);

        EditText edit_create_plan_id = (EditText) findViewById(R.id.editTextCreatePlanId);
        String plan_id_received = edit_create_plan_id.getText().toString();

        Pattern plan_id_pattern = Pattern.compile("[\\w]{2,12}");
        Matcher plan_id_matcher = plan_id_pattern.matcher(plan_id_received);

        if (!plan_id_matcher.matches()) {
            Toast.makeText(MemberCreatePlanActivity.this,"Plan ID must be between 2-12 word characters",Toast.LENGTH_LONG).show();
        }

        else {
            ValueEventListener post_listener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    boolean is_plan_id_taken = false;

                    if (dataSnapshot.hasChild("plans")) {

                        for (DataSnapshot child: dataSnapshot.child("plans").getChildren()) {

                            String current_plan_id = child.getKey();
                            if (current_plan_id.equals(plan_id_received)) {
                                is_plan_id_taken = true;
                            }
                        }
                    }

                    if (is_plan_id_taken) {
                        Toast.makeText(MemberCreatePlanActivity.this,"Plan ID is already taken",Toast.LENGTH_LONG).show();
                    }

                    else {
                        Database.ref.child("plans").child(plan_id_received).child("id").setValue(plan_id_received);
                        Database.ref.child("members").child(member_username).child("planId").setValue(plan_id_received);
                        Toast.makeText(MemberCreatePlanActivity.this,"Plan ID created and added successfully",Toast.LENGTH_LONG).show();

                        Intent pass_intent = new Intent(MemberCreatePlanActivity.this, MemberMainActivity.class);
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

    }

    public void exitOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);

        Intent pass_intent = new Intent(MemberCreatePlanActivity.this, MemberMainActivity.class);
        pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
        startActivity(pass_intent);
    }
}