package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nguyenthithao.adapter.ViewPaperAdapter;
import com.nguyenthithao.thestore.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int count_exit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        createBottomNavigation();
        getDeviceToken();

        if (getIntent().hasExtra("selectedFragment")) {
            String selectedFragment = getIntent().getStringExtra("selectedFragment");

            if (selectedFragment.equals("navigation_category")) {
                binding.mainViewpager.setCurrentItem(1);
            }
        }
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
                        String title = getResources().getString(R.string.strHome);
                        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>"+title+"</font>"));
                        break;
                    case 1:
                        binding.bottomNavigation.getMenu().findItem(R.id.navigation_category).setChecked(true);
                        String title1 = getResources().getString(R.string.strCategory);
                        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>"+title1+"</font>"));
                        break;
                    case 2:
                        binding.bottomNavigation.getMenu().findItem(R.id.navigation_notification).setChecked(true);
                        String title3 = getResources().getString(R.string.strNotification);
                        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>"+title3+"</font>"));
                        break;
                    case 3:
                        binding.bottomNavigation.getMenu().findItem(R.id.navigation_profile).setChecked(true);
                        String title4 = getResources().getString(R.string.strProfile);
                        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>"+title4+"</font>"));
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
        String title = getResources().getString(R.string.strHome);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>"+title+"</font>"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.mnuCart);
        View actionView = menuItem.getActionView();
        TextView cartBadgeTextView = actionView.findViewById(R.id.cart_badge_text_view);
        cartBadgeTextView.setText("2");

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuCart) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "Please log in to view cart", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        }
        else if (id == R.id.mnuSearch) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.mnuChatBot) {
            Intent intent = new Intent(MainActivity.this, ChatbotActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        count_exit++;
        if (count_exit==1)
        {
            Toast.makeText(this, "Press back button again to exit app", Toast.LENGTH_SHORT).show();
        }
        else if (count_exit>=2) {
            finish();
            count_exit=0;
        }
    }

    private void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.e("FireBaseLogs", " Fetching Token Failed" + task.getException());
                    return;
                }
                String token = task.getResult();
                Log.v("FireBaseLogs", "Device Token: " + token);
                addTokenToDatabase(token);
            }
        });
    }

    private void addTokenToDatabase(String token) {
        DatabaseReference clientTokenRef = FirebaseDatabase.getInstance().getReference("clientTokens");
        clientTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean exists = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String clientToken = snapshot.getValue(String.class);
                    if (clientToken.equals(token)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    clientTokenRef.push().setValue(token);
                }
                else {
                    Log.v("FireBaseLogs", "Token existed: " + token);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FireBaseLogs", "Error when add token: " + databaseError.getMessage());
            }
        });
    }
}