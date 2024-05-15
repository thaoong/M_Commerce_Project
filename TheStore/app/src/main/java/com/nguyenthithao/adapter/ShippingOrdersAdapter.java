package com.nguyenthithao.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyenthithao.model.Order;
import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.thestore.OrderDetail2Activity;
import com.nguyenthithao.thestore.Prepayment2Activity;
import com.nguyenthithao.thestore.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ShippingOrdersAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orders;
    private List<String> orderKeys;
    private LayoutInflater inflater;

    public ShippingOrdersAdapter(Context context, List<Order> orders, List<String> orderKeys) {
        this.context = context;
        this.orders = orders;
        this.orderKeys = orderKeys;
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_orderlist, parent, false);
            holder = new ViewHolder();
            holder.rvIteminorder = convertView.findViewById(R.id.rvIteminorder);
            holder.txtProductCount = convertView.findViewById(R.id.txtProductCount);
            holder.txtTotalMoney = convertView.findViewById(R.id.txtTotalMoney);
            holder.txtOrderKey = convertView.findViewById(R.id.txtOrderKey); // Thêm dòng này
            holder.btnBuyAgain = convertView.findViewById(R.id.btnBuyAgain);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = orders.get(position);
        String orderKey = orderKeys.get(position); // Lấy order key

        List<OrderBook> orderBooks = order.getOrderBooks();
        ProductAdapter productAdapter = new ProductAdapter(context, orderBooks);
        holder.rvIteminorder.setAdapter(productAdapter);
        holder.rvIteminorder.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.rvIteminorder.setNestedScrollingEnabled(false);

        holder.txtProductCount.setText(String.valueOf(orderBooks.size()));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalMoney = currencyFormat.format(order.getTotal());
        holder.txtTotalMoney.setText(formattedTotalMoney);

        holder.txtOrderKey.setText(orderKey); // Thiết lập giá trị cho txtOrderKey

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetail2Activity.class);
                intent.putExtra("order", order);
                intent.putExtra("orderKey", orderKey);
                context.startActivity(intent);
            }
        });

        holder.btnBuyAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Prepayment2Activity.class);
                intent.putExtra("order", order);
                intent.putExtra("orderKey", orderKey);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        RecyclerView rvIteminorder;
        TextView txtProductCount;
        TextView txtTotalMoney;
        TextView txtOrderKey; // Thêm biến này
        Button btnBuyAgain;
    }
}