package com.nguyenthithao.thestore;

import android.os.Bundle;
import android.text.Html;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.nguyenthithao.adapter.OrderDetailAdapterTest;
import com.nguyenthithao.model.OrderDetailTest;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {

    ListView lvOrderDetail;
    ArrayList<OrderDetailTest> dsOrderDetail;
    OrderDetailAdapterTest adapterOrderDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        displayActionBar();
        addViews();
    }

    private void addViews() {
        lvOrderDetail=findViewById(R.id.lvOrderDetail);
        dsOrderDetail=new ArrayList<>();
        adapterOrderDetail=new OrderDetailAdapterTest(OrderDetailActivity.this, R.layout.item_order_book, dsOrderDetail);
        lvOrderDetail.setAdapter(adapterOrderDetail);

//        giaLapOrderDetail();
    }

//    private void giaLapOrderDetail() {
//        dsOrderDetail.add(new OrderDetailTest("Hoàng tử bé", "112.000đ", 2));
//        dsOrderDetail.add(new OrderDetailTest("Doraemon", "117.000đ", 3));
//        dsOrderDetail.add(new OrderDetailTest("Hoàng tử lớn", "12.000đ", 1));
//
//        adapterOrderDetail.notifyDataSetChanged();
//    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Chi tiết đơn hàng</font>"));
    }
}