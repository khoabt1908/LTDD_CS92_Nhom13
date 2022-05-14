package com.example.todo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Adapter.SearchAdapter;
import com.example.todo.Model.TaskModel;
import com.example.todo.Model.UserModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabase;
    private List<TaskModel> taskList;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private TextInputLayout txtKeyWord;
    private TextInputEditText txtKey;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View rootview = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.listTaskSearch);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        searchAdapter = new SearchAdapter(this);
        txtKeyWord = (TextInputLayout) rootview.findViewById(R.id.keyWord);
        txtKey = (TextInputEditText) rootview.findViewById(R.id.txtKey);
        txtKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadData(txtKey.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return rootview;
    }

    public void loadData(String keyWord) {
        mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);

                if (userModel != null && userModel.getJobList() != null && userModel.getJobList().size() > 0) {
                    List<TaskModel> resultTaskList = new ArrayList<>();
                    if(keyWord.isEmpty())
                    {
                        recyclerView.setAdapter(searchAdapter);
                        searchAdapter.setTasks(resultTaskList);
                        return;
                    }
                    for (int j = 0; j < userModel.getJobList().size(); j++) {
                        taskList = userModel.getJobList().get(j).getTaskList();
                        if (taskList != null && taskList.size() > 0) {
                            for (TaskModel taskModel : taskList) {
                                if (taskModel.getTaskName().toLowerCase(Locale.ROOT).indexOf(keyWord.toLowerCase(Locale.ROOT)) != -1)
                                    resultTaskList.add(taskModel);
                            }
                        }
                    }
                    recyclerView.setAdapter(searchAdapter);
                    searchAdapter.setTasks(resultTaskList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}