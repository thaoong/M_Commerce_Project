package com.nguyenthithao.thestore;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.VoucherAdapter;
import com.nguyenthithao.model.Voucher;
import com.nguyenthithao.thestore.databinding.ActivityVoucherBinding;

public class VoucherActivity extends AppCompatActivity {
    ActivityVoucherBinding binding;
    VoucherAdapter voucherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_voucher);
        binding = ActivityVoucherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
//        addViews();
        voucherAdapter = new VoucherAdapter(VoucherActivity.this, R.layout.item_voucher);
        binding.lvVoucher.setAdapter(voucherAdapter);
        getVoucher();
        addEvents();
    }

    private void addEvents() {
        binding.lvVoucher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Voucher voucher = voucherAdapter.getItem(position);
                if (voucher != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("SELECTED_VOUCHER_CODE", voucher.getCode());
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    private void getVoucher() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("vouchers");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dss : snapshot.getChildren()){
                    Voucher voucher = dss.getValue(Voucher.class);
                    String key = dss.getKey();
                    voucher.setCode(key);
                    voucherAdapter.add(voucher);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strVoucher);
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
}