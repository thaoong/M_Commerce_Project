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
import com.nguyenthithao.adapter.ShippingOrdersAdapter;
import com.nguyenthithao.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class CancelledFragment extends Fragment {
    private ListView lvShippingOrders;
    private ShippingOrdersAdapter adapter;
    private List<Order> orders;
    private List<String> orderKeys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_orders, container, false);
        lvShippingOrders = view.findViewById(R.id.lvShippingOrders);

        // Retrieve orders from Firebase database
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        ordersRef.orderByChild("userID").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders = new ArrayList<>();
                orderKeys = new ArrayList<>();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order!= null && "Đã hủy".equals(order.getStatus())) {
                        orders.add(order);
                        orderKeys.add(orderSnapshot.getKey());
                    }
                }

                // Sort orders by order date from newest to oldest
                List<Order> sortedOrders = new ArrayList<>(orders);
                List<String> sortedOrderKeys = new ArrayList<>(orderKeys);
                for (int i = 0; i < sortedOrders.size(); i++) {
                    int maxIndex = i;
                    for (int j = i + 1; j < sortedOrders.size(); j++) {
                        if (sortedOrders.get(j).getOrderDate().compareTo(sortedOrders.get(maxIndex).getOrderDate()) > 0) {
                            maxIndex = j;
                        }
                    }
                    Order tempOrder = sortedOrders.get(maxIndex);
                    String tempOrderKey = sortedOrderKeys.get(maxIndex);
                    sortedOrders.set(maxIndex, sortedOrders.get(i));
                    sortedOrderKeys.set(maxIndex, sortedOrderKeys.get(i));
                    sortedOrders.set(i, tempOrder);
                    sortedOrderKeys.set(i, tempOrderKey);
                }

                adapter = new ShippingOrdersAdapter(getContext(), sortedOrders, sortedOrderKeys);
                lvShippingOrders.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "Error retrieving orders", databaseError.toException());
            }
        });

        // Set click listener for each order item in the list view
        lvShippingOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String orderKey = orderKeys.get(position);
                openOrderDetailActivity(orderKey);
            }
        });

        return view;
    }

    private void openOrderDetailActivity(String orderKey) {
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra("orderKey", orderKey);
        startActivity(intent);
    }
}