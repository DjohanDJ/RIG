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

public class ManageUserRoleAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private ArrayList<User> users;
    private Context ctx;

    public ManageUserRoleAdapter(Context ctx, ArrayList<User> users){
        this.ctx = ctx;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_grant_role_user_in_landscape, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        holder.getMyText().setText(users.get(position).getName());

        holder.getGiveAccess().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("users").document(users.get(position).getId()).update("role", "Admin");
                ((Activity)v.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
