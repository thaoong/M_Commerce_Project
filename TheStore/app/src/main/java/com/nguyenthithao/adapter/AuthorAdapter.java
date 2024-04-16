package com.nguyenthithao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.nguyenthithao.model.Author;
import com.nguyenthithao.model.Category;
import com.nguyenthithao.thestore.AuthorBookActivity;
import com.nguyenthithao.thestore.CategoryBookActivity;
import com.nguyenthithao.thestore.databinding.ItemAuthorBinding;

import java.util.ArrayList;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.ViewHolder> {
    private ArrayList<Author> items;
    private Context context;

    public AuthorAdapter(ArrayList<Author> items) {
        this.items = items;
    }
    @NonNull
    @Override
    public AuthorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemAuthorBinding binding = ItemAuthorBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.txtAuthorName.setText(items.get(position).getName());
        Glide.with(context).load(items.get(position).getImageLink()).into(holder.binding.imgAuthor);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AuthorBookActivity.class);
                intent.putExtra("SELECTED_AUTHOR", items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemAuthorBinding binding;
        public ViewHolder(ItemAuthorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
