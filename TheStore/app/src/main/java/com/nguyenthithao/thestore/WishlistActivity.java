package com.nguyenthithao.thestore;

//import static android.os.Build.VERSION_CODES.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ListView;

import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.models.Book;
import com.nguyenthithao.thestore.R;

import com.nguyenthithao.thestore.databinding.ActivityAddressBinding;
import com.nguyenthithao.thestore.databinding.ActivityWishlistBinding;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    ListView lvFavoBook;
    ArrayList<Book>dsBook;
    BookAdapter adapterBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        //binding = ActivityWishlistBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        displayActionBar();
        addViews();
    }

    private void addViews() {
        lvFavoBook=findViewById(R.id.lvFavoBook);
        dsBook=new ArrayList<>();
        adapterBook=new BookAdapter(WishlistActivity.this, R.layout.item_wishlist, dsBook);
        lvFavoBook.setAdapter(adapterBook);

        giaLapBook();
    }

    private void giaLapBook() {
        dsBook.add(new Book("Tại sao lại phải code android","100.000đ", "120.000đ", R.drawable.bot_ic));
        dsBook.add(new Book("Tại sao lại phải code android","100.000đ", "120.000đ", R.drawable.bot_ic));

        adapterBook.notifyDataSetChanged();
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Sản phẩm yêu thích</font>"));
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