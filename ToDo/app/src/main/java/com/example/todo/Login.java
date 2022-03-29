package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

public class Login extends AppCompatActivity {
    TextView textViewDontHaveAccount;
    LinearLayout mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textViewDontHaveAccount = (TextView) findViewById(R.id.dontHaveAccount);
        mainContainer = (LinearLayout) findViewById(R.id.mainContainer);
        Init();

    }

    void Init() {
        textViewDontHaveAccount.setText(HtmlCompat.fromHtml("<font color=#666666>Didn't have an account</font> <u></font><b><font color=#0173B7>Sign Up</font></u></b></font>", 0));
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
        textViewDontHaveAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
    }
}