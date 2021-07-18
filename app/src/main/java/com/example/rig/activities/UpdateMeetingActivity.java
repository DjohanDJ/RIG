package com.example.rig.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rig.R;
import com.example.rig.animations.LoadingAnimation;
import com.example.rig.authentication.SingletonFirebaseTool;
import com.example.rig.authentication.UserSession;
import com.example.rig.models.Meeting;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpdateMeetingActivity extends AppCompatActivity {

    EditText desc, meet_id, meet_pass, link_zoom, timeHour;
    TextView time;
    CheckBox astBox, spvBox, naBox, subcoBox;
    Meeting meeting = new Meeting();
    private Button updateMeeting, changeDate;
    DatePickerDialog.OnDateSetListener setListener;

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
        updateMeeting = findViewById(R.id.update_meeting_button);
        changeDate = findViewById(R.id.openDatePicker);
        timeHour = findViewById(R.id.timeHour);
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
        String[] arrSplit = meeting.getTime().split(" ");
        time.setText(arrSplit[0]);
        timeHour.setText(arrSplit[1]);

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

        doButtonListener();

    }

    private void doButtonListener() {
        updateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateMeetingActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = dayOfMonth + "-" + month + "-" + year;
                Date pDate = null;
                SimpleDateFormat dFormat = new SimpleDateFormat("dd-mm-yyyy");
                try {
                    pDate = new SimpleDateFormat("dd-mm-yyyy").parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                time.setText(dFormat.format(pDate));
            }
        };

    }

    void checkValidation() {
        String description = desc.getText().toString();
        String meetId = meet_id.getText().toString();
        String meetPass = meet_pass.getText().toString();
        String datePick = time.getText().toString();
        String timeH = timeHour.getText().toString();
        String link = link_zoom.getText().toString();


        if (description.trim().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.desc), Toast.LENGTH_SHORT).show();
        } else if (meetId.trim().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.meetId), Toast.LENGTH_SHORT).show();
        } else if (meetPass.trim().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.meetPass), Toast.LENGTH_SHORT).show();
        } else if (datePick.trim().equals("Pick your date")) {
            Toast.makeText(this, getResources().getString(R.string.pickdate), Toast.LENGTH_SHORT).show();
        } else if (timeH.trim().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.timeH), Toast.LENGTH_SHORT).show();
        } else if (link.trim().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.link), Toast.LENGTH_SHORT).show();
        } else if (!astBox.isChecked() && !spvBox.isChecked() && !naBox.isChecked() && !subcoBox.isChecked()) {
            Toast.makeText(this, getResources().getString(R.string.checkAst), Toast.LENGTH_SHORT).show();
        } else {
            LoadingAnimation.startLoading(UpdateMeetingActivity.this);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            ArrayList<String> updatedRole = new ArrayList<>();
            updatedRole.clear();

            if(astBox.isChecked()){
                updatedRole.add("Assistant");
            }
            if(spvBox.isChecked()){
                updatedRole.add("Supervisor");
            }
            if(naBox.isChecked()){
                updatedRole.add("Network Administrator");
            }
            if(subcoBox.isChecked()){
                updatedRole.add("Subject Coordinator");
            }

            Meeting updatedMeeting = new Meeting(meeting.getId(), desc.getText().toString(), link_zoom.getText().toString(),
                    meet_id.getText().toString(), meet_pass.getText().toString(), time.getText().toString() + " " + timeHour.getText().toString(),
                    dateFormat.format(date), updatedRole);
            SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("meetings")
                    .document(meeting.getId())
                    .set(updatedMeeting)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UpdateMeetingActivity.this, getResources().getString(R.string.meeting_updated), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
    }


}