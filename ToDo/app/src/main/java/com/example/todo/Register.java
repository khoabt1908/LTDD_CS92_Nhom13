package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private TextView textViewPolicy, textViewHaveAccount;
    private LinearLayout mainContainer;
    private Button signUpButton;
    private CircularProgressIndicator loading;
    private TextInputLayout txtEmail, txtPass, txtName;
    private FirebaseAuth fAuth;

    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textViewPolicy = (TextView) findViewById(R.id.confirm);
        textViewHaveAccount = (TextView) findViewById(R.id.haveAccount);
        mainContainer = (LinearLayout) findViewById(R.id.mainContainer);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        loading = (CircularProgressIndicator) findViewById(R.id.loading);
        txtEmail = (TextInputLayout) findViewById(R.id.email);
        txtPass = (TextInputLayout) findViewById(R.id.password);
        txtName = (TextInputLayout) findViewById(R.id.fullName);
        fAuth = FirebaseAuth.getInstance();

        Init();

    }

    void Init() {
        textViewPolicy.setText(HtmlCompat.fromHtml("<font color=#666666>By signing up you accept the </font><font color=#0173B7>  <b><u>Terms & Conditions</u></b></font><font color=#666666> and the <u></font><b><font color=#0173B7>Privacy Policy</font></u></b></font>", 0));
        textViewHaveAccount.setText(HtmlCompat.fromHtml("<font color=#666666>Already have an account</font> <u></font><b><font color=#0173B7>Log In</font></u></b></font>", 0));
        textViewHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                String fullName = txtName.getEditText().getText().toString().trim();
                String password = txtPass.getEditText().getText().toString().trim();
                String email = txtEmail.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("You need to enter email");
                    return;
                }
                if (!isValid(email)) {
                    txtEmail.setError("The email invalid");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    txtPass.setError("You need to enter your password");
                    return;
                } else {
                    txtEmail.setError("");
                }
                if (password.length() < 6) {
                    txtPass.setError("Password must be more 6 characters");
                    return;
                }
                else {
                    txtPass.setError("");
                }
                loading.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.INVISIBLE);
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "User create", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Main.class));
                        } else {
                            Toast.makeText(Register.this, "loi" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.INVISIBLE);
                            signUpButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
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