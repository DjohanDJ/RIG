package com.example.rig.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.rig.R;
import com.example.rig.activities.HomeActivity;
import com.example.rig.activities.NotificationShowMeeting;
import com.example.rig.activities.ViewAllMeetingAstActivity;
import com.example.rig.authentication.SingletonFirebaseTool;
import com.example.rig.authentication.UserSession;
import com.example.rig.models.Meeting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationBroadcast extends BroadcastReceiver {
    private static Context ctx = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.ctx = context;
        String role = intent.getStringExtra("role");
        String stat = intent.getStringExtra("stat");
        checkAlarm(context,role,stat);
//        sendNotif(context,"asdasd","asdsad");
    }

    void sendNotif(Context context, String title, String desc){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifChannel").setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notifManager = NotificationManagerCompat.from(context);

        notifManager.notify(200, builder.build());
    }

    void sendNotifZoom(Context context, String title, String desc, Meeting meeting){
        Intent intentTest = new Intent(Intent.ACTION_VIEW, Uri.parse(meeting.getLink_meeting()));
        intentTest.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context,1, intentTest, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifChannel").setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent intentAlarm = new Intent(context, NotificationShowMeeting.class);
        intentAlarm.putExtra("desc", meeting.getDescription());
        intentAlarm.putExtra("link", meeting.getLink_meeting());
        intentAlarm.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentAlarm);

        NotificationManagerCompat notifManager = NotificationManagerCompat.from(context);

        notifManager.notify(200, builder.build());
    }

    void deleteMeeting(String id){
        SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("meetings")
                .document(id)
                .delete();
    }

    void checkAlarm(final Context ctx, final String role, final String stat){
        final ArrayList<Meeting> meetingList = new ArrayList<>();

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

                            ArrayList<Meeting> meetingFilter = new ArrayList<>();

                            for( Meeting m : meetingList){
                                for(String r : m.getRoles()){
                                    if(r.equals(role)){
                                        meetingFilter.add(m);
                                    }
                                }
                            }

                            if(meetingFilter.size() != 0){
                                Collections.sort(meetingList, new Comparator<Meeting>() {
                                    @Override
                                    public int compare(Meeting o1, Meeting o2) {

                                        Date date = null, date2 = null;
                                        try {
                                            date = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(o1.getTime());
                                            date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(o2.getTime());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Timestamp ts1 = new Timestamp(date.getTime());
                                        Timestamp ts2 = new Timestamp(date2.getTime());

                                        return ts1.compareTo(ts2);
                                    }
                                });

                                int minute5 = 1000 * 60 * 5;
                                SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                                Date date = new Date();
                                String today = dFormat.format(date);
                                String postDate;

                                for(Meeting meeting : meetingFilter){
                                    Date pDate = null;
                                    try {
                                        pDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(meeting.getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    Date createdDate = null;
                                    try {
                                        createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(meeting.getCreated_time());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    pDate.setTime(pDate.getTime() - minute5);
                                    postDate = dFormat.format(pDate);

                                    if (date.getTime() - pDate.getTime() > 1000 * 60 * 120){
                                        //delete meeting
                                        deleteMeeting(meeting.getId());
                                        sendNotif(ctx, meeting.getDescription(), "Meeting should be ended");
                                    }
                                    if(postDate.equalsIgnoreCase(today)){
                                        sendNotifZoom(ctx, meeting.getDescription(), "Meeting will start in 5 minutes", meeting);
                                        break;
                                    }else if (date.getTime() - createdDate.getTime() <= 1000 * 60 * 2){
                                        sendNotif(ctx, meeting.getDescription(), "Check your new meeting");
                                        break;
                                    }

                                }
                            }

                        }
                    }
                });

    }
}