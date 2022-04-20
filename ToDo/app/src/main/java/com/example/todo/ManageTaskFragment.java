package com.example.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Adapter.ToDoAdapter;
import com.example.todo.Model.TaskModel;

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
    private RecyclerView recyclerView;
    private ToDoAdapter taskAdapter;
    private List<TaskModel> taskList;
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

        taskList = new ArrayList<>();

        recyclerView = (RecyclerView) rootview.findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskAdapter = new ToDoAdapter(this);
        recyclerView.setAdapter(taskAdapter);


        TaskModel task = new TaskModel();
        task.setTask("task 1");
        task.setStatus(0);
        task.setId(1);

        TaskModel task2 = new TaskModel();
        task2.setTask("task 2");
        task2.setStatus(1);
        task2.setId(2);

        TaskModel task3 = new TaskModel();
        task3.setTask("task 3");
        task3.setStatus(1);
        task3.setId(3);

        TaskModel task4 = new TaskModel();
        task4.setTask("task 4");
        task4.setStatus(1);
        task4.setId(4);

        taskList.add(task);
        taskList.add(task2);
        taskList.add(task3);
        taskList.add(task4);


        taskAdapter.setTasks(taskList);


        return rootview;
    }
}