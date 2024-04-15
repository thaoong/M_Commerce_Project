package com.nguyenthithao.thestore;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.nguyenthithao.adapter.BookAdapterTest;
import com.nguyenthithao.adapter.OrderHistoryAdapterTest;
import com.nguyenthithao.model.OrderHistory;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {
    ListView lvOrderHistory;
    ArrayList<OrderHistory> dsOrderHistory;
    OrderHistoryAdapterTest adapterOrderHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        displayActionBar();
        addViews();
    }

    private void addViews() {
        lvOrderHistory=findViewById(R.id.lvOrderHistory);
        dsOrderHistory=new ArrayList<>();
        adapterOrderHistory=new OrderHistoryAdapterTest(OrderHistoryActivity.this, R.layout.item_order_history, dsOrderHistory);
        lvOrderHistory.setAdapter(adapterOrderHistory);

        giaLapOrderHistory();
    }

    private void giaLapOrderHistory() {

        dsOrderHistory.add(new OrderHistory("0003", "Chờ xác nhận", "12/4/2024", "Huỳnh Như", "123.000"));
        dsOrderHistory.add(new OrderHistory("0002", "Đang giao hàng", "9/4/2024", "Thảo", "165.000"));
        dsOrderHistory.add(new OrderHistory("0001", "Hoàn tất", "9/3/2024", "MK", "153.000"));

        adapterOrderHistory.notifyDataSetChanged();
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Lịch sử mua hàng</font>"));
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