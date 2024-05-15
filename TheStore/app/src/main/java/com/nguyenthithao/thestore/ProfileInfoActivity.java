package com.nguyenthithao.thestore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.thestore.databinding.ActivityProfileInfoBinding;

import java.util.Calendar;

public class ProfileInfoActivity extends AppCompatActivity {
    ActivityProfileInfoBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile_info);
        binding = ActivityProfileInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        populateUserInfo();
        addEvents();
    }

    private void populateUserInfo() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        String userName = dataSnapshot.child("name").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phone").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String dateOfBirth = dataSnapshot.child("date_of_birth").getValue(String.class);
                        String gender = dataSnapshot.child("gender").getValue(String.class);

                        binding.edtName.setText(userName);
                        binding.edtPhone.setText(phoneNumber);
                        binding.edtEmail.setText(email);
                        binding.edtDateOfBirth.setText(dateOfBirth);

                        if (gender != null && gender.equals("Male")) {
                            binding.radMale.setChecked(true);
                        } else if (gender != null && gender.equals("Female")) {
                            binding.radFemale.setChecked(true);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }
    private void addEvents() {
        binding.btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfileInfo();
            }
        });
    }

    private void saveUserProfileInfo() {
        String name = binding.edtName.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String dateOfBirth = binding.edtDateOfBirth.getText().toString().trim();

        int selectedRadioButtonId = binding.radioGroupGender.getCheckedRadioButtonId();
        String gender = "";
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            gender = selectedRadioButton.getText().toString();
        }

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
            databaseReference.child("name").setValue(name);
            databaseReference.child("phone").setValue(phone);
            databaseReference.child("email").setValue(email);
            databaseReference.child("date_of_birth").setValue(dateOfBirth);
            databaseReference.child("gender").setValue(gender);
        }

        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

        Toast.makeText(ProfileInfoActivity.this, "Profile information saved successfully", Toast.LENGTH_SHORT).show();
    }


    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strProfileInfo);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>"+title+"</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dateOfBirth = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                binding.edtDateOfBirth.setText(dateOfBirth);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
}