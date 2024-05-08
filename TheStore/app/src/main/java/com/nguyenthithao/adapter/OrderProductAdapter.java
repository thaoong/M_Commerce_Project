package com.nguyenthithao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.thestore.R;

import java.util.List;

public class OrderProductAdapter extends BaseAdapter {
    private Context context;
    private List<OrderBook> orderBooks;
    private LayoutInflater inflater;

    public OrderProductAdapter(Context context, List<OrderBook> orderBooks) {
        this.context = context;
        this.orderBooks = orderBooks;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orderBooks.size();
    }

    @Override
    public Object getItem(int position) {
        return orderBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_orderproduct, parent, false);
            holder = new ViewHolder();
            holder.imgBook = (ImageView) convertView.findViewById(R.id.imgBook);
            holder.txtProductName = (TextView) convertView.findViewById(R.id.txtProductName);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderBook orderBook = orderBooks.get(position);
        Glide.with(context)
                .load(orderBook.getImageLink()) // Replace with actual image URL
                .into(holder.imgBook);
        holder.txtProductName.setText(orderBook.getName());
        holder.txtPrice.setText(String.valueOf(orderBook.getUnitPrice()));
        holder.txtQuantity.setText(String.valueOf(orderBook.getQuantity()));

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgBook;
        TextView txtProductName;
        TextView txtPrice;
        TextView txtQuantity;
    }
}