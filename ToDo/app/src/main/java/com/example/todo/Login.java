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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.thanguit.toastperfect.ToastPerfect;

public class Login extends AppCompatActivity {
    private TextView textViewDontHaveAccount, forgotPass;
    private LinearLayout mainContainer;
    private FirebaseAuth fAuth;
    private CircularProgressIndicator loading;
    private Button signInButton;
    private TextInputLayout txtEmail, txtPass;

    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewDontHaveAccount = (TextView) findViewById(R.id.dontHaveAccount);
        mainContainer = (LinearLayout) findViewById(R.id.mainContainer);
        txtEmail = (TextInputLayout) findViewById(R.id.email);
        txtPass = (TextInputLayout) findViewById(R.id.password);
        loading = (CircularProgressIndicator) findViewById(R.id.loading);
        signInButton = (Button) findViewById(R.id.loginButton);
        forgotPass = (TextView) findViewById(R.id.forgotPass);
        fAuth = FirebaseAuth.getInstance();

//        createRequest();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = txtPass.getEditText().getText().toString().trim();
                String email = txtEmail.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("You need to enter email");
                    return;
                } else {
                    if (txtEmail.isErrorEnabled())
                        txtEmail.setErrorEnabled(false);
                }
                if (!isValid(email)) {
                    txtEmail.setError("The email invalid");
                    return;
                } else {
                    if (txtEmail.isErrorEnabled())
                        txtEmail.setErrorEnabled(false);
                }
                if (TextUtils.isEmpty(password)) {
                    txtPass.setError("You need to enter your password");
                    return;
                } else {
                    if (txtPass.isErrorEnabled())
                        txtPass.setErrorEnabled(false);
                }
                if (password.length() < 6) {
                    txtPass.setError("Password must be more 6 characters");
                    return;
                } else {
                    if (txtPass.isErrorEnabled())
                        txtPass.setErrorEnabled(false);
                }
                loading.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.INVISIBLE);
                forgotPass.setVisibility(View.INVISIBLE);
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = fAuth.getCurrentUser();
                            if (currentUser.isEmailVerified()) {
                                startActivity(new Intent(getApplicationContext(), Main.class));
                            } else {
                                new MaterialAlertDialogBuilder(Login.this)
                                        .setTitle("Lỗi")
                                        .setMessage("Vui lòng xác nhận mail trước khi đăng nhập !")
                                        .setNegativeButton("Đóng", (dialogInterface, i) -> {
                                            txtEmail.getEditText().setText("");
                                            txtPass.getEditText().setText("");
                                        })
                                        .show();
                                loading.setVisibility(View.INVISIBLE);
                                signInButton.setVisibility(View.VISIBLE);
                                forgotPass.setVisibility(View.VISIBLE);
                            }

                        } else {
                            new MaterialAlertDialogBuilder(Login.this)
                                    .setTitle("Lỗi")
                                    .setMessage(task.getException().getMessage())
                                    .setNegativeButton("Đóng", (dialogInterface, i) -> {
                                        txtEmail.getEditText().setText("");
                                        txtPass.getEditText().setText("");
                                    })
                                    .show();
                            loading.setVisibility(View.INVISIBLE);
                            signInButton.setVisibility(View.VISIBLE);
                            forgotPass.setVisibility(View.VISIBLE);
                        }
                    }
                });


            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtEmail.getEditText().getText().toString().trim();
                if (email.isEmpty())
                    return;
                new MaterialAlertDialogBuilder(Login.this)
                        .setTitle("Reset Password?")
                        .setMessage(email)
                        .setNegativeButton("Huỷ", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("Xác nhận", (dialogInterface, i) -> {
                            loading.setVisibility(View.VISIBLE);
                            signInButton.setVisibility(View.INVISIBLE);
                            forgotPass.setVisibility(View.INVISIBLE);
                            fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    ToastPerfect.makeText(Login.this, ToastPerfect.SUCCESS, "Reset link sent to your email", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                    loading.setVisibility(View.INVISIBLE);
                                    signInButton.setVisibility(View.VISIBLE);
                                    forgotPass.setVisibility(View.VISIBLE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    ToastPerfect.makeText(Login.this, ToastPerfect.ERROR, "Error! Reset link is not sent", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                    loading.setVisibility(View.INVISIBLE);
                                    signInButton.setVisibility(View.VISIBLE);
                                    forgotPass.setVisibility(View.VISIBLE);
                                }
                            });

                        })
                        .show();

            }
        });


        Init();

    }

    private void createRequest() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {

        }

    }

    private void Init() {
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