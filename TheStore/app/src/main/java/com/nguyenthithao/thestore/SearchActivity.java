package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchActivity extends AppCompatActivity {
    
    ImageView icSearch;
    EditText searchBar;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        displayActionBar();
        setHasOptionsMenu(true);
        addViews();
        icSearch = findViewById(R.id.icSearch);
        searchBar = findViewById(R.id.searchBar);
        btnSearch = findViewById(R.id.btnSearch);

        icSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchBar();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBar.getText().toString();
                performSearch(query);
            }
        });
    }

    public void showSearchBar() {
        icSearch.setVisibility(View.GONE);
        searchBar.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);
    }

    public void hideSearchBar() {
        icSearch.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.GONE);
        btnSearch.setVisibility(View.GONE);
    }

    private void performSearch(String query) {
        // Xử lý tìm kiếm
    }


    private void addViews() {

    }

    private void setHasOptionsMenu(boolean b) {
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Tìm kiếm</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}