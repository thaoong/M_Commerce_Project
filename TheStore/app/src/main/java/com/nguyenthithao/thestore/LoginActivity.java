package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.model.User;
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
        binding.edtPassword.setOnTouchListener(new PasswordTouchListener());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        addEvents();

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");
        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            binding.edtInputEmail.setText(savedEmail);
            binding.edtPassword.setText(savedPassword);
            binding.checkBox.setChecked(true);
        }

        loadCredentials();

    }
    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
    private void addEvents() {
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        binding.txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSignIn();
            }
        });

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

        binding.txtForgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Log in...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

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

        progressDialog.show();

        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            saveLoginInfo(userEmail, userPassword);

                            Toast.makeText(LoginActivity.this, "Login successfully" , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email or password is incorrect" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private class PasswordTouchListener implements View.OnTouchListener {
        private final Drawable drawableVisible = ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_visibility);
        private final Drawable drawableInvisible = ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_visibility_off);

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int drawableBounds = binding.edtPassword.getCompoundPaddingEnd();
            final int edittextBounds = binding.edtPassword.getRight();
            final int touchPosition = (int) event.getX();

            if (touchPosition >= edittextBounds - drawableBounds && event.getAction() == MotionEvent.ACTION_UP) {
                EditText editText = (EditText) v;
                if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_security),
                            null,
                            drawableVisible,
                            null
                    );
                } else {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_security),
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

    private void saveLoginInfo(String email, String password) {
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void saveCredentials(boolean isChecked) {
        if (isChecked) {
            editor.putString("email", binding.edtInputEmail.getText().toString().trim());
            editor.putString("password", binding.edtPassword.getText().toString().trim());
            editor.apply();
        } else {
            editor.remove("email");
            editor.remove("password");
            editor.apply();
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                progressDialog.setMessage("Log in...");
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
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Google sign in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

            // Check if the user's UID already exists as a child of the databaseReference
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User data already exists, show a success message
                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // User data doesn't exist, save it to the database
                        databaseReference.child(userId).setValue(userData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during the database operation
                    Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void loadCredentials() {
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");
        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            binding.edtInputEmail.setText(savedEmail);
            binding.edtPassword.setText(savedPassword);
            binding.checkBox.setChecked(true);
        }
    }
}
