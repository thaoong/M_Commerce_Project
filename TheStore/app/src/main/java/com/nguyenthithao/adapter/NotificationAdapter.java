package com.nguyenthithao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nguyenthithao.model.Notification;
import com.nguyenthithao.thestore.R;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    ArrayList<Notification> items;
    Context context;
    LayoutInflater inflater;

    public NotificationAdapter(Context context, ArrayList<Notification> items) {
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_notification, parent, false);
        }
        TextView txtNotiTitle = view.findViewById(R.id.txtNotiTitle);
        txtNotiTitle.setText(items.get(position).getTitle());
        TextView txtNotiContent = view.findViewById(R.id.txtNotiContent);
        txtNotiContent.setText(items.get(position).getContent());
        TextView txtNotiTime = view.findViewById(R.id.txtNotiTime);
        txtNotiTime.setText(items.get(position).getTime());
        return view;
    }
}
