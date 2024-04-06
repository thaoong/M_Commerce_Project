package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyenthithao.models.User;
import com.nguyenthithao.thestore.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://thestore-55f0f-default-rtdb.firebaseio.com/");

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
        String phone = binding.edtInputPhone.getText().toString();
        String password = binding.edtInputPassword.getText().toString();
        String conformPw = binding.edtConformPassword.getText().toString();
        String dob = binding.edtInputDOB.getText().toString();

        // Kiểm tra các trường thông tin nhập vào
        if (name.isEmpty()) {
            binding.edtInputUser.setError("Enter your name");
            return;
        }
        if (!email.matches(emailPattern)) {
            binding.edtInputEmail.setError("Enter a valid email address");
            return;
        }
        if (password.isEmpty() || password.length() < 6) {
            binding.edtInputPassword.setError("Enter Proper Password!");
            return;
        }
        if (!password.equals(conformPw)) {
            binding.edtConformPassword.setError("Password does not match!");
            return;
        }
        if (dob.isEmpty()) {
            binding.edtInputDOB.setError("Enter your birthday!");
            return;
        }

        // Hiển thị hộp thoại ProgressDialog
        progressDialog.setMessage("Please wait while registering...");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // Thực hiện đăng ký tài khoản trong Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng ký thành công, gửi dữ liệu đến Realtime Database
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();

                            // Tạo một đối tượng User và đưa dữ liệu vào
                            User newUser = new User(name, email, password, dob, phone);
                            databaseReference.child("users").child(userId).setValue(newUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Gửi dữ liệu thành công, chuyển đến màn hình chính
                                                progressDialog.dismiss();
                                                sendUser2NextActivity();
                                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Đăng ký thất bại, hiển thị thông báo lỗi
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
