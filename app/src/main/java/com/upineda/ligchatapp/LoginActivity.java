package com.upineda.ligchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends SetupActivity {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private FirebaseAuth firebaseAuth;

    @Override
    public View.OnClickListener button2OnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(i);
                finish();
            }
        };
    }

    @Override
    public void setSetupViews(Button loginButton, Button signupButton) {
        loginButton.setText("Log in");
        signupButton.setText(R.string.sign_up_underline);
    }

    @Override
    public View.OnClickListener button1OnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check credentials
                setClickableButtons(false);
                final String username = getUsername();
                final String password = getPassword();

                root.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild(username) && validate(username, password)) {
                            String email = snapshot.child(username).getValue().toString();

                            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "SUCCESS log in", Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(getApplicationContext(), ChatRoomActivity.class);
                                        i.putExtra("username", username);
                                        startActivity(i);
                                        finish();
                                    }
                                    else
                                        displayError();
                                }
                            });
                        }
                        else
                            displayError();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference().child("Accounts");
    }
}
