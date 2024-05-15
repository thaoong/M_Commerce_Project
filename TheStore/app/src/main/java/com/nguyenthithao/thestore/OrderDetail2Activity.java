package com.nguyenthithao.thestore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.OrderBookAdapter;
import com.nguyenthithao.model.Order;
import com.nguyenthithao.model.OrderBook;

import java.util.ArrayList;

public class OrderDetail2Activity extends AppCompatActivity {

    private ListView lvOrderDetail;
    private TextView txtOrderID, txtOrderStatus, txtDate, txtDateReceive, txtBookQuantity, txtTotalPrice, txtCustomerName, txtCustomerPhone, txtCustomerAddress, txtPaymentMethod, txtTemporary, txtShippingFee, txtDiscount, txtTotalMoney;
    private Button btnBuyAgain;
    private Order order;
    private String orderKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail2);
        displayActionBar();
        addViews();

        orderKey = getIntent().getStringExtra("orderKey");
        txtOrderID.setText(orderKey);

        btnBuyAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetail2Activity.this, Prepayment2Activity.class);
                intent.putExtra("order", order);
                intent.putExtra("orderKey", orderKey);
                startActivity(intent);
            }
        });

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child(orderKey);
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                order = dataSnapshot.getValue(Order.class);

                if (order != null) {
                    txtOrderStatus.setText(order.getStatus());
                    txtDate.setText(order.getOrderDate());
                    txtDateReceive.setText(order.getReceivedDate());
                    txtBookQuantity.setText(String.valueOf(order.getOrderBooks().size()));
                    txtTotalPrice.setText(formatPrice(order.getTotal()));
                    txtCustomerName.setText(order.getName());
                    txtCustomerPhone.setText(order.getPhone());
                    txtCustomerAddress.setText(order.getStreet() + ", " + order.getWard() + ", " + order.getDistrict() + ", " + order.getProvince());
                    txtPaymentMethod.setText(order.getPaymentMethod());
                    txtTemporary.setText(formatPrice(order.getPrePrice()));
                    txtShippingFee.setText(formatPrice(order.getShippingFee()));
                    txtDiscount.setText(formatPrice(order.getDiscount()));
                    txtTotalMoney.setText(formatPrice(order.getTotal()));

                    ArrayList<OrderBook> orderBooks = order.getOrderBooks();
                    OrderBookAdapter adapter = new OrderBookAdapter(OrderDetail2Activity.this, R.layout.item_order_book, orderBooks);
                    lvOrderDetail.setAdapter(adapter);
                } else {
                    Toast.makeText(OrderDetail2Activity.this, "No order data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderDetail2Activity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strOrderDetail);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>"+title+"</font>"));
    }

    private void addViews() {
        lvOrderDetail = findViewById(R.id.lvOrderDetail);
        txtOrderID = findViewById(R.id.txtOrderID);
        txtOrderStatus = findViewById(R.id.txtOrderStatus);
        txtDate = findViewById(R.id.txtDate);
        txtDateReceive = findViewById(R.id.DateReceive);
        txtBookQuantity = findViewById(R.id.txtBookQuantity);
        txtTotalPrice = findViewById(R.id.TotalPrice);
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtCustomerPhone = findViewById(R.id.txtCustomerPhone);
        txtCustomerAddress = findViewById(R.id.txtCustomerAddress);
        txtPaymentMethod = findViewById(R.id.txtPaymentMethod);
        txtTemporary = findViewById(R.id.txtTemporary);
        txtShippingFee = findViewById(R.id.txtShippingFee);
        txtDiscount = findViewById(R.id.txtDiscount);
        txtTotalMoney = findViewById(R.id.txtTotalMoney);
        btnBuyAgain = findViewById(R.id.btnBuyAgain);
    }

    private String formatPrice(double price) {
        return String.format("%,.0fđ", price);
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