package com.nguyenthithao.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nguyenthithao.models.Book;
import com.nguyenthithao.thestore.R;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    Activity context;
    int resource;
    List<Book> objects;

    public BookAdapter(@NonNull Activity context, int resource, List<Book> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        TextView txtName = row.findViewById(R.id.txtName);
        TextView txtPrice = row.findViewById(R.id.txtPrice);
        TextView txtFakePrice = row.findViewById(R.id.txtFakePrice);


        Book book = this.objects.get(position);
        txtName.setText(book.getName());
        txtPrice.setText(book.getPrice());
        txtFakePrice.setText(book.getFakePrice());
//        txtPrice.setText(String.valueOf(book.getPrice()));
//        txtFakePrice.setText(String.valueOf(book.getFakePrice()));

        return row;
    }

}


