package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
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
import com.nguyenthithao.model.CartItem;
import com.nguyenthithao.model.Order;
import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.model.OrderHistory;
import com.nguyenthithao.model.Voucher;
import com.nguyenthithao.thestore.databinding.ActivityPrePaymentBinding;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PrePaymentActivity extends AppCompatActivity {
    ActivityPrePaymentBinding binding;
    Intent intent;
    private OrderBookAdapter orderBookAdapter;
    ArrayList<OrderBook> orderBooks;
    private String name;
    private String phone;
    private String street;
    private String ward;
    private String district;
    private String province;
    public float prePrice;
    private float shippingFee;
    private float discount;
    private float total;
    private String paymentMethod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pre_payment);
        binding = ActivityPrePaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        getAddress();
        getSelectedBookFromCart();
        calculatePrePrice();
        addEvent();
    }

    private void getAddress() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = firebaseDatabase.getReference("addresses").child(userId);
        myRef.orderByChild("default").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    name = snapshot.child("name").getValue(String.class);
                    binding.txtCustomerName.setText(name);

                    phone = snapshot.child("phone").getValue(String.class);
                    binding.txtPhone.setText(phone);

                    street = snapshot.child("street").getValue(String.class);
                    binding.txtStreet.setText(street);

                    ward = snapshot.child("ward").getValue(String.class);
                    district = snapshot.child("district").getValue(String.class);
                    province = snapshot.child("province").getValue(String.class);
                    binding.txtAddress.setText(ward + ", " + district + ", " + province);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
            orderBook.setId(item.getID());
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
        binding.btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrePaymentActivity.this, AddressActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        binding.btnChangePaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrePaymentActivity.this, PaymentMethodActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.btnChooseVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrePaymentActivity.this, VoucherActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        shippingFee = 30000;
        binding.txtShippingFee.setText(formatCurrency(shippingFee)+"đ");

        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVoucher();
            }
        });

        binding.txtDiscount.setText("-"+formatCurrency(discount)+"đ");
        total = prePrice + shippingFee - discount;
        binding.txtTotal.setText(formatCurrency(total) + "đ");

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
                    String selectedVoucherCode = data.getStringExtra("SELECTED_VOUCHER_CODE");
                    binding.edtVoucher.setText(selectedVoucherCode);
                }
            } else if (requestCode == 3) {
                if (data != null) {
                    Address selectedAddress = (Address) data.getSerializableExtra("SELECTED_ADDRESS");
                    name = selectedAddress.getName();
                    binding.txtCustomerName.setText(name);

                    phone = selectedAddress.getPhone();
                    binding.txtPhone.setText(phone);

                    street = selectedAddress.getStreet();
                    binding.txtStreet.setText(street);

                    ward = selectedAddress.getWard();
                    district = selectedAddress.getDistrict();
                    province = selectedAddress.getProvince();
                    binding.txtAddress.setText(ward + ", " + district + ", " + province);
                }
            }
        }
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
                            Toast.makeText(PrePaymentActivity.this, "Applied voucher", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                // Voucher is not applicable or does not exist
                discount = 0;
                binding.txtDiscount.setText("-" + formatCurrency(discount) + "đ");
                total = prePrice + shippingFee - discount;
                binding.txtTotal.setText(formatCurrency(total) + "đ");
                Toast.makeText(PrePaymentActivity.this, "Voucher invalid", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void processOrder() {
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }
        Order order = new Order();
        order.setOrderBooks(orderBooks);
        order.setName(name);
        order.setPhone(phone);
        order.setStreet(street);
        order.setWard(ward);
        order.setDistrict(district);
        order.setProvince(province);
        order.setPrePrice(prePrice);
        order.setShippingFee(shippingFee);
        order.setDiscount(discount);
        order.setTotal(total);
        order.setPaymentMethod(paymentMethod);
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
                        deleteSelectedItemsFromCart();
                        Dialog dialog = new Dialog(PrePaymentActivity.this);
                        dialog.setContentView(R.layout.dialog_order_successfuly);
                        dialog.setCancelable(false);
                        dialog.show();

                        Button btnContinueShopping = dialog.findViewById(R.id.btnContinueShopping);
                        Button btnViewOrder = dialog.findViewById(R.id.btnViewOrder);
                        btnContinueShopping.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(PrePaymentActivity.this, CartActivity.class);
                                startActivity(intent);
                            }
                        });
                        btnViewOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(PrePaymentActivity.this, OrderHistoryActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PrePaymentActivity.this, "Đã xảy ra lỗi. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteSelectedItemsFromCart() {
        intent = getIntent();
        ArrayList<CartItem> selectedItems = intent.getParcelableArrayListExtra("selectedItems");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);
        for (CartItem item : selectedItems) {
            String cartItemId = item.getID();
            cartRef.child(cartItemId).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Xóa thành công
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xóa thất bại
                        }
                    });
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
}