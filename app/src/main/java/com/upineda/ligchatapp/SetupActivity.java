package com.upineda.ligchatapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;

import org.w3c.dom.Text;


public abstract class SetupActivity extends AppCompatActivity {

    public void displayError() {
        TextView incorrectValue1 = findViewById(R.id.incorrectValue);
        TextView incorrectValue2 = findViewById(R.id.incorrectValue1);
        EditText passwordField = findViewById(R.id.passwordField);

        incorrectValue1.setVisibility(View.VISIBLE);
        incorrectValue2.setVisibility(View.VISIBLE);
        passwordField.setText("");

        setClickableButtons(true);
    }

    public boolean validate(String username, String password) {
        return username.length() >= 8 && username.length() <= 16 &&
                password.length() >= 8 && password.length() <=16 && username.matches("[a-zA-Z0-9]*");
    }

    public abstract View.OnClickListener button1OnClickListener();
    public abstract View.OnClickListener button2OnClickListener();
    public abstract void setSetupViews(Button button1, Button button2);

    public String getUsername(){
        TextView usernameField = findViewById(R.id.usernameField);
        return usernameField.getText().toString();
    }

    public String getPassword(){
        TextView passwordField = findViewById(R.id.passwordField);
        return passwordField.getText().toString();
    }

    public void setClickableButtons(Boolean enabled){
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
        button1.setOnClickListener(button1OnClickListener());
        button2.setOnClickListener(button2OnClickListener());
    }
}
