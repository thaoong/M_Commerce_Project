package com.nguyenthithao.thestore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyenthithao.thestore.databinding.FragmentHomeBinding;

import com.google.android.material.slider.Slider;
import com.nguyenthithao.adapter.SliderAdapter;
import com.nguyenthithao.models.SliderItems;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ViewPager2 viewPager2;
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
        //addEvents();
        banner();
        return view;
    }

//    private void addEvents() {
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
////        }
//
//    }

//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        viewPager2 = view.findViewById(R.id.viewpagerSlider);
//
//        return view;
//    }
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        banner();
//    }

    private void banner() {
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.slider2));
        sliderItems.add(new SliderItems(R.drawable.slider3));
        sliderItems.add(new SliderItems(R.drawable.slider1));
        binding.viewpagerSlider.setAdapter(new SliderAdapter(sliderItems,binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f + (float)(r * 0.15f));

            }
        });
        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
        binding.viewpagerSlider.setCurrentItem(1);
        binding.viewpagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);

            }

        });
        slideHandler.postDelayed(sliderRunnable, SLIDER_DELAY);


    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = binding.viewpagerSlider.getCurrentItem();
            int itemCount = binding.viewpagerSlider.getAdapter().getItemCount();

            if (currentItem < itemCount - 1) {
                binding.viewpagerSlider.setCurrentItem(currentItem + 1, true);
            } else {
                binding.viewpagerSlider.setCurrentItem(0, true);
            }

            slideHandler.postDelayed(this, SLIDER_DELAY);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable,2000);
    }


}