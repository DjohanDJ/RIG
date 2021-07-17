package com.example.rig.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rig.R;
import com.example.rig.models.Meeting;

import java.util.ArrayList;

public class AllMeetingAstAdapter extends RecyclerView.Adapter<AllMeetingAstAdapter.MyViewHolder>{

    Context ctx;
    ArrayList<Meeting> list;

    public AllMeetingAstAdapter(Context ctx, ArrayList<Meeting> list){
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.meeting_lis_ast_adapter, parent, false);

        return new AllMeetingAstAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  AllMeetingAstAdapter.MyViewHolder holder, final int position) {
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




    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView meetingDesc,meetingId, meetingPass, meetingSchedule, meetingLink;
        Button copy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            meetingDesc = itemView.findViewById(R.id.meeting_desc2);
            meetingId = itemView.findViewById(R.id.meeting_id2);
            meetingPass = itemView.findViewById(R.id.meeting_pass2);
            meetingSchedule = itemView.findViewById(R.id.meeting_time2);
            meetingLink = itemView.findViewById(R.id.meeting_link_zoom2);
            copy = itemView.findViewById(R.id.copy2_button);
        }
    }
}
