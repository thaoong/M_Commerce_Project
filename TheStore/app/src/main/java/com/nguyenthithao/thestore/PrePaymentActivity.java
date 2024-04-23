package com.nguyenthithao.thestore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.nguyenthithao.adapter.OrderBookAdapter;
import com.nguyenthithao.model.CartItem;
import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.thestore.databinding.ActivityPrePaymentBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PrePaymentActivity extends AppCompatActivity {
    ActivityPrePaymentBinding binding;
    Intent intent, intent1, intent2;
    private OrderBookAdapter orderBookAdapter;
    ArrayList<OrderBook> orderBooks;
    public float prePrice;
    private float shippingFee;
    private float discount;
    private float total;
    private String selectedPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pre_payment);
        binding = ActivityPrePaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        getSelectedBookFromCart();
        calculatePrePrice();
        addEvent();
    }

    private void calculatePrePrice() {
        prePrice = 0;
        for (OrderBook orderBook : orderBooks) {
            double unitPrice = orderBook.getUnitPrice();
            int quantity = orderBook.getQuantity();
            double itemTotalPrice = unitPrice * quantity;
            prePrice += itemTotalPrice;
        }
        binding.txtPrePrice.setText(formatCurrency(prePrice)+"đ");
    }

    private void getSelectedBookFromCart() {
        intent = getIntent();
        ArrayList<CartItem> selectedItems = intent.getParcelableArrayListExtra("selectedItems");
        orderBooks = new ArrayList<>();
        assert selectedItems != null;
        for (CartItem item : selectedItems) {
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
                Intent intent = new Intent(PrePaymentActivity.this, PaymentMethodActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        shippingFee = 30000;
        binding.txtShippingFee.setText(formatCurrency(shippingFee)+"đ");

        discount = 10000;
        binding.txtDiscount.setText("-"+formatCurrency(discount)+"đ");

        total = prePrice + shippingFee - discount;
        binding.txtTotal.setText(formatCurrency(total) + "đ");

        intent1 = getIntent();
        selectedPaymentMethod = intent1.getStringExtra("SELECTED_PAYMENT_METHOD");
        binding.txtPaymentMethod.setText(selectedPaymentMethod);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selectedPaymentMethod = data.getStringExtra("SELECTED_PAYMENT_METHOD");
            binding.txtPaymentMethod.setText(selectedPaymentMethod);
        }
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

    private String formatCurrency(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(value);
    }
}