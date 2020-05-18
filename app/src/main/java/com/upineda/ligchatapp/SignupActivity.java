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

import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends SetupActivity {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private FirebaseAuth fireBaseAuth;
    private String temp_key;
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


                    if (snapshot.hasChild(username) || !validate(username, password)) {
                        displayError();
                    }else
                    {
                        Map<String, Object> idMap = new HashMap<String,Object>();

                        temp_key = root.push().getKey();
                        temp_key+="@lig.co";
                        idMap.put(username, temp_key);
                        Task complete = root.updateChildren(idMap);

                        if(complete.isSuccessful()) {
                            displayError();
                            return;
                        }

                        fireBaseAuth.createUserWithEmailAndPassword(temp_key, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    fireBaseAuth.signInWithEmailAndPassword(temp_key, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()) {
                                                Intent i = new Intent(getApplicationContext(), ChatRoomActivity.class);
                                                i.putExtra("username", username);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    });
                                }
                                else {
                                    displayError();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
                });
            }
        };
    }

    @Override
    public View.OnClickListener button2OnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        };
    }

    @Override
    public void setSetupViews(Button signupButton, Button loginButton) {
        signupButton.setText("Sign up");
        loginButton.setText(R.string.log_in_underline);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireBaseAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference().child("Accounts");
    }
}
