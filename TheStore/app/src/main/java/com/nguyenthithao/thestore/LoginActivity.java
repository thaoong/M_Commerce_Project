package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.thestore.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    private static final int RC_SIGN_IN = 101;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo SharedPreferences để lưu thông tin đăng nhập
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Kiểm tra và tự động điền thông tin đăng nhập đã lưu trữ
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");
        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            binding.edtInputEmail.setText(savedEmail);
            binding.edtPassword.setText(savedPassword);
            binding.checkBox.setChecked(true);
        }

        // Thiết lập các sự kiện và định cấu hình
        setup();
    }

    private void setup() {
        getSupportActionBar().hide(); // Ẩn ActionBar

        // Khởi tạo Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo Firebase Database
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        // Cấu hình đăng nhập Google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // Đăng ký sự kiện khi nhấn vào nút Đăng ký
        binding.txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // Đăng ký sự kiện khi nhấn vào nút Đăng nhập
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSignIn();
            }
        });

        // Đăng ký sự kiện khi thay đổi trạng thái của CheckBox
        binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveCredentials(isChecked);
            }
        });

        binding.imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Google sign in...");
    }

    // Phương thức xử lý đăng nhập
    private void validateAndSignIn() {
        String userEmail = binding.edtInputEmail.getText().toString().trim();
        String userPassword = binding.edtPassword.getText().toString().trim();

        if (userEmail.isEmpty()) {
            binding.edtInputEmail.setError("Email cannot be empty");
            return;
        }
        if (userPassword.isEmpty()) {
            binding.edtPassword.setError("Password cannot be empty");
            return;
        }

        // Hiển thị hộp thoại ProgressDialog
        progressDialog.show();

        // Đăng nhập Firebase Authentication
        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss(); // Đóng ProgressDialog sau khi xử lý hoàn tất

                        if (task.isSuccessful()) {
                            // Đăng nhập thành công, chuyển đến MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Kết thúc LoginActivity để ngăn người dùng quay lại

                            // Hiển thị thông báo đăng nhập thành công
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // Đăng nhập thất bại, hiển thị thông báo lỗi
                            Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Phương thức lưu thông tin đăng nhập vào SharedPreferences
    private void saveCredentials(boolean isChecked) {
        if (isChecked) {
            // Lưu thông tin đăng nhập vào SharedPreferences
            editor.putString("email", binding.edtInputEmail.getText().toString().trim());
            editor.putString("password", binding.edtPassword.getText().toString().trim());
            editor.apply();
        } else {
            // Xóa thông tin đăng nhập khỏi SharedPreferences nếu CheckBox không được chọn
            editor.remove("email");
            editor.remove("password");
            editor.apply();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Kiểm tra xem người dùng đã đăng nhập trước đó chưa khi mở ứng dụng
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Nếu đã đăng nhập trước đó, chuyển đến MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish(); // Kết thúc LoginActivity để ngăn người dùng quay lại
        }
    }


    // Phương thức xử lý đăng nhập bằng Google
    private void signInWithGoogle() {
        // Kiểm tra xem người dùng đã đăng nhập trước đó chưa bằng cách gọi phương thức silentSignIn()
        Task<GoogleSignInAccount> task = mGoogleSignInClient.silentSignIn();
        if (task.isSuccessful()) {
            // Nếu đã đăng nhập trước đó, thực hiện xác thực
            GoogleSignInAccount account = task.getResult();
            firebaseAuthWithGoogle(account.getIdToken());
        } else {
            // Nếu chưa đăng nhập trước đó, mở hộp thoại chọn tài khoản Google để đăng nhập
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    // Xử lý kết quả sau khi đăng nhập bằng Google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng nhập bằng Google thành công, chuyển đến MainActivity
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Google sign in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
