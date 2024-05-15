package com.nguyenthithao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.thestore.R;

import java.util.ArrayList;

public class ReviewedProductAdapter extends BaseAdapter {
    private ArrayList<OrderBook> reviewedProducts;
    private Context context;

    public ReviewedProductAdapter(Context context, ArrayList<OrderBook> reviewedProducts) {
        this.context = context;
        this.reviewedProducts = reviewedProducts;
    }

    @Override
    public int getCount() {
        return reviewedProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewedProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_old_rating, parent, false);

            holder = new ViewHolder();
            holder.productName = convertView.findViewById(R.id.txtProductName);
            holder.productImage = convertView.findViewById(R.id.imgProduct);
            // Đánh dấu các view khác nếu cần

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderBook book = reviewedProducts.get(position);

        if (book.isReview()) {
            holder.productName.setText(book.getName());
        } else {
            convertView.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView productName;
        ImageView productImage;
    }
}
