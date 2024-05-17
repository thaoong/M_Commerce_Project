package com.nguyenthithao.thestore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyenthithao.adapter.NotificationAdapter;
import com.nguyenthithao.model.Notification;
import com.nguyenthithao.thestore.databinding.FragmentNotificationBinding;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    private View view;
    FragmentNotificationBinding binding;
    NotificationAdapter notificationAdapter;
    ArrayList<Notification> notifications;


    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        notifications = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getContext(), notifications);
        binding.lvNotification.setAdapter(notificationAdapter);
        loadData();
        return view;
    }

    private void loadData() {
        notifications.add(new Notification("Giảm giá Noel", "Giảm lên đến 50% các sản phẩm truyện tranh", "🕒 10:00 12/12/2023"));
        notifications.add(new Notification("Cốc cốc cốc, bạn bỏ quên gì nè!", "Một sản phẩm truyện ngôn tình đã được thêm vào giỏ hàng của bạn nhưng chưa được mua, đến ngay giỏ hàng để rinh ngay thôi nào!", "🕒 10:00 12/04/2024"));
        notifications.add(new Notification("Mừng ngày Nhà giáo Việt Nam 20 - 11", "Mừng nhà giáo Việt Nam, The Store giảm giá 20% cho tất cả các sản phẩm sách văn học cho quý thầy!", "🕒 10:00 21/04/2024"));
        notifications.add(new Notification("Ra mắt kênh truyền thông TikTok của The Store", "Tháng 4 vừa qua, thương hiệu The Store đã thành lập kênh TikTok @thestore để truyền tải thông điệp đọc sách đến với tất cả các bạn trẻ. Hãy theo dõi kênh TikTok của The Store để cập nhật những thông tin mới nhất về thương hiệu và những sản phẩm mới nhất của The Store nhé!", "🕒 10:00 14/05/2024"));
        notificationAdapter.notifyDataSetChanged();
    }
}