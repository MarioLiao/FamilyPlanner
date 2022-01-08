package com.example.familyplanner;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemberDB implements Database<MemberUser>{
    private static boolean isUsernameExist = false;

    public MemberDB() {

    }

    public void add(MemberUser member) {
        Database.ref.child("members").child(member.getUsername()).setValue(member);
    }

}