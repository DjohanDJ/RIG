package com.example.rig.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfileActivity extends AppCompatActivity {

    private EditText nameText, passwordText, emailText, initialText;
    private Button updateButton;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        doInitializeItems();
        doButtonListener();
    }

    private void doButtonListener() {
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCheckValidation();
            }
        });
    }

    private void doCheckValidation() {
        final String name = this.nameText.getText().toString();
        final String password = this.passwordText.getText().toString();
        final String email = this.emailText.getText().toString();
        final String initial = this.initialText.getText().toString();

//        if (name.trim().isEmpty()) {
//            Toast.makeText(this, getResources().getString(R.string.username_filled), Toast.LENGTH_SHORT).show();
//        } else if (email.isEmpty()) {
//            Toast.makeText(this, getResources().getString(R.string.emailFilled), Toast.LENGTH_SHORT).show();
//        } else if (password.isEmpty()) {
//            Toast.makeText(this, getResources().getString(R.string.passFilled), Toast.LENGTH_SHORT).show();
//        } else if (initial.length() != 6) {
//            Toast.makeText(this, getResources().getString(R.string.initial), Toast.LENGTH_SHORT).show();
//        } else {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential = EmailAuthProvider.getCredential(UserSession.getCurrentUser().getEmail(), UserSession.getCurrentUser().getPassword());
            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    LoadingAnimation.startLoading(EditProfileActivity.this);
                    if (task.isSuccessful()) {
                        firebaseUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

//                                    SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("users").document(UserSession.getCurrentUser().getId())
//                                            .delete()
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
////                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
//                                                }
//                                            });
                                    final User updatedUser = new User(UserSession.getCurrentUser().getId(), email, password, initial, name, UserSession.getCurrentUser().getRole(), UserSession.getCurrentUser().getBan());
                                    SingletonFirebaseTool.getInstance().getMyFireStoreReference().collection("users")
                                            .document(UserSession.getCurrentUser().getId())
                                            .set(updatedUser)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    UserSession.setCurrentUser(updatedUser);
                                                    HomeActivity.setUsernameText(initial);
                                                    Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.edit_success), Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });

                                }
                            }
                        });
                    } else {
                        Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.edit_failed), Toast.LENGTH_SHORT).show();
                        LoadingAnimation.getDialog().dismiss();
                    }
                }
            });
//        }
    }

    private void doInitializeItems() {
        this.nameText = findViewById(R.id.name);
        this.passwordText = findViewById(R.id.pass_text);
        this.emailText = findViewById(R.id.email_text);
        this.initialText = findViewById(R.id.initial);
        this.updateButton = findViewById(R.id.update_button);
        this.nameText.setText(UserSession.getCurrentUser().getName());
        this.emailText.setText(UserSession.getCurrentUser().getEmail());
        this.initialText.setText(UserSession.getCurrentUser().getInitial());
        this.emailText.setEnabled(false);
    }

}