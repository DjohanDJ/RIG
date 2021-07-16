package com.example.rig.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.rig.R;
import com.example.rig.adapter.AllMeetingAdapter;
import com.example.rig.authentication.SingletonFirebaseTool;
import com.example.rig.models.Meeting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class ViewAllMeetingActivity extends AppCompatActivity {

    private RecyclerView allMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_meeting);

        allMeeting = findViewById(R.id.allMeetingNonAst);

        final ArrayList<Meeting> meetingList = new ArrayList<>();

        final Context ctx = this;

        SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("meetings").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            meetingList.clear();
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                Meeting meeting = documentSnapshot.toObject(Meeting.class);
                                meetingList.add(meeting);
                            }

                            AllMeetingAdapter comAdapter = new AllMeetingAdapter(ctx, meetingList);
                            allMeeting.setAdapter(comAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            allMeeting.setLayoutManager(layoutManager);
                        }
                    }
        });
    }
}