package com.example.rig.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rig.R;
import com.example.rig.authentication.SingletonFirebaseTool;
import com.example.rig.holders.UserViewHolder;
import com.example.rig.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ManageUserBanAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private ArrayList<User> users;
    private Context ctx;

    public ManageUserBanAdapter(Context ctx, ArrayList<User> users){
        this.ctx = ctx;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_ban_user_in_landscape, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        holder.getMyText().setText(users.get(position).getName());
        SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("users").document(users.get(position).getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;
                    if (Objects.requireNonNull(documentSnapshot.get("ban")).toString().equals("Not Ban")) {
                        holder.getTakeAccess().setVisibility(View.GONE);
                    } else {
                        holder.getGiveAccess().setVisibility(View.GONE);
                    }
                }
            }
        });

        holder.getGiveAccess().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("users").document(users.get(position).getId()).update("ban", "Yes Ban");
                ((Activity)v.getContext()).finish();
            }
        });
        holder.getTakeAccess().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("users").document(users.get(position).getId()).update("ban", "Not Ban");
                ((Activity)v.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
