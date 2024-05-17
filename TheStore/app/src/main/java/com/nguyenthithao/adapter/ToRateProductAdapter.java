package com.nguyenthithao.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nguyenthithao.model.ToRateBook;
import com.nguyenthithao.thestore.R;
import com.nguyenthithao.thestore.RatingActivity;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ToRateProductAdapter extends ArrayAdapter<ToRateBook> {
    Activity context;
    int resource;
    List<ToRateBook> objects;
    public ToRateProductAdapter(@NonNull Activity context, int resource, List<ToRateBook> objects) {
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
        TextView txtOldPrice = row.findViewById(R.id.txtOldPrice);
        TextView txtBookQuantity = row.findViewById(R.id.txtBookQuantity);
        ImageView imageView = row.findViewById(R.id.imgProduct);
        Button btnToRate = row.findViewById(R.id.btnToRate);

        ToRateBook toRateBook = this.objects.get(position);
        txtName.setText(toRateBook.getName());
        txtUnitPrice.setText(formatCurrency(toRateBook.getPrice()) + "đ");

        if (toRateBook.getOldPrice() != 0) {
            txtOldPrice.setText(formatCurrency(toRateBook.getOldPrice()) + "đ");
            txtOldPrice.setPaintFlags(txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            txtOldPrice.setText("");
        }
        txtBookQuantity.setText(String.valueOf(toRateBook.getQuantity()));

        // Load image using Picasso
        Picasso.get().load(toRateBook.getImageLink()).into(imageView);

        btnToRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RatingActivity.class);
                intent.putExtra("productName", toRateBook.getName());
                intent.putExtra("productImage", toRateBook.getImageLink());
                intent.putExtra("bookID", toRateBook.getId());
                intent.putExtra("productPrice", toRateBook.getPrice());
                intent.putExtra("productQuantity", toRateBook.getQuantity());
                context.startActivity(intent);
            }
        });
        return row;
    }

    private String formatCurrency(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(value);
    }
}
