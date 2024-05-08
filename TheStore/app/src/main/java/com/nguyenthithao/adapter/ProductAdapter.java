package com.nguyenthithao.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.thestore.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<OrderBook> orderBooks;

    public ProductAdapter(Context context, List<OrderBook> orderBooks) {
        this.context = context;
        this.orderBooks = orderBooks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderproduct, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderBook orderBook = orderBooks.get(position);
        Glide.with(context)
                .load(orderBook.getImageLink())
                .into(holder.imgBook);
        holder.txtProductName.setText(orderBook.getName());

        // Format the unit price
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")); // Vietnamese locale
        String formattedPrice = currencyFormat.format(orderBook.getUnitPrice());
        holder.txtPrice.setText(formattedPrice);

        holder.txtQuantity.setText(String.valueOf(orderBook.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return orderBooks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBook;
        TextView txtProductName;
        TextView txtPrice;
        TextView txtQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            imgBook = (ImageView) itemView.findViewById(R.id.imgBook);
            txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtQuantity = (TextView) itemView.findViewById(R.id.txtQuantity);
        }
    }
}