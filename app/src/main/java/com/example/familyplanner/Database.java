package com.example.familyplanner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public interface Database<G>{
    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public abstract void add(G g);
}