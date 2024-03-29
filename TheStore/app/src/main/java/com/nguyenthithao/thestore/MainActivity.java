package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nguyenthithao.adapter.ViewPaperAdapter;
import com.nguyenthithao.thestore.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
//    public static ViewPager viewPager;
//    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        createBottomNavigation();
    }

    private void createBottomNavigation() {
        ViewPaperAdapter viewPaperAdapter = new ViewPaperAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.mainViewpager.setAdapter((viewPaperAdapter));
        binding.mainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ActionBar actionBar = getSupportActionBar();
                switch (position) {
                    case 0:
                        binding.bottomNavigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Trang chủ</font>"));
                        break;
                    case 1:
                        binding.bottomNavigation.getMenu().findItem(R.id.navigation_category).setChecked(true);
                        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Danh mục</font>"));
                        break;
                    case 2:
                        binding.bottomNavigation.getMenu().findItem(R.id.navigation_notification).setChecked(true);
                        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Thông báo</font>"));
                        break;
                    case 3:
                        binding.bottomNavigation.getMenu().findItem(R.id.navigation_profile).setChecked(true);
                        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Tài khoản</font>"));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    binding.mainViewpager.setCurrentItem(0);
                }
                if (id == R.id.navigation_category) {
                    binding.mainViewpager.setCurrentItem(1);
                }
                if (id == R.id.navigation_notification) {
                    binding.mainViewpager.setCurrentItem(2);
                }
                if (id == R.id.navigation_profile) {
                    binding.mainViewpager.setCurrentItem(3);
                }
                return true;
            }
        });
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Trang chủ</font>"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuCart) {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.mnuSearch) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}