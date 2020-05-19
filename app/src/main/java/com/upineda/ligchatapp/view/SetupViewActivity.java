package com.upineda.ligchatapp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.upineda.ligchatapp.R;

/**
 * This is the base class of Login and Sign up activities
 * This handles all the views and ui components
 *
 * 05-18-2020
 * @author  Uriel Pineda
 */
public abstract class SetupViewActivity extends AppCompatActivity {

    static String DEFAULT_DOMAIN = "@lig.co";
    static String FIREBASE_ACCOUNTS = "Accounts";
    static String USER_DATA = "user";

    public void displayError() {
        TextView incorrectValue1 = findViewById(R.id.incorrectValue);
        TextView incorrectValue2 = findViewById(R.id.incorrectValue1);
        EditText passwordField = findViewById(R.id.passwordField);

        incorrectValue1.setVisibility(View.VISIBLE);
        incorrectValue2.setVisibility(View.VISIBLE);
        passwordField.setText("");

        // We only set the buttons to clickable if there is an error
        // if there is no error, we proceed to the chat room
        setClickableButtons(true);
    }

    public boolean validate(String username, String password) {

        // to keep it simple, let's limit the username to only letters and numbers
        return username.length() >= 8 && username.length() <= 16 &&
                password.length() >= 8 && password.length() <= 16 && username.matches("[a-zA-Z0-9]*");
    }

    public abstract View.OnClickListener buttonOneOnClickListener();

    public abstract View.OnClickListener buttonTwoOnClickListener();

    public abstract void setSetupViews(Button button1, Button button2);

    public String getUsername() {
        TextView usernameField = findViewById(R.id.usernameField);
        return usernameField.getText().toString();
    }

    public String getPassword() {
        TextView passwordField = findViewById(R.id.passwordField);
        return passwordField.getText().toString();
    }

    public void setClickableButtons(Boolean enabled) {
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);

        button1.setClickable(enabled);
        button2.setClickable(enabled);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setup_page);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);

        setSetupViews(button1, button2);
        button1.setOnClickListener(buttonOneOnClickListener());
        button2.setOnClickListener(buttonTwoOnClickListener());
    }
}
