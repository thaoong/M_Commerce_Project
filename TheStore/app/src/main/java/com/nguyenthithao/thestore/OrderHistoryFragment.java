package com.nguyenthithao.thestore;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class OrderHistoryFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);

        FragmentPagerAdapter adapter = new OrderHistoryPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(Color.BLACK, Color.parseColor("#964B00"));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#964B00"));

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab!= null) {
                TextView tabTextView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
                tabTextView.setText(adapter.getPageTitle(i));
                tab.setCustomView(tabTextView);

                if (i == viewPager.getCurrentItem()) {
                    tabTextView.setTextColor(Color.parseColor("#964B00"));
                    tabTextView.setTypeface(null, Typeface.BOLD);
                } else {
                    tabTextView.setTextColor(Color.BLACK);
                    tabTextView.setTypeface(null, Typeface.NORMAL);
                }
            }
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab!= null) {
                        TextView tabTextView = (TextView) tab.getCustomView();
                        if (i == position) {
                            tabTextView.setTextColor(Color.parseColor("#964B00")); // brown color
                            tabTextView.setTypeface(null, Typeface.BOLD);
                        } else {
                            tabTextView.setTextColor(Color.BLACK);
                            tabTextView.setTypeface(null, Typeface.NORMAL);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        return view;
    }

    private class OrderHistoryPagerAdapter extends FragmentPagerAdapter {
        private final String[] tabTitles = {"Chờ xác nhận", "Đang giao", "Hoàn tất", "Đã hủy"};

        public OrderHistoryPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PendingOrdersFragment();
                case 1:
                    return new ShippingOrdersFragment();
                case 2:
                    return new CompletedOrdersFragment();
                case 3:
                    return new CancelledFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}