package com.example.familyplanner;

public abstract class MemberUser extends User{
    String name, password, gender, username, date_of_birth;
    String plan_id;

    public abstract String getPlan_id();

    public abstract void setPlan_id(String plan_id);
}