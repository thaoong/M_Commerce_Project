package com.nguyenthithao.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nguyenthithao.model.OrderDetailTest;
import com.nguyenthithao.thestore.R;

import java.util.List;

public class OrderDetailAdapterTest extends ArrayAdapter<OrderDetailTest> {
    Activity context;
    int resource;
    List<OrderDetailTest> objects;
    public OrderDetailAdapterTest(@NonNull Activity context, int resource, List<OrderDetailTest> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        TextView txtName = row.findViewById(R.id.txtBookName);
        TextView txtUnitPrice = row.findViewById(R.id.txtUnitPrice);
        TextView txtBookQuantity = row.findViewById(R.id.txtBookQuantity);

        OrderDetailTest orderDetailTest = this.objects.get(position);
        txtName.setText(orderDetailTest.getName());
        txtUnitPrice.setText(orderDetailTest.getPrice());
        txtBookQuantity.setText(String.valueOf(orderDetailTest.getQuantity()));

        return row;
    }
}
