package com.nguyenthithao.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nguyenthithao.model.OrderHistory;
import com.nguyenthithao.model.Voucher;
import com.nguyenthithao.thestore.R;

import java.util.List;

public class VoucherAdapter extends ArrayAdapter<Voucher> {
    Activity context;
    int resource;
    List<Voucher> objects;
    public VoucherAdapter(@NonNull Activity context, int resource, List<Voucher> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        TextView txtPromotion= row.findViewById(R.id.txtPromotion);
        TextView txtCondition = row.findViewById(R.id.txtCondition);
        TextView txtVoucherID = row.findViewById(R.id.txtVoucherID);
        TextView txtExpiration = row.findViewById(R.id.txtExpiration);

        Voucher voucher = this.objects.get(position);
        txtPromotion.setText(voucher.getPromotion());
        txtCondition.setText(voucher.getCondition());
        txtVoucherID.setText(voucher.getVoucherID());
        txtExpiration.setText(voucher.getExpiration());


        return row;
    }
}
