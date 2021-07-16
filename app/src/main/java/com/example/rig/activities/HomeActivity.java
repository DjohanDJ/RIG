package com.example.rig.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.rig.R;
import com.example.rig.authentication.UserSession;

public class HomeActivity extends AppCompatActivity {

    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        doInitializeItems();
        setWelcomeText();
    }

    private void setWelcomeText() {
        this.name.setText("Welcome " + UserSession.getCurrentUser().getInitial());
    }

    private void doInitializeItems() {
        this.name = findViewById(R.id.welcome_text);
    }
}