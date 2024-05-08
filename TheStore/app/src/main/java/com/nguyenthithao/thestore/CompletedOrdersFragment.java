package com.nguyenthithao.thestore;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.PendingOrdersAdapter;
import com.nguyenthithao.adapter.ShippingOrdersAdapter;
import com.nguyenthithao.model.Order;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrdersFragment extends Fragment {
    private ListView lvShippingOrders;
    private PendingOrdersAdapter adapter;
    private List<Order> orders;
    private List<String> orderKeys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_orders, container, false);
        lvShippingOrders = view.findViewById(R.id.lvShippingOrders);
        lvShippingOrders.setOnTouchListener((v, event) -> true);

        // Retrieve orders from Firebase database
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        ordersRef.orderByChild("userID").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders = new ArrayList<>();
                orderKeys = new ArrayList<>(); // Create a list to store order keys
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order.getStatus().equals("Hoàn tất")) { // Filter orders with status "Hoàn tất"
                        orders.add(order);
                        orderKeys.add(orderSnapshot.getKey()); // Add the order key to the list
                    }
                }
                adapter = new PendingOrdersAdapter(getContext(), orders, orderKeys); // Pass the order keys to the adapter
                adapter.setOnOrderClickListener(new PendingOrdersAdapter.OnOrderClickListener() {
                    @Override
                    public void onOrderClick(Order order, String orderKey) {
                        // Handle the buy again button click
                        openBuyAgainActivity(orderKey);
                    }
                });
                lvShippingOrders.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "Error retrieving orders");
            }
        });

        return view;
    }

    private void openBuyAgainActivity(String orderKey) {
        Intent intent = new Intent(getContext(), OrderDetail2Activity.class);
        intent.putExtra("orderKey", orderKey);
        startActivity(intent);
    }
}