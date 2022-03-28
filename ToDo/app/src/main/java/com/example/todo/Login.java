package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

public class Login extends AppCompatActivity {
    TextView textViewDontHaveAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textViewDontHaveAccount = (TextView) findViewById(R.id.dontHaveAccount);

        textViewDontHaveAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
        Init();

    }
    void Init() {
        textViewDontHaveAccount.setText(HtmlCompat.fromHtml("<font color=#666666>Didn't have an account</font> <u></font><b><font color=#0173B7>Sign Up</font></u></b></font>",0));
    }
}