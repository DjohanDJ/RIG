package com.example.rig.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rig.R;
import com.example.rig.models.Meeting;

public class UpdateMeetingActivity extends AppCompatActivity {

    EditText desc, meet_id, meet_pass, link_zoom;
    TextView time;
    CheckBox astBox, spvBox, naBox, subcoBox;
    Meeting meeting = new Meeting();

    void initVariable(){
        desc = findViewById(R.id.up_meet_desc);
        meet_id = findViewById(R.id.up_meet_id);
        meet_pass = findViewById(R.id.up_meet_password);
        link_zoom = findViewById(R.id.up_meet_link_zoom);
        time = findViewById(R.id.up_meeting_schedule);
        astBox = findViewById(R.id.up_ast2);
        spvBox = findViewById(R.id.up_spv2);
        naBox = findViewById(R.id.up_na2);
        subcoBox = findViewById(R.id.up_subco2);
    }

    void setMeeting(){
        Intent intent = getIntent();
        meeting.setId(intent.getStringExtra("id"));
        meeting.setDescription(intent.getStringExtra("description"));
        meeting.setMeeting_id(intent.getStringExtra("meeting_id"));
        meeting.setMeeting_password(intent.getStringExtra("meeting_pass"));
        meeting.setTime(intent.getStringExtra("time"));
        meeting.setLink_meeting(intent.getStringExtra("link_zoom"));
        meeting.setRoles(intent.getStringArrayListExtra("roles"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meeting);
        initVariable();
        setMeeting();

        desc.setText(meeting.getDescription());
        meet_id.setText(meeting.getMeeting_id());
        meet_pass.setText(meeting.getMeeting_password());
        link_zoom.setText(meeting.getLink_meeting());
        time.setText(meeting.getTime());

        for (String role : meeting.getRoles()){
            if(role.equals("Assistant")){
                astBox.setChecked(true);
            }else if(role.equals("Supervisor")){
                spvBox.setChecked(true);
            }else if(role.equals("Network Administrator")){
                naBox.setChecked(true);
            }else if(role.equals("Subject Coordinator")){
                subcoBox.setChecked(true);
            }
        }


    }


}