package com.nguyenthithao.thestore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.adapter.CategoryAdapter;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.model.Category;
import com.nguyenthithao.thestore.databinding.FragmentHomeBinding;

import com.nguyenthithao.adapter.SliderAdapter;
import com.nguyenthithao.model.SliderItems;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private Handler slideHandler = new Handler();
    private static final long SLIDER_DELAY = 3000;

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
        loadBestSelling();
        return view;
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

//        private void banner() {
//        List<SliderItems> sliderItems = new ArrayList<>();
//        sliderItems.add(new SliderItems(R.drawable.slider2));
//        sliderItems.add(new SliderItems(R.drawable.slider3));
//        sliderItems.add(new SliderItems(R.drawable.slider1));
//        binding.viewpagerSlider.setAdapter(new SliderAdapter(sliderItems,binding.viewpagerSlider));
//        binding.viewpagerSlider.setClipToPadding(false);
//        binding.viewpagerSlider.setOffscreenPageLimit(3);
//        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);
//
//
//        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
//        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                float r=1-Math.abs(position);
//                page.setScaleY(0.85f + (float)(r * 0.15f));
//
//            }
//        });
//        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
//        binding.viewpagerSlider.setCurrentItem(1);
//        binding.viewpagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                slideHandler.removeCallbacks(sliderRunnable);
//
//            }
//
//        });
//        slideHandler.postDelayed(sliderRunnable, SLIDER_DELAY);
//    }
//    private Runnable sliderRunnable = new Runnable() {
//        @Override
//        public void run() {
//            int currentItem = binding.viewpagerSlider.getCurrentItem();
//            int itemCount = binding.viewpagerSlider.getAdapter().getItemCount();
//
//            if (currentItem < itemCount - 1) {
//                binding.viewpagerSlider.setCurrentItem(currentItem + 1, true);
//            } else {
//                binding.viewpagerSlider.setCurrentItem(0, true);
//            }
//
//            slideHandler.postDelayed(this, SLIDER_DELAY);
//        }
//    };
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        slideHandler.removeCallbacks(sliderRunnable);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        slideHandler.postDelayed(sliderRunnable,2000);
//    }
}