package com.nguyenthithao.thestore;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nguyenthithao.thestore.databinding.ActivityConfirmPasswordBinding;

public class ConfirmPasswordActivity extends AppCompatActivity {
    ActivityConfirmPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_confirm_password);
        binding = ActivityConfirmPasswordBinding.inflate(getLayoutInflater());
        binding.edtPassword.setOnTouchListener(new PasswordTouchListener());
        setContentView(binding.getRoot());
        displayActionBar();
        addEvents();
    }

    private class PasswordTouchListener implements View.OnTouchListener {
        private final Drawable drawableVisible = ContextCompat.getDrawable(ConfirmPasswordActivity.this, R.drawable.ic_visibility);
        private final Drawable drawableInvisible = ContextCompat.getDrawable(ConfirmPasswordActivity.this, R.drawable.ic_visibility_off);

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
                            ContextCompat.getDrawable(ConfirmPasswordActivity.this, R.drawable.ic_security),
                            null,
                            drawableVisible,
                            null
                    );
                } else {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            ContextCompat.getDrawable(ConfirmPasswordActivity.this, R.drawable.ic_security),
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

    private void addEvents() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        binding.btnConfirmPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = binding.edtPassword.getText().toString().trim();

                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String userEmail = currentUser.getEmail();

                    mAuth.signInWithEmailAndPassword(userEmail, enteredPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(ConfirmPasswordActivity.this, CreateNewPasswordActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(ConfirmPasswordActivity.this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(ConfirmPasswordActivity.this, "Please log in first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Verify by Password</font>"));
    }
}