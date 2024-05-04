package com.nguyenthithao.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.thestore.R;
import com.nguyenthithao.thestore.ProductDetailActivity;

import java.util.ArrayList;

public class BookRecommendAdapter extends RecyclerView.Adapter<BookRecommendAdapter.ViewHolder> {
    ArrayList<Book> items;
    Context context;

    public BookRecommendAdapter(ArrayList<Book> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BookRecommendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookRecommendAdapter.ViewHolder holder, int position) {
        Book book = items.get(position);
        String categoryName = book.getName();
        String truncatedName = truncateText(categoryName, 3);
        holder.txtCategoryName.setText(truncatedName);
        Glide.with(context)
                .load(book.getImageLink().get(0))
                .into(holder.imgCategory);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("SELECTED_BOOK", book);
                context.startActivity(intent);
            }
        });
    }

    private String truncateText(String text, int maxWords) {
        String[] words = text.split(" ");
        if (words.length > maxWords) {
            String truncatedText = "";
            for (int i = 0; i < maxWords; i++) {
                truncatedText += words[i] + " ";
            }
            return truncatedText.trim();
        } else {
            return text;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName;
        ImageView imgCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            imgCategory = itemView.findViewById(R.id.imgCategory);
        }
    }
}