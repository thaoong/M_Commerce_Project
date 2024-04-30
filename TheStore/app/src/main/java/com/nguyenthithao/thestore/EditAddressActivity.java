package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
    private boolean hidden;
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
            binding.chkDefaultAddress.setChecked(address.isDefault());
        }
    }

    private void addEvents() {

        binding.btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });

        binding.btnDeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAddress();
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
        boolean isDefault = binding.chkDefaultAddress.isChecked(); // Lấy trạng thái mặc định từ checkbox

        // Kiểm tra các trường nhập liệu có hợp lệ không
        if (!name.isEmpty() && !phone.isEmpty() && !province.isEmpty() && !district.isEmpty() && !ward.isEmpty() && !street.isEmpty()) {
            // Lấy địa chỉ cần chỉnh sửa từ intent
            Intent intent = getIntent();
            Address address = (Address) intent.getSerializableExtra("SELECTED_ADDRESS");
            if (address != null) {
                // Cập nhật thông tin của địa chỉ
                address.setName(name);
                address.setPhone(phone);
                address.setProvince(province);
                address.setDistrict(district);
                address.setWard(ward);
                address.setStreet(street);
                address.setDefault(isDefault); // Cập nhật trạng thái mặc định mới

                // Lưu cập nhật vào cơ sở dữ liệu Firebase
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("addresses").child(userId).child(address.getAddressId());
                addressRef.removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditAddressActivity.this, "Address deleted successfully", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    Toast.makeText(EditAddressActivity.this, "Failed to delete address: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Failed to retrieve address information", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditAddressActivity.this);
        builder.setTitle("Delete Address");
        builder.setMessage("Are you sure you want to delete this address?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAddressFromDatabase();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.brown_text));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.brown_text));
            }
        });
        dialog.show();
    }

    private void deleteAddressFromDatabase() {
        Intent intent = getIntent();
        Address address = (Address) intent.getSerializableExtra("SELECTED_ADDRESS");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("addresses").child(userId).child(address.getAddressId());
        addressRef.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditAddressActivity.this, "Address deleted successfully", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(EditAddressActivity.this, "Failed to delete address: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}