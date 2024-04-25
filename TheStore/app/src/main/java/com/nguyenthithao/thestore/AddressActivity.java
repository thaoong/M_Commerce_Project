package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.AddressAdapter;
import com.nguyenthithao.model.Address;
import com.nguyenthithao.thestore.databinding.ActivityAddressBinding;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {
    ActivityAddressBinding binding;
    ListView lvAddress;
    List<Address> addressList;
    AddressAdapter adapter;
    private static final int REQUEST_CODE_ADD_ADDRESS = 101;
    private DatabaseReference addressRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();

        lvAddress = binding.lvAddress;
        addressList = new ArrayList<>();
        adapter = new AddressAdapter(this, R.layout.item_address, addressList);
        lvAddress.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            addressRef = FirebaseDatabase.getInstance().getReference().child("addresses").child(userId);

            Query query = addressRef.orderByKey();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    addressList.clear();
                    for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                        Address address = addressSnapshot.getValue(Address.class);
                        if (address != null) {
                            addressList.add(address);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AddressActivity.this, "Failed to load addresses.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS);
            }
        });

        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address selectedAddress = addressList.get(position);

                // Tạo Intent để chuyển đến màn hình chỉnh sửa
                Intent intent = new Intent(AddressActivity.this, EditAddressActivity.class);
                // Truyền địa chỉ được chọn qua Intent
                intent.putExtra("address", (CharSequence) selectedAddress);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ADDRESS && resultCode == RESULT_OK && data != null) {
            Address address = (Address) data.getSerializableExtra("address");
            if (address != null) {
                String addressId = addressRef.push().getKey();
                addressRef.child(addressId).setValue(address);
            }
        }
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>My Addresses</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}