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
import com.nguyenthithao.adapter.ShippingOrdersAdapter;
import com.nguyenthithao.model.Order;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrdersFragment extends Fragment {
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
        if (userId!= null) { // Check if userId is not null
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
            ordersRef.orderByChild("userID").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    orders = new ArrayList<>();
                    orderKeys = new ArrayList<>();
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order!= null && order.getStatus().equals("Hoàn tất")) { // Check if order is not null
                            orders.add(order);
                            orderKeys.add(orderSnapshot.getKey());
                        }
                    }
                    adapter = new ShippingOrdersAdapter(getContext(), orders, orderKeys);
                    lvShippingOrders.setAdapter(adapter); // Set the adapter to the ListView
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Error", "Error retrieving orders: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("Error", "User is not logged in");
        }

        return view;
    }


    private void openBuyAgainActivity(Order order, String orderKey) {
        Intent intent = new Intent(getContext(), PrePaymentActivity.class);
        intent.putExtra("order", order);
        intent.putExtra("orderKey", orderKey);
        startActivity(intent);
    }

}