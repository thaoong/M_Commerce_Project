package com.nguyenthithao.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nguyenthithao.model.OrderDetailTest;
import com.nguyenthithao.thestore.R;
import com.nguyenthithao.thestore.RatingActivity;
import com.squareup.picasso.Picasso;

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
        ImageView imageView = row.findViewById(R.id.imgProduct); // Change here
        Button btnToRate = row.findViewById(R.id.btnToRate);

        OrderDetailTest orderDetailTest = this.objects.get(position);
        txtName.setText(orderDetailTest.getName());
        txtUnitPrice.setText(String.valueOf(orderDetailTest.getPrice()));
        txtBookQuantity.setText(String.valueOf(orderDetailTest.getQuantity()));

        // Load image using Picasso
        Picasso.get().load(orderDetailTest.getImageLink()).into(imageView);

        btnToRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RatingActivity.class);

                intent.putExtra("productName", orderDetailTest.getName());
                intent.putExtra("productImage", orderDetailTest.getImageLink());

                intent.putExtra("bookID", orderDetailTest.getId());
                intent.putExtra("productPrice", orderDetailTest.getPrice());
                intent.putExtra("productQuantity", orderDetailTest.getQuantity());


                context.startActivity(intent);
            }
        });

        return row;
    }
}
