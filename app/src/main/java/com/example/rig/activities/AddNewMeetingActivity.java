package com.example.rig.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import com.example.rig.models.Meeting;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddNewMeetingActivity extends AppCompatActivity {

    EditText desc, meet_id, meet_pass, link_zoom, timeHour;
    TextView time;
    CheckBox astBox, spvBox, naBox, subcoBox;
    private Button addMeeting, changeDate;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meeting);
        initializeVariable();
        buttonListener();
    }

    private void buttonListener() {

        addMeeting.setOnClickListener(new View.OnClickListener() {
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
                        AddNewMeetingActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
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
            LoadingAnimation.startLoading(AddNewMeetingActivity.this);
            final Date date = new Date();
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            final ArrayList<String> updatedRole = new ArrayList<>();
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

            Meeting meetingTemp = new Meeting();

            SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("meetings")
                    .add(meetingTemp).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Meeting updatedMeeting = new Meeting(documentReference.getId(), desc.getText().toString(), link_zoom.getText().toString(),
                            meet_id.getText().toString(), meet_pass.getText().toString(), time.getText().toString() + " " + timeHour.getText().toString(),
                            dateFormat.format(date), updatedRole);

                    SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("meetings")
                            .document(updatedMeeting.getId())
                            .set(updatedMeeting)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddNewMeetingActivity.this, getResources().getString(R.string.meeting_added), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }
            });
        }
    }

    void initializeVariable(){
        desc = findViewById(R.id.add_meet_desc);
        meet_id = findViewById(R.id.add_meet_id);
        meet_pass = findViewById(R.id.add_meet_password);
        link_zoom = findViewById(R.id.add_meet_link_zoom);
        time = findViewById(R.id.add_meeting_schedule);
        astBox = findViewById(R.id.up_ast);
        spvBox = findViewById(R.id.up_spv);
        naBox = findViewById(R.id.up_na);
        subcoBox = findViewById(R.id.up_subco);
        addMeeting = findViewById(R.id.add_meeting);
        changeDate = findViewById(R.id.openDatePicker);
        timeHour = findViewById(R.id.timeHour);
    }
}