package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.adapter.BookAdapter2;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.thestore.databinding.ActivityForYouBinding;
import com.nguyenthithao.thestore.databinding.ActivityNewArrivalsBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ForYouActivity extends AppCompatActivity {
    ActivityForYouBinding binding;
    private BookAdapter2 bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_for_you);
        binding = ActivityForYouBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bookAdapter = new BookAdapter2(ForYouActivity.this, R.layout.item_book);
        binding.lvForYou.setAdapter(bookAdapter);
        displayActionBar();
        loadForYou();
        addEvents();
    }

    private void addEvents() {
        binding.lvForYou.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = bookAdapter.getItem(position);
                Intent intent = new Intent(ForYouActivity.this, ProductDetailActivity.class);
                intent.putExtra("SELECTED_BOOK", book);
                startActivity(intent);
            }
        });
    }

    private void loadForYou() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        binding.progressBarForYou.setVisibility(View.VISIBLE);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<DataSnapshot> snapshotList = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        snapshotList.add(childSnapshot);
                    }

                    int totalItems = snapshotList.size();
                    int itemsToRetrieve = Math.min(totalItems, 10);

                    Random random = new Random();
                    Set<Integer> randomIndexes = new HashSet<>();
                    while (randomIndexes.size() < itemsToRetrieve) {
                        int randomIndex = random.nextInt(totalItems);
                        randomIndexes.add(randomIndex);
                    }

                    for (Integer randomIndex : randomIndexes) {
                        DataSnapshot randomSnapshot = snapshotList.get(randomIndex);
                        bookAdapter.add(randomSnapshot.getValue(Book.class));
                    }
                }
                binding.progressBarForYou.setVisibility(View.GONE);
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
        String title = getResources().getString(R.string.strFORYOU);
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
}