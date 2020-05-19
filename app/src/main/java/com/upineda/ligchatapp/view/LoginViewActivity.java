package com.upineda.ligchatapp.view;

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
import com.upineda.ligchatapp.R;
import com.upineda.ligchatapp.model.User;

/**
 * This activity handles the Log in logic
 * and the communication with Firebase
 *
 * 05-18-2020
 * @author  Uriel Pineda
 */
public class LoginViewActivity extends SetupViewActivity {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private FirebaseAuth firebaseAuth;

    // Sign-up button click listener
    @Override
    public View.OnClickListener buttonTwoOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignupViewActivity.class);
                startActivity(i);
                finish();
            }
        };
    }

    @Override
    public void setSetupViews(Button loginButton, Button signupButton) {
        loginButton.setText(R.string.log_in);
        signupButton.setText(R.string.sign_up_underline);
    }

    // Log-in button click listener
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
                        if (snapshot.hasChild(username) && validate(username, password)) {
                            // we use the user's placeholder email to take advantage of firebase's
                            // secure authentication
                            final String email = snapshot.child(username).getValue().toString();

                            // initiate log-in
                            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // log in is successful, pass user data to the chat room
                                    if (task.isSuccessful()) {
                                        User user = new User(username, password, email);
                                        Intent i = new Intent(getApplicationContext(), ChatRoomActivity.class);
                                        i.putExtra(USER_DATA, user);
                                        startActivity(i);
                                        finish();
                                    } else
                                        displayError();
                                }
                            });
                        } else
                            displayError();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference().child(FIREBASE_ACCOUNTS);
    }
}
