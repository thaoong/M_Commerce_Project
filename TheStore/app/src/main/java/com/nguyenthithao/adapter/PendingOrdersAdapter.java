package com.nguyenthithao.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyenthithao.model.Order;
import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.thestore.OrderDetailActivity;
import com.nguyenthithao.thestore.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PendingOrdersAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orders;
    private List<String> orderKeys;
    private LayoutInflater inflater;
    private OnOrderClickListener onOrderClickListener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order, String orderKey);
    }

    public PendingOrdersAdapter(Context context, List<Order> orders, List<String> orderKeys) {
        this.context = context;
        this.orders = orders;
        this.orderKeys = orderKeys;
        inflater = LayoutInflater.from(context);
    }

    public void setOnOrderClickListener(OnOrderClickListener onOrderClickListener) {
        this.onOrderClickListener = onOrderClickListener;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pendingorder, parent, false);
            holder = new ViewHolder();
            holder.rvIteminorder = convertView.findViewById(R.id.rvIteminorder);
            holder.txtProductCount = convertView.findViewById(R.id.txtProductCount);
            holder.txtTotalMoney = convertView.findViewById(R.id.txtTotalMoney);
            holder.btnDetails = convertView.findViewById(R.id.btnDetails);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = orders.get(position);

        List<OrderBook> orderBooks = order.getOrderBooks();
        ProductAdapter productAdapter = new ProductAdapter(context, orderBooks);
        holder.rvIteminorder.setAdapter(productAdapter);
        holder.rvIteminorder.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.rvIteminorder.setNestedScrollingEnabled(false);

        holder.txtProductCount.setText(String.valueOf(orderBooks.size()));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalMoney = currencyFormat.format(order.getTotal());
        holder.txtTotalMoney.setText(formattedTotalMoney);

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOrderClickListener!= null) {
                    Order order = orders.get(position);
                    String orderKey = orderKeys.get(position);
                    onOrderClickListener.onOrderClick(order, orderKey);
                }
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        RecyclerView rvIteminorder;
        TextView txtProductCount;
        TextView txtTotalMoney;
        Button btnDetails;
    }
}