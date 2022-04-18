package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TestActivity extends AppCompatActivity {
    private Button changePass;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private EditText inputPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        changePass = (Button) findViewById(R.id.changePass);
        inputPass = (EditText) findViewById(R.id.inputPass);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();


        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(TestActivity.this)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn muốn đổi mật khẩu?")
                        .setNegativeButton("Huỷ", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("Xác nhận", (dialogInterface, i) -> {
                            String newPass = inputPass.getText().toString().trim();
                            user.updatePassword(newPass).addOnSuccessListener(unused -> {
                                Intent intent = new Intent(getApplicationContext(), Main.class);
                                startActivity(intent);
                            });
                        })
                        .show();
            }
        });

    }
}