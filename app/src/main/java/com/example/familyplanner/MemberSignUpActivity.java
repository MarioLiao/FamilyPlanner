package com.example.familyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_sign_up);
        setTitle(MainActivity.APP_NAME);
    }

    public void createAccountOnClick(View view) {

        ValueEventListener post_listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EditText edit_full_name = (EditText) findViewById(R.id.editTextFullName);
                EditText edit_username = (EditText) findViewById(R.id.editTextCreateAUsername);
                EditText edit_password = (EditText) findViewById(R.id.editTextCreateAPassword);
                EditText edit_birth_date = (EditText) findViewById(R.id.editTextCreateBirthDate);
                RadioGroup gender_group = (RadioGroup) findViewById(R.id.completionGroup);
                int gender_group_id = gender_group.getCheckedRadioButtonId();
                RadioButton gender_chosen = (RadioButton) findViewById(gender_group_id);

                String name = edit_full_name.getText().toString();
                String username = edit_username.getText().toString();
                String password = edit_password.getText().toString();
                String birth_date = edit_birth_date.getText().toString();
                String gender = gender_chosen.getText().toString();

                Pattern name_pattern = Pattern.compile("[a-z[A-Z]]+ [a-z[A-Z]]+");
                Matcher name_matcher = name_pattern.matcher(name);
                Pattern username_pattern = Pattern.compile("[\\w]{5,18}");
                Matcher username_matcher = username_pattern.matcher(username);
                Pattern password_pattern = Pattern.compile("[\\w]{5,22}");
                Matcher password_matcher = password_pattern.matcher(password);
                Pattern birth_date_pattern = Pattern.compile("((0[1-9])|(1[0-2]))/((0[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))/((19[\\d]{2})|(20[\\d]{2}))");
                Matcher birth_date_matcher = birth_date_pattern.matcher(birth_date);

                boolean valid_full_name = name_matcher.matches();
                boolean valid_username = username_matcher.matches();
                boolean valid_password = password_matcher.matches();
                boolean valid_birth_date = birth_date_matcher.matches();

                boolean is_username_taken = false;

                if (dataSnapshot.hasChild("members")) {
                    for (DataSnapshot child : dataSnapshot.child("members").getChildren()) {

                        String current_username = child.getKey();
                        if (current_username.equals(username)) {
                            is_username_taken = true;
                        }
                    }
                }

                if (is_username_taken) {
                    Toast.makeText(MemberSignUpActivity.this,"Username Is Already Taken",Toast.LENGTH_LONG).show();
                }

                else if (!valid_full_name) {
                    Toast.makeText(MemberSignUpActivity.this,"Invalid Full Name (Must be in form of [first_name last_name])",Toast.LENGTH_LONG).show();
                }

                else if (!valid_username) {
                    Toast.makeText(MemberSignUpActivity.this,"Invalid Username (Must be 5-18 word characters)",Toast.LENGTH_LONG).show();
                }

                else if (!valid_password) {
                    Toast.makeText(MemberSignUpActivity.this,"Invalid Password (Must be 5-22 word characters)",Toast.LENGTH_LONG).show();
                }

                else if (!valid_birth_date) {
                    Toast.makeText(MemberSignUpActivity.this,"Invalid Birth Date (Must be in form of [MM/DD/YYYY])",Toast.LENGTH_LONG).show();
                }

                else {
                    Database database = new MemberDB();
                    MemberUser member_to_add = new Member(name, password, gender, username, birth_date);
                    database.add(member_to_add);

                    Toast.makeText(MemberSignUpActivity.this,"Account Created Successfully",Toast.LENGTH_LONG).show();

                    Intent pass_intent = new Intent(MemberSignUpActivity.this, MainActivity.class);
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