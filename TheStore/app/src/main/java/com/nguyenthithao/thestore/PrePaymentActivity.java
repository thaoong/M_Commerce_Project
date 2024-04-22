package com.nguyenthithao.thestore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.nguyenthithao.adapter.MyReviewAdapterTest;
import com.nguyenthithao.adapter.OrderBookAdapter;
import com.nguyenthithao.model.CartItem;
import com.nguyenthithao.model.MyReviewTest;
import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.thestore.databinding.ActivityPrePaymentBinding;

import java.util.ArrayList;
import java.util.Objects;

public class PrePaymentActivity extends AppCompatActivity {
    ActivityPrePaymentBinding binding;
    private OrderBookAdapter orderBookAdapter;
    ArrayList<OrderBook> orderBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pre_payment);
        binding = ActivityPrePaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        addEvent();
        getSelectedBookFromCart();
    }

    private void getSelectedBookFromCart() {
        ArrayList<CartItem> selectedItems = getIntent().getParcelableArrayListExtra("selectedItems");
        orderBooks = new ArrayList<>();
        for (CartItem item : selectedItems) {
            // Tạo đối tượng OrderBook từ CartItem và thêm vào danh sách orderBooks
            OrderBook orderBook = new OrderBook();
            orderBook.setImageLink(item.getImageLink());
            orderBook.setName(item.getName());
            orderBook.setUnitPrice(item.getUnitPrice());
            orderBook.setOldPrice(item.getOldPrice());
            orderBook.setQuantity(item.getQuantity());
            orderBooks.add(orderBook);
        }
        orderBookAdapter = new OrderBookAdapter(this, R.layout.item_order_book, orderBooks);
        binding.lvBook.setAdapter(orderBookAdapter);
    }

    private void addEvent() {
        binding.btnChangePaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrePaymentActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Thanh toán</font>"));
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