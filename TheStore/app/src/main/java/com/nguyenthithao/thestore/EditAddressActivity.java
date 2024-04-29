package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.model.Address;
import com.nguyenthithao.model.User;
import com.nguyenthithao.thestore.databinding.ActivityEditAddressBinding;

import java.util.ArrayList;
import java.util.List;

public class EditAddressActivity extends AppCompatActivity {
    ActivityEditAddressBinding binding;
    DatabaseReference userRef;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_address);
        binding = ActivityEditAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        getAddress();
        addEvents();

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
    }

    private void getAddress() {
        Intent intent = getIntent();
        Address address = (Address) intent.getSerializableExtra("SELECTED_ADDRESS");

        if (address != null) {
            binding.edtName.setText(address.getName());
            binding.edtPhone.setText(address.getPhone());
            binding.edtProvince.setText(address.getProvince());
            binding.edtDistrict.setText(address.getDistrict());
            binding.edtWard.setText(address.getWard());
            binding.edtStreet.setText(address.getStreet());
        }
    }

    private void addEvents() {
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                currentUser = dataSnapshot.getValue(User.class);
//                if (currentUser == null) {
//                    // Người dùng không tồn tại, xử lý tùy ý
//                } else {
//                    // Hiển thị thông tin địa chỉ của người dùng (nếu cần)
//                    // Ví dụ: binding.txtAddress.setText(currentUser.getAddresses());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý khi có lỗi xảy ra khi đọc dữ liệu từ cơ sở dữ liệu
//            }
//        });

        binding.btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strEditAddress);
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

    private void saveAddress() {
        String name = binding.edtName.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();
        String province = binding.edtProvince.getText().toString().trim();
        String district = binding.edtDistrict.getText().toString().trim();
        String ward = binding.edtWard.getText().toString().trim();
        String street = binding.edtStreet.getText().toString().trim();
        boolean isDefault = binding.chkDefaultAddress.isChecked();

        if (!name.isEmpty() && !phone.isEmpty() && !province.isEmpty() && !district.isEmpty() && !ward.isEmpty() && !street.isEmpty()) {
            // Create a new Address object
            com.nguyenthithao.model.Address address = new com.nguyenthithao.model.Address(name, phone, province, district, ward, street, isDefault);
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("addresses").child(userId).push();
            addressRef.setValue(address)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditAddressActivity.this, "Address saved successfully", Toast.LENGTH_SHORT).show();
//                                startActivity(EditAddressActivity.this, AddressActivity.class);
                            } else {
                                Toast.makeText(EditAddressActivity.this, "Failed to save address: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }


}