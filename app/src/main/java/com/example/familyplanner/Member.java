package com.example.familyplanner;

public class Member extends MemberUser{
    String name, password, gender, username, date_of_birth;
    String plan_id;

    public Member() {

    }

    public Member(String name, String password, String gender, String username, String date_of_birth){
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.username = username;
        this.date_of_birth = date_of_birth;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getDate_of_birth() {
        return date_of_birth;
    }

    @Override
    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    @Override
    public String getPlan_id() {
        return plan_id;
    }

    @Override
    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }
}