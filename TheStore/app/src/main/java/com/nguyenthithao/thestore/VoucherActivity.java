package com.nguyenthithao.thestore;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nguyenthithao.adapter.OrderHistoryAdapterTest;
import com.nguyenthithao.adapter.VoucherAdapter;
import com.nguyenthithao.model.OrderHistory;
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
    }

    private void addViews() {
        lvVoucher=findViewById(R.id.lvVoucher);
        dsVoucher=new ArrayList<>();
        adapterVoucher=new VoucherAdapter(VoucherActivity.this, R.layout.item_voucher, dsVoucher);
        lvVoucher.setAdapter(adapterVoucher);

        giaLapOrderHistory();
    }

    private void giaLapOrderHistory() {
//        dsOrderHistory.add(new OrderHistory("0003", "Chờ xác nhận", "12/4/2024", "Huỳnh Như", "123.000"));
//        dsOrderHistory.add(new OrderHistory("0002", "Đang giao hàng", "9/4/2024", "Thảo", "165.000"));
//        dsOrderHistory.add(new OrderHistory("0001", "Hoàn tất", "9/3/2024", "MK", "153.000"));
//
//
        dsVoucher.add(new Voucher("Giảm 50K", "500k", "THESTORE001", "30/04/2024"));
        dsVoucher.add(new Voucher("Giảm 100K", "1000k", "THESTORE002", "30/05/2024"));
        dsVoucher.add(new Voucher("Giảm 10K", "50k", "THESTORE003", "30/06/2024"));
        adapterVoucher.notifyDataSetChanged();

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