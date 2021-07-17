package com.example.rig.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rig.R;
import com.example.rig.adapter.AllMeetingAdapter;
import com.example.rig.authentication.SingletonFirebaseTool;
import com.example.rig.authentication.UserSession;
import com.example.rig.models.Meeting;
import com.example.rig.notification.NotificationBroadcast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private static TextView name;
    private Button AllMeetingNonAst, AllMeetingAst, create, ban, grant, editProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Context ctx = this;
        doInitializeItems();
        setWelcomeText();
        setButtonOnClick(this);
        createNotifChannel();
        process(ctx);
    }

    private void setWelcomeText() {
        this.name.setText("Welcome " + UserSession.getCurrentUser().getInitial());


    }

    private void doInitializeItems() {
        this.name = findViewById(R.id.welcome_text);
        this.AllMeetingNonAst = findViewById(R.id.viewAllMeetingNonAstBtn);
        this.AllMeetingAst = findViewById(R.id.viewAllMeetingAstBtn);
        this.create = findViewById(R.id.createAccBtn);
        this.ban = findViewById(R.id.banAccBtn);
        this.grant = findViewById(R.id.grantRoleAccBtn);
        this.editProfile = findViewById(R.id.editProfile);
    }

    private void setButtonOnClick(final Context ctx){

        AllMeetingNonAst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ctx, ViewAllMeetingActivity.class);
                ctx.startActivity(myIntent);
            }
        });

        AllMeetingAst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ctx, ViewAllMeetingAstActivity.class);
                ctx.startActivity(myIntent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ctx, CreateAccountActivity.class);
                ctx.startActivity(myIntent);
            }
        });

        ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ctx, BanAccountActivity.class);
                ctx.startActivity(myIntent);
            }
        });

        grant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ctx, GrantRoleAccountActivity.class);
                ctx.startActivity(myIntent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ctx, EditProfileActivity.class);
                ctx.startActivity(myIntent);
            }
        });


    }

    public static void setUsernameText(String editText) {
        name.setText(editText);
    }

    void process(final Context ctx){

        SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("meetings").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Intent intent =  new Intent(HomeActivity.this, NotificationBroadcast.class);
                            intent.putExtra("role", UserSession.getCurrentUser().getRole());
                            intent.putExtra("stat", "initial");
                            final PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, intent, 0);


                            final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DATE),calendar.get(calendar.HOUR),  calendar.get(calendar.MINUTE), 0);
                            calendar.add(Calendar.MINUTE, 2);

                            Toast.makeText(ctx,  calendar.getTime().toString(), Toast.LENGTH_SHORT).show();

                            String now = "" + calendar.get(calendar.DATE) + "-" +  calendar.get(calendar.MONTH)+"-"+calendar.get(calendar.YEAR) + " "
                                    + calendar.get(calendar.HOUR) + ":" + calendar.get(calendar.MINUTE)  + ":00";

                            Toast.makeText(ctx, String.valueOf(calendar.get(calendar.MINUTE)), Toast.LENGTH_SHORT).show();

                            Date date = null;

                            try {
                                date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(now);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            int repeatEvery = 1000 * 60 * 1; //1 menit

                            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, date.getTime() , repeatEvery, pendingIntent);
                        }
                    }
                });


    }

    void createNotifChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "BinusReminderChannel";
            String desc =  "It's time to post";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =  new NotificationChannel("notifChannel", name, importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}