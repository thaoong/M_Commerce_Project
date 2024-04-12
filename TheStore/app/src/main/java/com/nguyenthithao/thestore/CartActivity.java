package com.nguyenthithao.thestore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.nguyenthithao.adapter.BookAdapterTest;
import com.nguyenthithao.model.Book;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView lvFavoBook;
    ArrayList<Book>dsBook;
    BookAdapterTest adapterBook;
    Button btnBuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        displayActionBar();
        addViews();
    }

//    private void addViews() {
//        btnBuy=findViewById(R.id.btnBuy);
//    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Giỏ hàng</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buyProduct(View view) {

        Intent intent = new Intent(CartActivity.this, MainActivity.class);
        intent.putExtra("selectedFragment", "navigation_category");
        startActivity(intent);
    }

    private void addViews() {
        lvFavoBook=findViewById(R.id.lvFavoBook);
        dsBook=new ArrayList<>();
        adapterBook=new BookAdapterTest(CartActivity.this, R.layout.item_cart, dsBook);
        lvFavoBook.setAdapter(adapterBook);

        //giaLapBook();
    }

//    private void giaLapBook() {
//        dsBook.add(new Book("Tại sao lại phải code android","100.000đ", "120.000đ", R.drawable.bot_ic));
//        dsBook.add(new Book("Tại sao lại phải code android","100.000đ", "120.000đ", R.drawable.bot_ic));
//
//        adapterBook.notifyDataSetChanged();
//    }

    public void buyProductActivity(View view) {
        Intent intent = new Intent(CartActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}