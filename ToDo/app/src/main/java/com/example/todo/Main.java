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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Main extends AppCompatActivity {
    private MaterialToolbar topAppBar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth fAuth;
    private FloatingActionButton addTask;
    private DatabaseReference mDatabase;
    private int currentJob = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment manageTask = new ManageTaskFragment();
        loadManageTaskFragment(manageTask, currentJob, 0);
        topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Menu menuNav = navigationView.getMenu();
        MenuItem logoutItem = menuNav.findItem(R.id.logOutDrawer);
        MenuItem deleteItem = menuNav.findItem(R.id.deleteListDrawer);
        MenuItem addJobItem = menuNav.findItem(R.id.createListDrawer);
        MenuItem deleteJobItem = topAppBar.getMenu().findItem(R.id.deleteJob);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        addTask = (FloatingActionButton) findViewById(R.id.floating_action_button);
//
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
//

//
//        JobModel jobModel = new JobModel();
//        UserModel userModel = new UserModel();
//
//        JobModel jobModel2 = new JobModel();
//
//        ArrayList<TaskModel> taskModels = new ArrayList<>();
//        ArrayList<TaskModel> taskModels2 = new ArrayList<>();
//
//        ArrayList<JobModel> jobModels = new ArrayList<>();
//
//
//
//        UUID uuid = UUID.randomUUID();
//
//
//        jobModel.setId(uuid.toString());
//        jobModel.setName("job1");
//
//       UUID  uuid2 = UUID.randomUUID();
//        jobModel2.setId(uuid2.toString());
//        jobModel2.setName("job2 ");
//
//         uuid = UUID.randomUUID();
//
//        for (int i = 0; i < 10; i++) {
//            TaskModel taskModel = new TaskModel();
//            taskModel.setStatus(1);
//            taskModel.setTaskName("Task show");
//            taskModel.setDescription("mo ta task 1");
//             uuid = UUID.randomUUID();
//            taskModel.setId(uuid.toString());
//            taskModels.add(taskModel);
//        }
//
//        for (int i = 0; i < 3; i++) {
//            TaskModel taskModel2 = new TaskModel();
//            taskModel2.setStatus(0);
//            taskModel2.setTaskName("task detete");
//            taskModel2.setDescription("mo ta task detete");
//            taskModel2.setIsDelete(0);
//             uuid = UUID.randomUUID();
//            taskModel2.setId(uuid.toString());
//            taskModels.add(taskModel2);
//        }
//
//
//        jobModel.setTaskList(taskModels);
//
//
//        jobModel2.setTaskList(taskModels);
//
//
//        jobModels.add(jobModel);
//        jobModels.add(jobModel2);
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
                                if (!editTextInput.isEmpty()) {
                                    TaskModel taskModel = new TaskModel();
                                    UUID uuid = UUID.randomUUID();
                                    Date date = new Date();
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                                    String strDate;
                                    strDate = formatter.format(date);

                                    taskModel.setId(uuid.toString());
                                    taskModel.setStatus(0);
                                    taskModel.setTaskName(editTextInput);
                                    taskModel.setDescription("");
                                    taskModel.setStartDate(strDate);
                                    taskModel.setEndDate("");
                                    taskModel.setIsDelete(0);

                                    addTaskToDB(taskModel);

                                }
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
                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) topAppBar.getLayoutParams();

                if (item.getItemId() == R.id.page_1) {
                    loadManageTaskFragment(manageTask, currentJob, 0);
                    addTask.show();
                    topAppBar.setTitle("ToDo");
                    params.height = 154;
                    topAppBar.setLayoutParams(params);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                    return true;
                }
                if (item.getItemId() == R.id.page_2) {
                    loadSearchFragment(search);
                    topAppBar.setTitle("Tìm kiếm");
                    params.height = 0;
                    topAppBar.setLayoutParams(params);
                    addTask.hide();
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    return true;
                }
                if (item.getItemId() == R.id.page_3) {
                    loadUserFragment(user);
                    topAppBar.setTitle("Quản lý tài khoản");
                    params.height = 0;
                    topAppBar.setLayoutParams(params);
                    addTask.hide();
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
        addJobItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                EditText ten = new EditText(Main.this);
                ten.setMaxLines(1);
                AlertDialog dialog = new AlertDialog.Builder(Main.this)
                        .setTitle("Thêm mới danh sách")
                        .setMessage("Nhập tên danh sách")
                        .setView(ten)
                        .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String editTextInput = ten.getText().toString();
                                if (!editTextInput.isEmpty()) {
                                    UUID uuid = UUID.randomUUID();
                                    Date date = new Date();
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                                    String strDate;
                                    strDate = formatter.format(date);

                                    JobModel jobModel = new JobModel();
                                    jobModel.setId(uuid.toString());
                                    jobModel.setName(editTextInput);
                                    jobModel.setStartDate(strDate);

                                    addJobToDB(jobModel);
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .create();
                dialog.show();
                return false;
            }
        });
        deleteJobItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                new MaterialAlertDialogBuilder(Main.this)
                        .setTitle("Cảnh báo")
                        .setMessage("Bạn có chắc chắn xoá danh sách này không ?")
                        .setNegativeButton("Huỷ", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("Xác nhận", (dialogInterface, i) -> {
                            deleteJobToDB();
                        })
                        .show();

                return false;
            }
        });
        
        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ManageTaskFragment manageTaskFragment = new ManageTaskFragment();
                loadManageTaskFragment(manageTaskFragment, currentJob, 1);
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
                if (item.getGroupId() == R.id.group1) {
                    currentJob = item.getItemId();
                    System.out.println(item.getItemId());
                    ManageTaskFragment manageTaskFragment = new ManageTaskFragment();
                    loadManageTaskFragment(manageTaskFragment, currentJob, 0);
                }
                if (item == logoutItem || item == addJobItem)
                    return false;
                item.setChecked(true);
                drawerLayout.close();
                return true;
            }
        });

        mDatabase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                if (userModel == null) {
                    UserModel userModel1 = new UserModel();
                    userModel1.setId(user.getUid());
                    mDatabase.child(user.getUid()).setValue(userModel1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.toException());
            }
        });

        mDatabase.child(user.getUid() + "/jobList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<JobModel> jobModelList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    JobModel jobModel = dataSnapshot.getValue(JobModel.class);
                    jobModelList.add(jobModel);
                }

                if (jobModelList != null && jobModelList.size() > 0)
                    for (int j = 0; j < jobModelList.size()+1; j++) {
                        Menu menu = navigationView.getMenu();
                        menu.removeItem(j);
                    }
                for (int i = 0; i < jobModelList.size(); i++) {
                    JobModel jobModel = jobModelList.get(i);
                    Menu menu = navigationView.getMenu();
                    if (i == 0) {
                        ManageTaskFragment manageTaskFragment = new ManageTaskFragment();
                        loadManageTaskFragment(manageTaskFragment,0,0);
                        menu.add(R.id.group1, Menu.FIRST - 1 + i, 0, jobModel.getName()).setIcon(R.drawable.ic_outline_format_list_bulleted_24).setChecked(true).setCheckable(true);
                    } else
                        menu.add(R.id.group1, Menu.FIRST - 1 + i, 0, jobModel.getName()).setIcon(R.drawable.ic_outline_format_list_bulleted_24).setChecked(false).setCheckable(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadSearchFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadUserFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadManageTaskFragment(Fragment fragment, int i, int isDelete) {
        Bundle args = new Bundle();
        args.putInt("index", i);
        args.putInt("isDelete", isDelete);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addTaskToDB(TaskModel taskModel) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Task<DataSnapshot> dataSnapshotTask = mDatabase.child(user.getUid()).get();
        dataSnapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                userModel.getJobList().get(currentJob).getTaskList().add(taskModel);
                mDatabase.child(user.getUid()).setValue(userModel);

            }
        });
    }

    private void addJobToDB(JobModel jobModel) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Task<DataSnapshot> dataSnapshotTask = mDatabase.child(user.getUid()).get();
        dataSnapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                userModel.getJobList().add(jobModel);
                mDatabase.child(user.getUid()).setValue(userModel);
            }
        });
    }

    private void deleteJobToDB(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Task<DataSnapshot> dataSnapshotTask = mDatabase.child(user.getUid()).get();
        dataSnapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                userModel.getJobList().remove(currentJob);
                mDatabase.child(user.getUid()).setValue(userModel);
            }
        });
    }
}