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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static final String APP_NAME = "FamilyPlanner";
    public static final String USERNAME_INTENT = "usernamepass";
    public static final String DATE_INTENT = "datepass";
    public static final String DETAIL_INTENT = "detailpass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(APP_NAME);
    }

    public void logInOnClick(View view) {
        EditText edit_text_username = (EditText) findViewById(R.id.editTextEnterUsername);
        String username_received = edit_text_username.getText().toString();

        EditText edit_text_password = (EditText) findViewById(R.id.editTextEnterPassword);
        String password_received = edit_text_password.getText().toString();

        ValueEventListener post_listener  = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean username_found = false;
                boolean user_found = false;

                if (dataSnapshot.hasChild("members")) {
                    for (DataSnapshot child : dataSnapshot.child("members").getChildren()) {

                        String current_username = child.getKey();
                        if (current_username.equals(username_received)) {
                            username_found = true;

                            String current_password = dataSnapshot.child("members").child(current_username).child("password").getValue().toString();
                            if (current_password.equals(password_received)) {
                                user_found = true;
                            }
                        }
                    }
                }

                if (!username_found) {
                    Toast.makeText(MainActivity.this,"Try Again",Toast.LENGTH_LONG).show();
                }

                else if (username_found && !user_found) {
                    Toast.makeText(MainActivity.this,"Try Again",Toast.LENGTH_LONG).show();
                }

                else {
                    Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                    Intent pass_intent = new Intent(MainActivity.this, MemberMainActivity.class);
                    pass_intent.putExtra(USERNAME_INTENT, username_received);
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

    public void signUpOnClick(View view) {

        Intent pass_intent = new Intent(MainActivity.this, MemberSignUpActivity.class);
        startActivity(pass_intent);
    }
}