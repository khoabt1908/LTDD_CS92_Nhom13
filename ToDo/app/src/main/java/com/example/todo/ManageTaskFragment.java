package com.example.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Adapter.ToDoAdapter;
import com.example.todo.Model.TaskModel;
import com.example.todo.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private RecyclerView recyclerView;
    private ToDoAdapter taskAdapter;
    private List<TaskModel> taskList;
    private DatabaseReference mDatabase;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageTaskFragment newInstance(String param1, String param2) {
        ManageTaskFragment fragment = new ManageTaskFragment();
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
        View rootview = inflater.inflate(R.layout.fragment_manage_task, container, false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.taskList);
        taskAdapter = new ToDoAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        Bundle args = getArguments();
        int index = args.getInt("index", 0);
        loadData(index);

        return rootview;
    }

    public void loadData(int i) {
        mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                ///check
                if(userModel.getJobList()!=null && userModel.getJobList().size()>0)
                {
                    taskList = userModel.getJobList().get(i).getTaskList();
                    List<TaskModel> resultTaskList = new ArrayList<>();
                    for (TaskModel taskModel : taskList) {
                        if (taskModel.getIsDelete() != 1)
                            resultTaskList.add(taskModel);
                    }
                    recyclerView.setAdapter(taskAdapter);
                    taskAdapter.setTasks(resultTaskList);
                    System.out.println(userModel.getJobList().get(0).getTaskList().get(0).getTaskName());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.toException());
            }
        });
    }
}