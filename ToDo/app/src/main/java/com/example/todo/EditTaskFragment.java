package com.example.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.todo.Model.TaskModel;
import com.example.todo.Model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.thanguit.toastperfect.ToastPerfect;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnBack, btnUpdate;
    private TextInputLayout txtId, txtDateStart, txtDateEnd, txtName, txtDescription;

    private int currentJob;
    private String currentItemId;
    private DatabaseReference mDatabase;


    public EditTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTaskFragment newInstance(String param1, String param2) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_edit_task, container, false);

        btnBack = (Button) rootview.findViewById(R.id.goBack);
        btnUpdate = (Button) rootview.findViewById(R.id.editTask);
        txtId = (TextInputLayout) rootview.findViewById(R.id.editTaskId);
        txtDateStart = (TextInputLayout) rootview.findViewById(R.id.editTaskStartDate);
        txtDateEnd = (TextInputLayout) rootview.findViewById(R.id.editTaskEndDate);
        txtDescription = (TextInputLayout) rootview.findViewById(R.id.editTaskDescription);
        txtName = (TextInputLayout) rootview.findViewById(R.id.editTaskName);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        Bundle args = getArguments();
        String itemId = args.getString("itemId");
        this.currentItemId = itemId;
        int currentJob = args.getInt("currentJob", 0);
        this.currentJob = currentJob;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn trở về ?" +
                                "\nMọi thay đổi sẽ không được lưu lại")
                        .setNegativeButton("Huỷ", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("Xác nhận", (dialogInterface, i) -> {
                            ((Main) getActivity()).onBack();
                        })
                        .show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                ((Main) getActivity()).onBack();
            }
        });
        loadData();
        return rootview;
    }

    private void loadData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Task<DataSnapshot> dataSnapshotTask = mDatabase.child(user.getUid()).get();
        dataSnapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                TaskModel currentTask = new TaskModel();
                for (TaskModel taskModel : userModel.getJobList().get(currentJob).getTaskList()) {
                    if (taskModel.getId().equals(currentItemId)) {
                        currentTask = taskModel;
                        break;
                    }
                }
                txtId.getEditText().setText(currentTask.getId());
                txtDateEnd.getEditText().setText((!currentTask.getEndDate().isEmpty())
                        ? currentTask.getEndDate() : "Chưa hoàn thành");
                txtDateStart.getEditText().setText(currentTask.getStartDate());
                txtName.getEditText().setText(currentTask.getTaskName());
                txtDescription.getEditText().setText(currentTask.getDescription());
            }
        });
    }
    private void updateData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Task<DataSnapshot> dataSnapshotTask = mDatabase.child(user.getUid()).get();
        dataSnapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
               for(int i = 0; i<userModel.getJobList().get(currentJob).getTaskList().size();i++)
               {
                   TaskModel taskModel = userModel.getJobList().get(currentJob).getTaskList().get(i);
                   if(taskModel.getId().equals(currentItemId)){
                       userModel.getJobList().get(currentJob).getTaskList().get(i).setTaskName(txtName.getEditText().getText().toString());
                       userModel.getJobList().get(currentJob).getTaskList().get(i).setDescription(txtDescription.getEditText().getText().toString());
                   }
               }
                mDatabase.child(user.getUid()).setValue(userModel);
                ToastPerfect.makeText(getContext(), ToastPerfect.SUCCESS, "Cập nhật thông tin thành công", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
            }
        });
    }
}