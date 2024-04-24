package com.nguyenthithao.thestore;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.OrderHistoryAdapterTest;
import com.nguyenthithao.adapter.VoucherAdapter;
import com.nguyenthithao.model.OrderHistory;
import com.nguyenthithao.model.PaymentMethod;
import com.nguyenthithao.model.Voucher;

import java.util.ArrayList;

public class VoucherActivity extends AppCompatActivity {
    ListView lvVoucher;
    ArrayList<Voucher> dsVoucher;
    VoucherAdapter adapterVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        displayActionBar();
        addViews();
        //addEvents();
    }

//    private void addEvents() {
//        lvVoucher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Voucher voucher = adapterVoucher.getItem(position);
//                if (voucher != null) {
//                    Intent returnIntent = new Intent();
//                    returnIntent.putExtra("SELECTED_VOUCHER", voucher.getCode());
//                    setResult(RESULT_OK, returnIntent);
//                    finish();
//                }
//            }
//        });
//    }

    private void addViews() {
        lvVoucher=findViewById(R.id.lvVoucher);
        dsVoucher=new ArrayList<>();
        adapterVoucher=new VoucherAdapter(VoucherActivity.this, R.layout.item_voucher);
        lvVoucher.setAdapter(adapterVoucher);
        getVoucher();
    }

    private void getVoucher() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("vouchers");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapterVoucher.clear();
                for (DataSnapshot dss : snapshot.getChildren()){
                    Voucher voucher = dss.getValue(Voucher.class);
                    String key = dss.getKey();
                    voucher.setCode(key);
                    adapterVoucher.add(voucher);
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
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Ví Voucher</font>"));
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