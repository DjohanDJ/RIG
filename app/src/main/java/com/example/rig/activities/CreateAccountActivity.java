package com.example.rig.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rig.R;
import com.example.rig.animations.LoadingAnimation;
import com.example.rig.authentication.SingletonFirebaseTool;
import com.example.rig.authentication.UserSession;
import com.example.rig.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

    private Spinner dropdownRole;
    private EditText name, email, password, initial;
    private Button createButton;
    private String role = "Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        doInitializeItems();
        doButtonListener();
    }

    private void doButtonListener() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role = dropdownRole.getItemAtPosition((int) dropdownRole.getSelectedItemId()).toString();
                doCheckValidation();
            }
        });
    }

    private void doCheckValidation() {

        final String name = this.name.getText().toString();
        final String email = this.email.getText().toString();
        final String password = this.password.getText().toString();
        final String initial = this.initial.getText().toString();

        if (name.trim().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.username_filled), Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.emailFilled), Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.passFilled), Toast.LENGTH_SHORT).show();
        } else if (initial.length() != 6) {
            Toast.makeText(this, getResources().getString(R.string.initial), Toast.LENGTH_SHORT).show();
        } else {
            LoadingAnimation.startLoading(CreateAccountActivity.this);
            SingletonFirebaseTool.getInstance().getMyFirebaseAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(CreateAccountActivity.this, getResources().getString(R.string.sign_up_error), Toast.LENGTH_SHORT).show();
                    } else {
                        String userId = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                        User newUser = new User(userId, email, password, initial, name, role, "Not Ban");
                        SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("users").document(userId).set(newUser);
                        finish();
                    }
                }
            });
        }
    }

    private void doInitializeItems() {
        this.dropdownRole = findViewById(R.id.dropdownRole);
        this.dropdownRole.setAdapter(new ArrayAdapter<>(CreateAccountActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.role)));
        this.name = findViewById(R.id.name);
        this.email = findViewById(R.id.email_text);
        this.password = findViewById(R.id.pass_text);
        this.initial = findViewById(R.id.initial);
        this.createButton = findViewById(R.id.create_button);
    }
}