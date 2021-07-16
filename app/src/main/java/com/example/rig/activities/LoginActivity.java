package com.example.rig.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rig.R;
import com.example.rig.animations.LoadingAnimation;
import com.example.rig.authentication.SingletonFirebaseTool;
import com.example.rig.authentication.UserSession;
import com.example.rig.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button signInButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        doInitializeItems();
        doCheckSession();
        doButtonListener();
    }

    private void doCheckSession() {
        final String currentUserId = sharedPreferences.getString("user_userId", "");
        assert currentUserId != null;
        if (!currentUserId.equals("")) {
            doFeedUserSession(currentUserId);
        }
    }

    private void doButtonListener() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCheckCredential();
            }
        });
    }

    private void doCheckCredential() {
        String email = this.email.getText().toString();
        String pass = this.password.getText().toString();
        if (email.trim().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.emailFilled), Toast.LENGTH_SHORT).show();
        } else if (pass.trim().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.passFilled), Toast.LENGTH_SHORT).show();
        } else {
            LoadingAnimation.startLoading(LoginActivity.this);
            SingletonFirebaseTool.getInstance().getMyFirebaseAuth()
                    .signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalidCredential), Toast.LENGTH_SHORT).show();
                        LoadingAnimation.getDialog().dismiss();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_userId", Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid());
                        editor.apply();
                        doFeedUserSession(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid());
                    }
                }
            });
        }
    }

    public void doFeedUserSession(final String loggedId) {
        SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("users")
                .document(loggedId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User newUser = new User(documentSnapshot.getString("id"), documentSnapshot.getString("email"), documentSnapshot.getString("password"), documentSnapshot.getString("initial"), documentSnapshot.getString("name"), documentSnapshot.getString("role"), documentSnapshot.getString("ban"));
                    UserSession.setCurrentUser(newUser);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
            }
        });
    }

    private void doInitializeItems() {
        this.email = findViewById(R.id.email_text);
        this.password = findViewById(R.id.password_text);
        this.signInButton = findViewById(R.id.button_login);
        this.sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
    }
}