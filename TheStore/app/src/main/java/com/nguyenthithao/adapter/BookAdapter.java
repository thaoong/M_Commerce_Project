package com.nguyenthithao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.thestore.ProductDetailActivity;
import com.nguyenthithao.thestore.databinding.ItemCatebookBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.Viewholder> {
    ArrayList<Book> items;
    Context context;


    public BookAdapter(ArrayList<Book> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BookAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemCatebookBinding binding = ItemCatebookBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.txtName.setText(items.get(position).getName());

        float unitPrice = items.get(position).getUnitPrice();
        String formattedUnitPrice = formatCurrency(unitPrice);
        holder.binding.txtUnitPrice.setText(formattedUnitPrice + "đ");

        float oldPrice = items.get(position).getOldPrice();
        if (oldPrice != 0) {
            String formattedOldPrice = formatCurrency(oldPrice);
            holder.binding.txtOldPrice.setText(formattedOldPrice + "đ");
            holder.binding.txtOldPrice.setPaintFlags(holder.binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.binding.txtOldPrice.setText("");
        }

        holder.binding.txtRating.setText(items.get(position).getRating()+"");
        holder.binding.txtReviewNum.setText("("+items.get(position).getReviewNum()+")");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());

        Glide.with(context)
                .load(items.get(position).getImageLink().get(0))
                .apply(requestOptions)
                .into(holder.binding.imgBook);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("SELECTED_BOOK", items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ItemCatebookBinding binding;
        public Viewholder(ItemCatebookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String formatCurrency(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(value);
    }
}
