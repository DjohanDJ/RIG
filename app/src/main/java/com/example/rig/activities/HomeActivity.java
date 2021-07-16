package com.example.rig.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rig.R;
import com.example.rig.authentication.UserSession;

public class HomeActivity extends AppCompatActivity {

    private TextView name;
    private Button AllMeetingNonAst, AllMeetingAst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        doInitializeItems();
        setWelcomeText();
        setButtonOnClick(this);
    }

    private void setWelcomeText() {
        this.name.setText("Welcome " + UserSession.getCurrentUser().getInitial());
    }

    private void doInitializeItems() {
        this.name = findViewById(R.id.welcome_text);
        this.AllMeetingNonAst = findViewById(R.id.viewAllMeetingNonAstBtn);
        this.AllMeetingAst = findViewById(R.id.viewAllMeetingAstBtn);
    }

    private void setButtonOnClick(final Context ctx){

        AllMeetingNonAst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ctx, ViewAllMeetingActivity.class);
                ctx.startActivity(myIntent);
            }
        });

        AllMeetingAst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ctx, ViewAllMeetingAstActivity.class);
                ctx.startActivity(myIntent);
            }
        });

    }


}