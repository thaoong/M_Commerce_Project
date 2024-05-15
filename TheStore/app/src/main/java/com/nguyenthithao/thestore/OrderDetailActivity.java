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

public class OrderDetailActivity extends AppCompatActivity {

    ListView lvOrderDetail;
    TextView txtOrderID, txtOrderStatus, txtDate, txtDateReceive, txtBookQuantity, txtTotalPrice, txtCustomerName, txtCustomerPhone, txtCustomerAddress, txtPaymentMethod, txtTemporary, txtShippingFee, txtDiscount, txtTotalMoney;
    Button btnDeleteOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        displayActionBar();
        addViews();

        String orderKey = getIntent().getStringExtra("orderKey");

        txtOrderID.setText(orderKey);

        btnDeleteOrder = findViewById(R.id.btnDeleteorder);
        btnDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrder(orderKey);
            }
        });

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child(orderKey);
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Order orderData = dataSnapshot.getValue(Order.class);

                if (orderData!= null) {
                    txtOrderStatus.setText(orderData.getStatus());
                    txtDate.setText(orderData.getOrderDate());
                    txtDateReceive.setText(orderData.getReceivedDate());
                    txtBookQuantity.setText(String.valueOf(orderData.getOrderBooks().size()));
                    txtTotalPrice.setText(formatPrice(orderData.getTotal()));
                    txtCustomerName.setText(orderData.getName());
                    txtCustomerPhone.setText(orderData.getPhone());
                    txtCustomerAddress.setText(orderData.getStreet() + ", " + orderData.getWard() + ", " + orderData.getDistrict() + ", " + orderData.getProvince());
                    txtPaymentMethod.setText(orderData.getPaymentMethod());
                    txtTemporary.setText(formatPrice(orderData.getPrePrice()));
                    txtShippingFee.setText(formatPrice(orderData.getShippingFee()));
                    txtDiscount.setText(formatPrice(orderData.getDiscount()));
                    txtTotalMoney.setText(formatPrice(orderData.getTotal()));

                    ArrayList<OrderBook> orderBooks = orderData.getOrderBooks();
                    OrderBookAdapter adapter = new OrderBookAdapter(OrderDetailActivity.this, R.layout.item_order_book, orderBooks);
                    lvOrderDetail.setAdapter(adapter);
                } else {
                    Toast.makeText(OrderDetailActivity.this, "No order data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderDetailActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
        btnDeleteOrder = findViewById(R.id.btnDeleteorder);
    }

    private String formatPrice(double price) {
        return String.format("%,.0fđ", price);
    }

    private void deleteOrder(String orderKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Cancel Order");
        builder.setMessage("Are you sure you want to cancel this order?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child(orderKey);
                orderRef.child("status").setValue("Đã hủy");

                // Set the result to indicate the order was deleted
                Intent resultIntent = new Intent();
                resultIntent.putExtra("deletedOrderKey", orderKey);
                setResult(AppCompatActivity.RESULT_OK, resultIntent);

                Toast.makeText(OrderDetailActivity.this, "Order cancelled successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
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