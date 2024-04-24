package com.nguyenthithao.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.nguyenthithao.model.OrderHistory;
import com.nguyenthithao.model.Voucher;
import com.nguyenthithao.thestore.R;

import java.text.DecimalFormat;
import java.util.List;

public class VoucherAdapter extends ArrayAdapter<Voucher> {
    Activity context;
    int resource;
    public VoucherAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        TextView txtAmount= row.findViewById(R.id.txtPromotion);
        TextView txtCondition = row.findViewById(R.id.txtCondition);
        TextView txtVoucherCode = row.findViewById(R.id.txtVoucherID);
        TextView txtExpiration = row.findViewById(R.id.txtExpiration);
        Button btnCopy = row.findViewById(R.id.btnCopy);

        Voucher voucher = getItem(position);
        txtAmount.setText(formatCurrency(voucher.getAmount())+"đ");
        txtCondition.setText(formatCurrency(voucher.getCondition())+"đ");
        txtVoucherCode.setText(voucher.getCode());
        txtExpiration.setText(voucher.getExpiration());

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String voucherId = txtVoucherCode.getText().toString();
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Voucher ID", voucherId);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "Voucher code copied", Toast.LENGTH_SHORT).show();
            }
        });

        return row;
    }

    private String formatCurrency(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(value);
    }
}
