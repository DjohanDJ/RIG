package com.example.rig.adapter;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rig.R;
import com.example.rig.models.Meeting;

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
    public void onBindViewHolder(@NonNull AllMeetingAdapter.MyViewHolder holder, int position) {
        holder.meetingDesc.setText(list.get(position).getDescription());
        holder.meetingId.setText(list.get(position).getMeeting_id());
        holder.meetingPass.setText(list.get(position).getMeeting_password());
        holder.meetingSchedule.setText(list.get(position).getTime());
        holder.meetingLink.setText(list.get(position).getLink_meeting());


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
