package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import vn.thanguit.toastperfect.ToastPerfect;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    private TextView fullName, changePass, userName;
    private TextInputLayout txtUserName, txtContact, txtEmail;
    private Button btnEdit, btnLogout;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private FirebaseFirestore fStore;
    private ImageView avatar;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        View rootview = inflater.inflate(R.layout.fragment_user, container, false);
        fullName = (TextView) rootview.findViewById(R.id.profile_name);
        changePass = (TextView) rootview.findViewById(R.id.changePassword);
        userName = (TextView) rootview.findViewById(R.id.profile_username);
        txtUserName = (TextInputLayout) rootview.findViewById(R.id.profile_user_name);
        txtContact = (TextInputLayout) rootview.findViewById(R.id.profile_contact);
        txtEmail = (TextInputLayout) rootview.findViewById(R.id.profile_email);
        btnEdit = (Button) rootview.findViewById(R.id.editProfile);
        btnLogout = (Button) rootview.findViewById(R.id.logoutButton);
        avatar = (ImageView) rootview.findViewById(R.id.profile_image);
        fStore = FirebaseFirestore.getInstance();

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn đăng xuất ?")
                        .setNegativeButton("Huỷ", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("Xác nhận", (dialogInterface, i) -> {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getContext(), Login.class);
                            startActivity(intent);
                        })
                        .show();
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputPass = new EditText(getContext());
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Nhập mật khẩu mới")
                        .setMessage("Bạn có chắc chắn muốn đổi mật khẩu?")
                        .setView(inputPass)
                        .setNegativeButton("Huỷ", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("Xác nhận", (dialogInterface, i) -> {
                            String newPass = inputPass.getText().toString().trim();
                            user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    ToastPerfect.makeText(getContext(), ToastPerfect.SUCCESS,
                                            "Thay đổi mật khẩu thành công "
                                            , ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(), Main.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    ToastPerfect.makeText(getContext(), ToastPerfect.ERROR,
                                            "Có lỗi, thay đổi mật khẩu không thành công "
                                            , ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .show();
            }
        });
        loadData();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
        return rootview;
    }

    private void loadData() {
        userName.setText(user.getEmail());
        txtEmail.getEditText().setText(user.getEmail());
        fStore.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fullName.setText(documentSnapshot.getString("fName"));
                txtUserName.getEditText().setText(documentSnapshot.getString("fName"));
                txtContact.getEditText().setText(documentSnapshot.getString("phone"));
            }
        });
    }

    private void updateData() {
        String userId = user.getUid();
        String email = user.getEmail();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        String name = txtUserName.getEditText().getText().toString().trim();
        String sdt = txtContact.getEditText().getText().toString().trim();
        if (sdt.length() < 10) {
            ToastPerfect.makeText(getContext(), ToastPerfect.ERROR,
                    "Sđt không đúng "
                    , ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("fName", name);
        user.put("phone", sdt);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                ToastPerfect.makeText(getContext(), ToastPerfect.SUCCESS,
                        "Cập nhật thông tin thành công "
                        , ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                Log.d("TAG", "On Success user update" + userId);
                loadData();
            }
        });
    }
}