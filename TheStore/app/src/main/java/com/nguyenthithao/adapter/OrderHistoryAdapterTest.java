package com.nguyenthithao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nguyenthithao.model.OrderHistory;
import com.nguyenthithao.thestore.R;

import java.util.List;

public class OrderHistoryAdapterTest extends ArrayAdapter<OrderHistory> {
    Activity context;
    int resource;
    List<OrderHistory> objects;
    public OrderHistoryAdapterTest(@NonNull Activity context, int resource, List<OrderHistory> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        TextView txtOrderID= row.findViewById(R.id.txtOrderID);
        TextView txtOrderStatus = row.findViewById(R.id.txtOrderStatus);
        TextView txtDate = row.findViewById(R.id.txtDate);
        TextView txtName = row.findViewById(R.id.txtName);
        TextView txtTotalPrice = row.findViewById(R.id.txtTotalPrice);

        OrderHistory orderHistory = this.objects.get(position);
        txtOrderID.setText(orderHistory.getOrderID());
        txtOrderStatus.setText(orderHistory.getOderStatus());
        txtDate.setText(orderHistory.getDate());
        txtName.setText(orderHistory.getName());
        txtTotalPrice.setText(orderHistory.getTotalPrice()+"đ");


        return row;
    }
}
