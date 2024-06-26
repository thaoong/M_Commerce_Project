package com.nguyenthithao.thestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.PendingOrdersAdapter;
import com.nguyenthithao.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShippingOrdersFragment extends Fragment {
    private ListView lvPendingOrders;
    private PendingOrdersAdapter adapter;
    private List<Order> orders;
    private List<String> orderKeys;
    private Map<String, Order> orderMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_orders, container, false);
        lvPendingOrders = view.findViewById(R.id.lvPendingOrders);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        ordersRef.orderByChild("userID").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders = new ArrayList<>();
                orderKeys = new ArrayList<>();
                orderMap = new HashMap<>();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null && "Đang vận chuyển".equals(order.getStatus())) {
                        orders.add(order);
                        orderKeys.add(orderSnapshot.getKey());
                        orderMap.put(orderSnapshot.getKey(), order);
                    }
                }

                // Sort orders by order date from newest to oldest
                Collections.sort(orders, new Comparator<Order>() {
                    @Override
                    public int compare(Order o1, Order o2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        try {
                            return sdf.parse(o2.getOrderDate()).compareTo(sdf.parse(o1.getOrderDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                // Sort orderKeys to sync with sorted orders
                Collections.sort(orderKeys, new Comparator<String>() {
                    @Override
                    public int compare(String key1, String key2) {
                        Order o1 = orderMap.get(key1);
                        Order o2 = orderMap.get(key2);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        try {
                            return sdf.parse(o2.getOrderDate()).compareTo(sdf.parse(o1.getOrderDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                adapter = new PendingOrdersAdapter(getContext(), orders, orderKeys);
                adapter.setOnOrderClickListener(new PendingOrdersAdapter.OnOrderClickListener() {
                    @Override
                    public void onOrderClick(Order order, String orderKey) {
                        openOrderDetailActivity(orderKey);
                    }
                });
                lvPendingOrders.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "Error retrieving orders");
            }
        });

        lvPendingOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String orderKey = orderKeys.get(position);
                openOrderDetailActivity(orderKey);
            }
        });

        return view;
    }

    private void openOrderDetailActivity(String orderKey) {
        Intent intent = new Intent(getContext(), OrderDetail3Activity.class);
        intent.putExtra("orderKey", orderKey);
        startActivity(intent);
    }
}