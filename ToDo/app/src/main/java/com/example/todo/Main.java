package com.example.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.todo.Model.JobModel;
import com.example.todo.Model.TaskModel;
import com.example.todo.Model.UserModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import vn.thanguit.toastperfect.ToastPerfect;

public class Main extends AppCompatActivity {
    private MaterialToolbar topAppBar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth fAuth;
    private FloatingActionButton addTask;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment manageTask = new ManageTaskFragment();
        loadFragment(manageTask);
        topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Menu menuNav = navigationView.getMenu();
        MenuItem logoutItem = menuNav.findItem(R.id.logOutDrawer);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        addTask = (FloatingActionButton) findViewById(R.id.floating_action_button);
//
//        mDatabase = FirebaseDatabase.getInstance().getReference("users");
//
//
//        UserModel userModel = new UserModel();
//        TaskModel taskModel = new TaskModel();
//        JobModel jobModel = new JobModel();
//
//
//        ArrayList<TaskModel> taskModels = new ArrayList<>();
//        ArrayList<JobModel> jobModels = new ArrayList<>();
//
//        taskModel.setId(1);
//        taskModel.setStatus(1);
//        taskModel.setTaskName("Task 1");
//        taskModel.setDescription("mo ta task 1");
//
//        jobModel.setId(1);
//        jobModel.setName("job1");
//
//        taskModels.add(taskModel);
//        taskModels.add(taskModel);
//        taskModels.add(taskModel);
//        taskModels.add(taskModel);
//        taskModels.add(taskModel);
//
//        jobModel.setTaskList(taskModels);
//
//        jobModels.add(jobModel);
//
//        userModel.setJobList(jobModels);
//        userModel.setId(user.getUid());
//
//        mDatabase.child(user.getUid()).setValue(userModel);


        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ten = new EditText(Main.this);
                ten.setMaxLines(1);
                AlertDialog dialog = new AlertDialog.Builder(Main.this)
                        .setTitle("Thêm mới công việc")
                        .setMessage("Nhập tên công việc")
                        .setView(ten)
                        .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String editTextInput = ten.getText().toString();
                                ToastPerfect.makeText(Main.this, ToastPerfect.SUCCESS, editTextInput, ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .create();
                dialog.show();
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment search = new SearchFragment();
                Fragment manageTask = new ManageTaskFragment();
                Fragment user = new UserFragment();

                if (item.getItemId() == R.id.page_1) {
                    loadFragment(manageTask);
                    addTask.show();
                    return true;
                }
                if (item.getItemId() == R.id.page_2) {
                    loadFragment(search);
                    topAppBar.setTitle("Tìm kiếm");
                    addTask.hide();
                    return true;
                }
                if (item.getItemId() == R.id.page_3) {
                    loadFragment(user);
                    topAppBar.setTitle("Quản lý tài khoản");
                    addTask.hide();
                    return true;
                }

                return false;
            }
        });

        logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                drawerLayout.close();
                new MaterialAlertDialogBuilder(Main.this)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn đăng xuất ?")
                        .setNegativeButton("Huỷ", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("Xác nhận", (dialogInterface, i) -> {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), FirstLogin.class));
                            finish();
                        })
                        .show();
                return false;
            }
        });

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item == logoutItem)
                    return false;
                item.setChecked(true);
                drawerLayout.close();
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}