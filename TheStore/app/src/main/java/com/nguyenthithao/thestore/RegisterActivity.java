package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nguyenthithao.thestore.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    String emailPattern = "[a-zA-Z0-9_-]+@[a-z]+\\. +[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideActionBar();
        haveAccount();
        addEvents();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerForAuth();
            }
        });
    }

    private void addEvents() {
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void PerForAuth() {
        String name = binding.edtInputUser.getText().toString();
        String email = binding.edtInputEmail.getText().toString();
        String password = binding.edtInputPassword.getText().toString();
        String conformPw = binding.edtConformPassword.getText().toString();

        if (name.isEmpty()){
            binding.edtInputUser.setError("Enter your name");
        }
        else
            if (email.matches(emailPattern)){
            binding.edtInputEmail.setError("Enter Context Email!");
        }
        else if (password.isEmpty() || password.length()<6){
            binding.edtInputPassword.setError("Enter Proper Password!");
        } else if (!    password.equals(conformPw)) {
            binding.edtConformPassword.setError("Password Not Match Both Field");
        }
        else {
            progressDialog.setMessage("Please wait while registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUser2NextActivity();
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void sendUser2NextActivity() {
        Intent intent = (new Intent(RegisterActivity.this, MainActivity.class));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void haveAccount() {
        binding.txtChange2L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    // Phương thức ẩn ActionBar
    private void hideActionBar() {
        getSupportActionBar().hide();
    }
}