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
import com.nguyenthithao.model.Category;
import com.nguyenthithao.thestore.CategoryBookActivity;
import com.nguyenthithao.thestore.ProductDetailActivity;
import com.nguyenthithao.thestore.databinding.ItemCategoryBinding;
import com.nguyenthithao.thestore.databinding.ViewholderCategoryBinding;

import java.util.ArrayList;

public class CategoryAdapter2 extends RecyclerView.Adapter<CategoryAdapter2.Viewholder> {
    private ArrayList<Category> items;
    private Context context;

    public CategoryAdapter2(ArrayList<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter2.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.txtCategoryName.setText(items.get(position).getName());
        Glide.with(context).load(items.get(position).getImageLink()).into(holder.binding.imgCategory);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryBookActivity.class);
                intent.putExtra("SELECTED_CATEGORY", items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ItemCategoryBinding binding;
        public Viewholder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
