package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nguyenthithao.thestore.databinding.ActivityCreateNewPasswordBinding;

public class CreateNewPasswordActivity extends AppCompatActivity {
    ActivityCreateNewPasswordBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        binding.edtInputPassword.setOnTouchListener(new PasswordTouchListener());
        binding.edtConfirmPassword.setOnTouchListener(new PasswordTouchListener());
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private class PasswordTouchListener implements View.OnTouchListener {
        private final Drawable drawableVisible = ContextCompat.getDrawable(CreateNewPasswordActivity.this, R.drawable.ic_visibility);
        private final Drawable drawableInvisible = ContextCompat.getDrawable(CreateNewPasswordActivity.this, R.drawable.ic_visibility_off);

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
                            ContextCompat.getDrawable(CreateNewPasswordActivity.this, R.drawable.ic_security),
                            null,
                            drawableVisible,
                            null
                    );
                } else {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            ContextCompat.getDrawable(CreateNewPasswordActivity.this, R.drawable.ic_security),
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

    private void resetPassword() {
        String newPassword = binding.edtInputPassword.getText().toString().trim();
        String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newPassword)) {
            binding.edtInputPassword.setError("Please enter a new password");
            binding.edtInputPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            binding.edtConfirmPassword.setError("Please confirm the new password");
            binding.edtConfirmPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            binding.edtConfirmPassword.setError("Passwords do not match");
            binding.edtConfirmPassword.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(CreateNewPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(CreateNewPasswordActivity.this, "Failed to update password: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(CreateNewPasswordActivity.this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }
}