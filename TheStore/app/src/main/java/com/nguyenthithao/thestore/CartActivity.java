package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.CartAdapter;
import com.nguyenthithao.model.CartItem;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ListView lvCart;
    CartAdapter cartAdapter;
    LinearLayout llNoItem;
    LinearLayout llHaveItem;
    CheckBox chkBuyAll;
    TextView txtTotal;
    Button btnBuy;
    private ArrayList<CartItem> selectedCartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        displayActionBar();
        addViews();
        addEvents();
        getCartByUser();
    }

    private void addEvents() {
        chkBuyAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Lặp qua tất cả các mục trong CartAdapter
                for (int i = 0; i < cartAdapter.getCount(); i++) {
                    // Đặt trạng thái checkbox của từng mục thành isChecked
                    cartAdapter.setChecked(i, isChecked);
                }
                // Cập nhật giá trị tổng và số lượng đã chọn
                cartAdapter.updateTotalValue();
                cartAdapter.updateTextBuyButton();
            }
        });
    }

    private void getCartByUser() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = firebaseDatabase.getReference("carts").child(userId);
        cartAdapter.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0) {
                    llHaveItem.setVisibility(View.GONE);
                    llNoItem.setVisibility(View.VISIBLE);
                } else {
                    llHaveItem.setVisibility(View.VISIBLE);
                    llNoItem.setVisibility(View.GONE);

                    for (DataSnapshot dss : snapshot.getChildren()){
                        CartItem cartItem = dss.getValue(CartItem.class);
                        String key = dss.getKey();
                        cartItem.setID(key);
                        cartAdapter.add(cartItem);
                    }
                }
                String title = getResources().getString(R.string.strSelectAll);
                String products = getResources().getString(R.string.strProducts);
                chkBuyAll.setText(title +" (" + snapshot.getChildrenCount() + " " +products+")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strCart);
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

    private void addViews() {
        lvCart = findViewById(R.id.lvCart);
        cartAdapter = new CartAdapter(CartActivity.this, R.layout.item_cart);
        lvCart.setAdapter(cartAdapter);
        llNoItem = findViewById(R.id.llNoItem);
        llHaveItem = findViewById(R.id.llHaveItem);
        chkBuyAll = findViewById(R.id.chkBuyAll);
        txtTotal = findViewById(R.id.txtTotal);
        btnBuy = findViewById(R.id.btnBuy);
    }

    public void processShopNow(View view) {
        Intent intent = new Intent(CartActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void updateTotalValue(String totalValue) {
        txtTotal.setText(totalValue);
    }

    public void updateSelectedCount(int count) {
        String title = getResources().getString(R.string.strCheckOut);
        btnBuy.setText(title + " (" +count+ ")");
    }

    public boolean isBuyAllChecked() {
        return chkBuyAll.isChecked();
    }

    public void buyProductActivity(View view) {
        if (selectedCartItems != null && !selectedCartItems.isEmpty()) {
            Intent intent = new Intent(this, PrePaymentActivity.class);
            intent.putParcelableArrayListExtra("selectedItems", selectedCartItems);
            startActivity(intent);
        }
        else {
            String title = getResources().getString(R.string.strChooseItemInCart);
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
        }
    }

    public void updateSelectedItems(ArrayList<CartItem> selectedCartItems) {
        this.selectedCartItems = selectedCartItems;
    }
}