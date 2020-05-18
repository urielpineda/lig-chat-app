package com.upineda.ligchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import com.upineda.ligchatapp.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * This activity handles the Sign up logic
 * and the communication with Firebase
 * <p>
 * 05-18-2020
 *
 * @author Uriel Pineda
 */
public class SignupViewActivity extends SetupViewActivity {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private FirebaseAuth fireBaseAuth;

    // Sign-up button click listener
    @Override
    public View.OnClickListener buttonOneOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = getUsername();
                final String password = getPassword();

                // set Clickable to false to avoid multiple executions at the same time
                setClickableButtons(false);

                root.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // validate the data
                        if (snapshot.hasChild(username) || !validate(username, password)) {
                            displayError();
                        } else {
                            Map<String, Object> idMap = new HashMap<String, Object>();

                            String temp_key = root.push().getKey();
                            temp_key += DEFAULT_DOMAIN;
                            idMap.put(username, temp_key);
                            Task complete = root.updateChildren(idMap);

                            // Check if username is valid in firebase
                            if (complete.isSuccessful()) {
                                displayError();
                                return;
                            }

                            // create the account using firebase authentication
                            final String finalTemp_key = temp_key;
                            fireBaseAuth.createUserWithEmailAndPassword(temp_key, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If account creation is successful we log in right away and enter the chat room

                                    if (task.isSuccessful()) {
                                        fireBaseAuth.signInWithEmailAndPassword(finalTemp_key, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    User user = new User(username, password, finalTemp_key);
                                                    Intent i = new Intent(getApplicationContext(), ChatRoomActivity.class);
                                                    i.putExtra(USER_DATA, user);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }
                                        });
                                    } else {
                                        displayError();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        };
    }

    // Log-in button click listener
    @Override
    public View.OnClickListener buttonTwoOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginViewActivity.class);
                startActivity(i);
                finish();
            }
        };
    }

    @Override
    public void setSetupViews(Button signupButton, Button loginButton) {
        signupButton.setText(R.string.sign_up);
        loginButton.setText(R.string.log_in_underline);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireBaseAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference().child(FIREBASE_ACCOUNTS);
    }
}
