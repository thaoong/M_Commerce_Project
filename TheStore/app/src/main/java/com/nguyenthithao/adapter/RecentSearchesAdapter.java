package com.nguyenthithao.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyenthithao.thestore.R;

import java.util.ArrayList;

public class RecentSearchesAdapter extends RecyclerView.Adapter<RecentSearchesAdapter.ViewHolder> {
    private ArrayList<String> recentSearches;
    private OnItemClickListener mListener;
    private String currentQuery;

    public interface OnItemClickListener {
        void onItemClick(String recentSearch);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setCurrentQuery(String currentQuery) {
        this.currentQuery = currentQuery;
    }

    public RecentSearchesAdapter(ArrayList<String> recentSearches) {
        if (recentSearches.size() > 10) {
            this.recentSearches = new ArrayList<>(recentSearches.subList(recentSearches.size() - 10, recentSearches.size()));
        } else {
            this.recentSearches = recentSearches;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_recentsearches, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = position;

        holder.recentSearchText.setText(recentSearches.get(index));

        if (recentSearches.get(index).equals(currentQuery)) {
            holder.itemView.setVisibility(View.GONE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(recentSearches.get(index));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentSearches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recentSearchText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recentSearchText = itemView.findViewById(R.id.recentsearches);
        }

    }
}