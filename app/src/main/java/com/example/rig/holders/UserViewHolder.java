package com.example.rig.holders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.rig.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    private TextView myText;
    private Button giveAccess, takeAccess;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        myText = itemView.findViewById(R.id.titleRow);
        giveAccess = itemView.findViewById(R.id.giveAccessBtn);
        takeAccess = itemView.findViewById(R.id.takeAccessBtn);
    }

    public TextView getMyText() {
        return myText;
    }

    public Button getGiveAccess() {
        return giveAccess;
    }

    public Button getTakeAccess() {
        return takeAccess;
    }

}
