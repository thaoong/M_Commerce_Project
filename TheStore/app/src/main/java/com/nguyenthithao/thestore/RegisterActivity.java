package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.model.User;
import com.nguyenthithao.thestore.databinding.ActivityRegisterBinding;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    boolean isAgreed=false;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://thestore-55f0f-default-rtdb.firebaseio.com/");
    private static final int RC_SIGN_IN = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addEvents();
    }

    private void addEvents() {
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        binding.edtInputPassword.setOnTouchListener(new PasswordTouchListener());
        binding.edtConfirmPassword.setOnTouchListener(new PasswordTouchListener());

        getSupportActionBar().hide();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerForAuth();
            }
        });

        binding.txtChange2L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // Xử lý sự kiện click vào ImageView đăng ký bằng Google
        binding.imgGoogleRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    private void signInWithGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void PerForAuth() {
        String name = binding.edtInputUser.getText().toString();
        String email = binding.edtInputEmail.getText().toString();
        String phone = binding.edtInputPhone.getText().toString();
        String password = binding.edtInputPassword.getText().toString();
        String confirmPw = binding.edtConfirmPassword.getText().toString();

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
        if (!password.equals(confirmPw)) {
            binding.edtConfirmPassword.setError("Password does not match!");
            return;
        }

        if (!isAgreed) {
            Toast.makeText(this, "Please agree to terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Please wait while registering...");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        databaseReference.child("users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Email has already been registered!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String userId = user.getUid();

                                        HashMap<String, Object> userMap = new HashMap<>();
                                        userMap.put("name", name);
                                        userMap.put("email", email);
                                        userMap.put("password", password);
                                        userMap.put("phone", phone);

                                        databaseReference.child("users").child(userId).setValue(userMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                            sendUser2NextActivity();
                                                        } else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(RegisterActivity.this, "Registration failed ", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Registration failed ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendUser2NextActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void AcceptActivity(View view) {
        CheckBox checkBox = (CheckBox) view;
        isAgreed = checkBox.isChecked();
        if (isAgreed) {
            binding.btnRegister.setEnabled(true);
        } else {
            binding.btnRegister.setEnabled(false);
        }
    }

    public void txtLogin(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private class PasswordTouchListener implements View.OnTouchListener {
        private final Drawable drawableVisible = ContextCompat.getDrawable(RegisterActivity.this, R.drawable.ic_visibility);
        private final Drawable drawableInvisible = ContextCompat.getDrawable(RegisterActivity.this, R.drawable.ic_visibility_off);

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int drawableBounds = binding.edtInputPassword.getCompoundPaddingEnd();
            final int edittextBounds = binding.edtInputPassword.getRight();
            final int touchPosition = (int) event.getX();

            if (touchPosition >= edittextBounds - drawableBounds && event.getAction() == MotionEvent.ACTION_UP) {
                EditText editText = (EditText) v;
                if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            ContextCompat.getDrawable(RegisterActivity.this, R.drawable.ic_security),
                            null,
                            drawableVisible,
                            null
                    );
                } else {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            ContextCompat.getDrawable(RegisterActivity.this, R.drawable.ic_security),
                            null,
                            drawableInvisible,
                            null
                    );
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please wait while registering...");
                progressDialog.setTitle("Registration");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                firebaseAuthWithGoogle(account.getIdToken(), progressDialog);
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken, ProgressDialog progressDialog) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Lưu thông tin người dùng vào Realtime Database
                            saveUserDataToDatabase(user);
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Google sign in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserDataToDatabase(FirebaseUser user) {
        if (user != null) {
            String userId = user.getUid();
            String userEmail = user.getEmail();
            String userName = user.getDisplayName();
            User userData = new User(userName, userEmail, "", "", "", "");

            databaseReference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User data already exists, show a success message
                        Toast.makeText(RegisterActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // User data doesn't exist, save it to the database
                        databaseReference.child("users").child(userId).setValue(userData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(RegisterActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during the database operation
                    Toast.makeText(RegisterActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}