package com.example.rig.models;

import java.util.ArrayList;

public class Meeting {
    private String id, description, link_meeting, meeting_id, meeting_password,time;
    private ArrayList<String> roles;

    public Meeting(){

    }

    public Meeting(String id, String description, String link_meeting, String meeting_id, String meeting_password, String time, ArrayList<String> roles) {
        this.id = id;
        this.description = description;
        this.link_meeting = link_meeting;
        this.meeting_id = meeting_id;
        this.meeting_password = meeting_password;
        this.time = time;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink_meeting() {
        return link_meeting;
    }

    public void setLink_meeting(String link_meeting) {
        this.link_meeting = link_meeting;
    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getMeeting_password() {
        return meeting_password;
    }

    public void setMeeting_password(String meeting_password) {
        this.meeting_password = meeting_password;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }
}
