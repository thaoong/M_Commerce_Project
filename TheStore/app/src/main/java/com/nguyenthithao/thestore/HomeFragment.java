package com.nguyenthithao.thestore;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.AuthorAdapter;
import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.adapter.CategoryAdapter;
import com.nguyenthithao.model.Author;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.model.Category;
import com.nguyenthithao.thestore.databinding.FragmentHomeBinding;

import com.nguyenthithao.adapter.SliderAdapter;
import com.nguyenthithao.model.SliderItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HomeFragment extends Fragment {
    private int duration = 1800;
    private View view;
    FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        loadBanner();
        loadCategory();
        loadFlashSale();
        processCountDownFlashSale();
        loadBestSelling();
        loadAuthor();
        loadNewArrivals();
        loadForYou();
        addEvents();
        return view;
    }

    private void addEvents() {
        binding.txtSeeMoreFlashSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FlashSaleActivity.class);
                startActivity(intent);
            }
        });

        binding.txtSeeMoreBestSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BestSellingActivity.class);
                startActivity(intent);            }
        });

        binding.txtSeeMoreNewArrivals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewArrivalsActivity.class);
                startActivity(intent);            }
        });

        binding.txtSeeMoreForYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ForYouActivity.class);
                startActivity(intent);            }
        });
    }

    private void processCountDownFlashSale() {
        CountDownTimer countDownTimer = new CountDownTimer(duration*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                long seconds = (millisUntilFinished / 1000) % 60;

                binding.txtCountDownHour.setText(String.format("%02d", hours));
                binding.txtCountDownMinute.setText(String.format("%02d", minutes));
                binding.txtCountDownSecond.setText(String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                binding.txtCountDownHour.setText("00");
                binding.txtCountDownMinute.setText("00");
                binding.txtCountDownSecond.setText("00");
            }
        };
        countDownTimer.start();
    }

    private void loadForYou() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        binding.progressBarForYou.setVisibility(View.VISIBLE);
        ArrayList<Book> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<DataSnapshot> snapshotList = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        snapshotList.add(childSnapshot);
                    }

                    int totalItems = snapshotList.size();
                    int itemsToRetrieve = Math.min(totalItems, 7); // Lấy tối đa 7 giá trị

                    Random random = new Random();
                    Set<Integer> randomIndexes = new HashSet<>();
                    while (randomIndexes.size() < itemsToRetrieve) {
                        int randomIndex = random.nextInt(totalItems);
                        randomIndexes.add(randomIndex);
                    }

                    for (Integer randomIndex : randomIndexes) {
                        DataSnapshot randomSnapshot = snapshotList.get(randomIndex);
                        items.add(randomSnapshot.getValue(Book.class));
                    }

                    if (!items.isEmpty()) {
                        binding.rvForYou.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.rvForYou.setAdapter(new BookAdapter(items));
                    }
                }
                binding.progressBarForYou.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadNewArrivals() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        binding.progressBarNewArrivals.setVisibility(View.VISIBLE);
        ArrayList<Book> items = new ArrayList<>();
        myRef.limitToLast(7).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Book.class));
                    }
                    if (!items.isEmpty()) {
                        binding.rvNewArrivals.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.rvNewArrivals.setAdapter(new BookAdapter(items));
                    }
                }
                binding.progressBarNewArrivals.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAuthor() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("authors");
        binding.progressBarAuthor.setVisibility(View.VISIBLE);
        ArrayList<Author> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Author.class));
                    }
                    if (!items.isEmpty()) {
                        binding.rvAuthor.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.rvAuthor.setAdapter(new AuthorAdapter(items));
                    }
                    binding.progressBarAuthor.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadFlashSale() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        binding.progressBarFlashSale.setVisibility(View.VISIBLE);
        ArrayList<Book> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Book book = issue.getValue(Book.class);
                        if (book != null && book.getOldPrice() != 0) {
                            items.add(book);
                        }
                    }
                    if (!items.isEmpty()) {
                        binding.rvFlashSale.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.rvFlashSale.setAdapter(new BookAdapter(items));
                    }
                    binding.progressBarFlashSale.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadBestSelling() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        binding.progressBarBestSelling.setVisibility(View.VISIBLE);
        ArrayList<Book> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Book book = issue.getValue(Book.class);
                        if (book != null && book.getBestSelling() == 1) {
                            items.add(book);
                        }
                    }
                    if (!items.isEmpty()) {
                        binding.rvBestSelling.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.rvBestSelling.setAdapter(new BookAdapter(items));
                    }
                    binding.progressBarBestSelling.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadCategory() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("categories");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Category.class));
                    }
                    if (!items.isEmpty()) {
                        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.rvCategory.setAdapter(new CategoryAdapter(items));
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadBanner() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("banners");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot issue:snapshot.getChildren())
                    {
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewpagerSlider.setAdapter(new SliderAdapter(items, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
    }
}