package com.example.rig.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.rig.R;
import com.example.rig.activities.HomeActivity;
import com.example.rig.authentication.SingletonFirebaseTool;
import com.example.rig.authentication.UserSession;
import com.example.rig.models.Meeting;
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

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String role = intent.getStringExtra("role");
        checkAlarm(context,role);
//        setNextAlarm(context);
    }

    void sendNotif(Context context, String title, String desc){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifChannel").setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notifManager = NotificationManagerCompat.from(context);

        notifManager.notify(200, builder.build());
    }

    void checkAlarm(final Context ctx, final String role){
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
                                            date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(o1.getTime());
                                            date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(o2.getTime());
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
                                date.setTime(date.getTime() - minute5);
                                String today = dFormat.format(date);
                                String postDate;

                                for(Meeting meeting : meetingFilter){
                                    Date pDate = null;
                                    try {
                                        pDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(meeting.getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    pDate.setTime(pDate.getTime() - minute5);
                                    postDate = dFormat.format(pDate);


                                    if(today.equalsIgnoreCase(postDate)){
                                        sendNotif(ctx, meeting.getDescription(), "Meeting will start in 5 minutes");
                                        Toast.makeText(ctx, "asd", Toast.LENGTH_SHORT).show();
                                        break;
                                    }else if (pDate.getTime() - date.getTime() > 1000 * 60 * 60){
                                        //delete meeting
                                    }
                                }


                            }

                        }
                    }
                });

    }

    void setNextAlarm(final Context ctx){

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

                            Collections.sort(meetingList, new Comparator<Meeting>() {
                                @Override
                                public int compare(Meeting o1, Meeting o2) {

                                    Date date = null, date2 = null;
                                    try {
                                        date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(o1.getTime());
                                        date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(o2.getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Timestamp ts1 = new Timestamp(date.getTime());
                                    Timestamp ts2 = new Timestamp(date2.getTime());

                                    return ts1.compareTo(ts2);
                                }
                            });

                            Date pDate = null;

                            try {
                                pDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(meetingList.get(0).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String hour = new SimpleDateFormat("HH").format(pDate);
                            String minute = new SimpleDateFormat("mm").format(pDate);
                            String year = new SimpleDateFormat("yyyy").format(pDate);
                            String month = new SimpleDateFormat("MM").format(pDate);
                            String day = new SimpleDateFormat("dd").format(pDate);


                            Intent intent =  new Intent(ctx, NotificationBroadcast.class);
                            final PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);

                            final AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Integer.parseInt(year) , Integer.parseInt(month) - 1, Integer.parseInt(day),
                                    Integer.parseInt(hour),  Integer.parseInt(minute), 0);



                            Toast.makeText(ctx,  calendar.getTime().toString(), Toast.LENGTH_SHORT).show();

                            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        }
                    }
                });

    }
}
