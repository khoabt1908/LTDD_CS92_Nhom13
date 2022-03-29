package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

public class Register extends AppCompatActivity {
    TextView textViewPolicy;
    TextView textViewHaveAccount;
    LinearLayout mainContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textViewPolicy = (TextView) findViewById(R.id.confirm);
        textViewHaveAccount = (TextView) findViewById(R.id.haveAccount);
        mainContainer = (LinearLayout) findViewById(R.id.mainContainer);

        Init();

    }
    void Init() {
        textViewPolicy.setText(HtmlCompat.fromHtml("<font color=#666666>By signing up you accept the </font><font color=#0173B7>  <b><u>Terms & Conditions</u></b></font><font color=#666666> and the <u></font><b><font color=#0173B7>Privacy Policy</font></u></b></font>",0));
        textViewHaveAccount.setText(HtmlCompat.fromHtml("<font color=#666666>Already have an account</font> <u></font><b><font color=#0173B7>Log In</font></u></b></font>",0));
        textViewHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
        mainContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                if (imm.isAcceptingText()) { // verify if the soft keyboard is open
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }
        });
    }
}