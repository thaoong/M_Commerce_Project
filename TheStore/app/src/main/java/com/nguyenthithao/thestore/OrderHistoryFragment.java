package com.nguyenthithao.thestore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
            return Html.fromHtml("<font color='#5C3507'>" + tabTitles[position].toLowerCase() + "</font>");
        }
    }
}