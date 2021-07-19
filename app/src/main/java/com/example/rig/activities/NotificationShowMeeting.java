package com.example.rig.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rig.R;
import com.example.rig.notification.NotificationBroadcast;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationShowMeeting extends AppCompatActivity {

    private TextView desc_txt;
    private Button join, copy, dismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notidication_show_meeting);

        Intent intent = getIntent();
        String desc = intent.getStringExtra("desc");
        final String link = intent.getStringExtra("link");

        desc_txt = findViewById(R.id.notif_alarm_title);
        join = findViewById(R.id.notif_join_zoom);
        copy = findViewById(R.id.notif_copy_link);
        dismiss = findViewById(R.id.notif_dismiss);

        desc_txt.setText(desc);

        final Context ctx = this;

        final Ringtone r = RingtoneManager.getRingtone(this.getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        r.play();

        Timer task = new Timer();
        final Ringtone finalR = r;
        task.schedule(new TimerTask() {
            @Override
            public void run() {
                r.stop();
            }
        }, 30000);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r.stop();
                Intent intentTest = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                intentTest.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intentTest);
                finish();
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r.stop();
                ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", link.toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ctx, "Zoom Link copied to clipboard", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r.stop();
                finish();
            }
        });


    }
}