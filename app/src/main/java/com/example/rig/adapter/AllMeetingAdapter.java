package com.example.rig.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rig.R;
import com.example.rig.activities.UpdateMeetingActivity;
import com.example.rig.activities.ViewAllMeetingActivity;
import com.example.rig.authentication.SingletonFirebaseTool;
import com.example.rig.models.Meeting;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class AllMeetingAdapter extends RecyclerView.Adapter<AllMeetingAdapter.MyViewHolder>{

    Context ctx;
    ArrayList<Meeting> list;

    public AllMeetingAdapter(Context ctx, ArrayList<Meeting> list){
        this.ctx = ctx;
        this.list = list;
    }


    @NonNull
    @Override
    public AllMeetingAdapter.MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.meeting_list_adapter, parent, false);

        return new AllMeetingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllMeetingAdapter.MyViewHolder holder, final int position) {
        holder.meetingDesc.setText(list.get(position).getDescription());
        holder.meetingId.setText(list.get(position).getMeeting_id());
        holder.meetingPass.setText(list.get(position).getMeeting_password());
        holder.meetingSchedule.setText(list.get(position).getTime());
        holder.meetingLink.setText(list.get(position).getLink_meeting());

        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", list.get(position).getLink_meeting().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ctx, "Zoom Link copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ctx, UpdateMeetingActivity.class);
                myIntent.putExtra("id", list.get(position).getId());
                myIntent.putExtra("description", list.get(position).getDescription());
                myIntent.putExtra("meeting_id", list.get(position).getMeeting_id());
                myIntent.putExtra("meeting_pass", list.get(position).getMeeting_password());
                myIntent.putExtra("link_zoom", list.get(position).getLink_meeting());
                myIntent.putExtra("time", list.get(position).getTime());
                myIntent.putExtra("roles", list.get(position).getRoles());
                ctx.startActivity(myIntent);
                ((Activity)v.getContext()).finish();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("meetings")
                        .document(list.get(position).getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ctx, ctx.getResources().getString(R.string.meeting_updated), Toast.LENGTH_SHORT).show();
                                ((Activity)v.getContext()).finish();
                            }
                        });
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView meetingDesc,meetingId, meetingPass, meetingSchedule, meetingLink;
        Button copy, update, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            meetingDesc = itemView.findViewById(R.id.meeting_desc);
            meetingId = itemView.findViewById(R.id.meeting_id);
            meetingPass = itemView.findViewById(R.id.meeting_pass);
            meetingSchedule = itemView.findViewById(R.id.meeting_schdule);
            meetingLink = itemView.findViewById(R.id.meeting_link_zoom);
            copy = itemView.findViewById(R.id.copy1_button);
            update = itemView.findViewById(R.id.update_meeting_button);
            delete = itemView.findViewById(R.id.delete_meeting_button);
        }
    }
}
