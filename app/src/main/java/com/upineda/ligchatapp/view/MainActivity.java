package com.upineda.ligchatapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.upineda.ligchatapp.R;

/**
 * Main landing page with two options, Log-in or Sign-up
 *
 * 05-18-2020
 * @author  Uriel Pineda
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        Button loginButton = findViewById(R.id.button1);
        Button signupButton = findViewById(R.id.button2);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(getApplicationContext(), SignupViewActivity.class);
                startActivity(signupIntent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginViewActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}