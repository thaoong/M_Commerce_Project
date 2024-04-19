package com.nguyenthithao.thestore;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.nguyenthithao.adapter.OrderHistoryAdapterTest;
import com.nguyenthithao.model.OrderHistory;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {
    ListView lvOrderHistory;
    ArrayList<OrderHistory> dsOrderHistory;
    OrderHistoryAdapterTest adapterOrderHistory;
    TabHost tabHost;
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

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1=tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("Tất cả");
        tabHost.addTab(tab1);


        TabHost.TabSpec tab2=tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("Chờ xác nhận");
        tabHost.addTab(tab2);

        TabHost.TabSpec tab3=tabHost.newTabSpec("t3");
        tab3.setContent(R.id.tab3);
        tab3.setIndicator("Đang giao");
        tabHost.addTab(tab3);

        TabHost.TabSpec tab4=tabHost.newTabSpec("t4");
        tab4.setContent(R.id.tab4);
        tab4.setIndicator("Hoàn tất");
        tabHost.addTab(tab4);

        customizeTabs(tabHost);
    }

    private void customizeTabs(TabHost tabHost) {
        TabWidget tabWidget = tabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            LinearLayout tabLayout = (LinearLayout) tabWidget.getChildAt(i);
            TextView tabTextView = (TextView) tabLayout.getChildAt(1);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tabTextView.getLayoutParams();
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            tabTextView.setAllCaps(false);
            tabTextView.setTextSize(11);
            tabTextView.setTextColor(Color.parseColor("#5C3507"));

            tabTextView.setLayoutParams(layoutParams);
        }
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