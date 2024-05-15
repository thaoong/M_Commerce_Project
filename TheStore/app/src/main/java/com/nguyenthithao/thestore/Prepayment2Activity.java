package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.OrderBookAdapter;
import com.nguyenthithao.model.Address;
import com.nguyenthithao.model.Order;
import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.model.Voucher;
import com.nguyenthithao.thestore.databinding.ActivityPrePayment2Binding;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Prepayment2Activity extends AppCompatActivity {
    private ActivityPrePayment2Binding binding;
    private Order order;
    private String orderKey;
    private String paymentMethod;
    private float discount;
    private float total;
    private int shippingFee;
    public float prePrice;

    private String selectedVoucherCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrePayment2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();

        Intent intent = getIntent();
        order = intent.getParcelableExtra("order");
        orderKey = intent.getStringExtra("orderKey");
        paymentMethod = intent.getStringExtra("paymentMethod");
        discount = intent.getFloatExtra("discount", 0);
        shippingFee = intent.getIntExtra("shippingFee", 0);

        if (order != null) {
            displayOrderInformation();
        } else {
            Toast.makeText(this, "Order is null", Toast.LENGTH_SHORT).show();
        }

        addEvent();
    }

    private void displayOrderInformation() {
        binding.txtCustomerName.setText(order.getName());
        binding.txtPhone.setText(order.getPhone());
        binding.txtStreet.setText(order.getStreet());
        binding.txtAddress.setText(order.getWard() + ", " + order.getDistrict() + ", " + order.getProvince());

        ArrayList<OrderBook> orderBooks = order.getOrderBooks();
        OrderBookAdapter orderBookAdapter = new OrderBookAdapter(this, R.layout.item_order_book, orderBooks);
        binding.lvBook.setAdapter(orderBookAdapter);

        float prePrice = 0;
        for (OrderBook orderBook : orderBooks) {
            double unitPrice = orderBook.getUnitPrice();
            int quantity = orderBook.getQuantity();
            double itemTotalPrice = unitPrice * quantity;
            prePrice += itemTotalPrice;
        }
        binding.txtPrePrice.setText(formatCurrency(prePrice) + "đ");

        binding.txtShippingFee.setText(formatCurrency(shippingFee) + "đ");
        binding.txtDiscount.setText("-" + formatCurrency(discount) + "đ");
        binding.txtTotal.setText(formatCurrency(order.getTotal() - discount) + "đ");
        binding.txtPaymentMethod.setText(paymentMethod);
    }

    private void addEvent() {
        binding.btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Prepayment2Activity.this, AddressActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        binding.btnChangePaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Prepayment2Activity.this, PaymentMethodActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.btnChooseVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Prepayment2Activity.this, VoucherActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVoucher();
            }
        });

        binding.btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processOrder();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    paymentMethod = data.getStringExtra("SELECTED_PAYMENT_METHOD");
                    binding.txtPaymentMethod.setText(paymentMethod);
                }
            } else if (requestCode == 2) {
                if (data != null) {
                    selectedVoucherCode = data.getStringExtra("SELECTED_VOUCHER_CODE");
                    binding.edtVoucher.setText(selectedVoucherCode);
                }
            } else if (requestCode == 3) {
                if (data != null) {
                    Address selectedAddress = (Address) data.getSerializableExtra("SELECTED_ADDRESS");
                    binding.txtCustomerName.setText(selectedAddress.getName());
                    binding.txtPhone.setText(selectedAddress.getPhone());
                    binding.txtStreet.setText(selectedAddress.getStreet());
                    binding.txtAddress.setText(selectedAddress.getWard() + ", " + selectedAddress.getDistrict() + ", " + selectedAddress.getProvince());
                }
            }
        }
    }
    private void processOrder() {
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }
        order.setPaymentMethod(paymentMethod);
        order.setDiscount(discount);
        order.setTotal(total);
        order.setStatus("Chờ xác nhận");

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String orderDate = dateFormat.format(currentDate);
        order.setOrderDate(orderDate);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        order.setUserID(userId);

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        String orderId = ordersRef.push().getKey();
        ordersRef.child(orderId).setValue(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Dialog dialog = new Dialog(Prepayment2Activity.this);
                        dialog.setContentView(R.layout.dialog_order_successfuly);
                        dialog.setCancelable(false);
                        dialog.show();

                        Button btnContinueShopping = dialog.findViewById(R.id.btnContinueShopping);
                        Button btnViewOrder = dialog.findViewById(R.id.btnViewOrder);
                        btnContinueShopping.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(Prepayment2Activity.this, CartActivity.class);
                                startActivity(intent);
                            }
                        });
                        btnViewOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(Prepayment2Activity.this, OrderHistoryActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Prepayment2Activity.this, "Đã xảy ra lỗi. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkVoucher() {
        String voucherCode = binding.edtVoucher.getText().toString();
        DatabaseReference vouchersRef = FirebaseDatabase.getInstance().getReference("vouchers");
        vouchersRef.child(voucherCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Voucher voucher = dataSnapshot.getValue(Voucher.class);
                    if (voucher != null) {
                        float voucherCondition = voucher.getCondition();
                        String voucherExpiration = voucher.getExpiration();

                        // Get the current date
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String currentDate = sdf.format(new Date());

                        // Check if prePrice is greater than the voucher condition and current date is before the voucher expiration
                        if (prePrice > voucherCondition && isCurrentDateBeforeVoucherExpiration(currentDate, voucherExpiration)) {
                            discount = voucher.getAmount();
                            binding.txtDiscount.setText("-" + formatCurrency(discount) + "đ");
                            total = prePrice + shippingFee - discount;
                            binding.txtTotal.setText(formatCurrency(total) + "đ");
                            Toast.makeText(Prepayment2Activity.this, "Applied voucher", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                // Voucher is not applicable or does not exist
                discount = 0;
                binding.txtDiscount.setText("-" + formatCurrency(discount) + "đ");
                total = prePrice + shippingFee - discount;
                binding.txtTotal.setText(formatCurrency(total) + "đ");
                Toast.makeText(Prepayment2Activity.this, "Voucher invalid", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private String formatCurrency(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(value);
    }
    private boolean isCurrentDateBeforeVoucherExpiration(String currentDate, String voucherExpiration) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date current = sdf.parse(currentDate);
            Date expiration = sdf.parse(voucherExpiration);
            return current.before(expiration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Thanh toán</font>"));
    }

}