package com.nguyenthithao.thestore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.nguyenthithao.adapter.MyReviewAdapterTest;
import com.nguyenthithao.adapter.OrderDetailAdapterTest;
import com.nguyenthithao.model.MyReviewTest;
import com.nguyenthithao.model.OrderDetailTest;

import java.util.ArrayList;

public class MyReviewActivity extends AppCompatActivity {
    //ActivityMyReviewBinding binding;
    ListView lvOrderDetail;
    ArrayList<OrderDetailTest> dsOrderDetail;

    ListView lvRated;
    ArrayList<MyReviewTest> dsMyReview;
    TabHost tabHost;
    OrderDetailAdapterTest adapterOrderDetail;
    MyReviewAdapterTest adapterMyReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);
        //binding = ActivityMyReviewBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        displayActionBar();
        addViews();
    }

    private void addViews() {
        lvOrderDetail = findViewById(R.id.lvOrderDetail);
        lvOrderDetail.setNestedScrollingEnabled(false);

        dsOrderDetail=new ArrayList<>();
        adapterOrderDetail = new OrderDetailAdapterTest(MyReviewActivity.this, R.layout.item_order_book, dsOrderDetail);
        lvOrderDetail.setAdapter(adapterOrderDetail);

        lvRated=findViewById(R.id.lvRated);
        dsMyReview=new ArrayList<>();
        adapterMyReview=new MyReviewAdapterTest(MyReviewActivity.this, R.layout.item_rated, dsMyReview);
        lvRated.setAdapter(adapterMyReview);

        giaLapOrderHistory();
        giaLapMyReview();

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1=tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("Chưa đánh giá");
        tabHost.addTab(tab1);


        TabHost.TabSpec tab2=tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("Đã đánh giá");
        tabHost.addTab(tab2);

        customizeTabs(tabHost);
    }


        private void giaLapMyReview() {
            dsMyReview.add(new MyReviewTest("Sách", "12/12/2023", "sách ý nghĩa", "14/12/2023", 4, 2));
            dsMyReview.add(new MyReviewTest("Sách", "12/12/2023", "sách ý nghĩa", "14/12/2023", 4, 2));
            dsMyReview.add(new MyReviewTest("Sách", "12/12/2023", "sách ý nghĩa", "14/12/2023", 4, 2));

            adapterMyReview.notifyDataSetChanged();
        }





    private void giaLapOrderHistory() {
        dsOrderDetail.add(new OrderDetailTest("Hoàng tử bé", "112.000đ", 2));
        dsOrderDetail.add(new OrderDetailTest("Doraemon", "117.000đ", 3));
        dsOrderDetail.add(new OrderDetailTest("Hoàng tử lớn", "12.000đ", 1));

        adapterOrderDetail.notifyDataSetChanged();

    }
    private void customizeTabs(TabHost tabHost) {
        TabWidget tabWidget = tabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            LinearLayout tabLayout = (LinearLayout) tabWidget.getChildAt(i);
            TextView tabTextView = (TextView) tabLayout.getChildAt(1);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tabTextView.getLayoutParams();
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            tabTextView.setTextColor(Color.parseColor("#5C3507"));

            tabTextView.setLayoutParams(layoutParams);
        }
    }
    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strMyReview);
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