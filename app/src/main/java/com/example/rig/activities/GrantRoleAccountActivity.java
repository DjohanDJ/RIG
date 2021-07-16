package com.example.rig.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.rig.R;
import com.example.rig.adapters.ManageUserBanAdapter;
import com.example.rig.adapters.ManageUserRoleAdapter;
import com.example.rig.authentication.SingletonFirebaseTool;
import com.example.rig.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class GrantRoleAccountActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> userList;
    private ManageUserRoleAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grant_role_account);
        doInitializeItems();
        doGetAllUsers();
    }

    private void doGetAllUsers() {
        SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    userList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                        if ((Objects.equals(documentSnapshot.getData().get("role"), "Supervisor") ||
                                Objects.equals(documentSnapshot.getData().get("role"), "Subject Coordinator") ||
                                Objects.equals(documentSnapshot.getData().get("role"), "Network Administrator") ||
                                Objects.equals(documentSnapshot.getData().get("role"), "Assistant"))
                        ) {
                            User user = new User(documentSnapshot.getId(),
                                    Objects.requireNonNull(documentSnapshot.getData().get("email")).toString()
                                    , Objects.requireNonNull(documentSnapshot.getData().get("password")).toString()
                                    , Objects.requireNonNull(documentSnapshot.getData().get("initial")).toString()
                                    , Objects.requireNonNull(documentSnapshot.getData().get("name")).toString()
                                    , Objects.requireNonNull(documentSnapshot.getData().get("role")).toString()
                                    , Objects.requireNonNull(documentSnapshot.getData().get("ban")).toString());
                            userList.add(user);
                        }
                    }
                    userAdapter = new ManageUserRoleAdapter(GrantRoleAccountActivity.this, userList);
                    recyclerView.setAdapter(userAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(GrantRoleAccountActivity.this));
                }
            }
        });
    }

    private void doInitializeItems() {
        this.recyclerView = findViewById(R.id.recViewGrantRoleUser);
        this.userList = new ArrayList<>();
    }
}