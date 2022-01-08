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

public class MemberJoinAnotherPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_join_another_plan);
        setTitle(MainActivity.APP_NAME);
    }

    public void enterOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);

        EditText edit_join_plan_id = (EditText) findViewById(R.id.editTextJoinPlanId);
        String plan_id_received = edit_join_plan_id.getText().toString();

        ValueEventListener post_listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean is_plan_id_exist = false;

                if (dataSnapshot.hasChild("plans")) {

                    for (DataSnapshot child : dataSnapshot.child("plans").getChildren()) {

                        String current_plan_id = child.getKey();
                        if (current_plan_id.equals(plan_id_received)) {
                            is_plan_id_exist = true;
                        }
                    }
                }

                if (!is_plan_id_exist) {
                    Toast.makeText(MemberJoinAnotherPlanActivity.this,"Try Again",Toast.LENGTH_LONG).show();
                }

                else {
                    Database.ref.child("members").child(member_username).child("planId").setValue(plan_id_received);
                    Toast.makeText(MemberJoinAnotherPlanActivity.this,"Plan joined successfully",Toast.LENGTH_LONG).show();

                    Intent pass_intent = new Intent(MemberJoinAnotherPlanActivity.this, MemberMainActivity.class);
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

    public void exitOnClick(View view) {

        Intent received_intent = getIntent();
        String member_username = received_intent.getStringExtra(MainActivity.USERNAME_INTENT);

        Intent pass_intent = new Intent(MemberJoinAnotherPlanActivity.this, MemberMainActivity.class);
        pass_intent.putExtra(MainActivity.USERNAME_INTENT, member_username);
        startActivity(pass_intent);
    }
}