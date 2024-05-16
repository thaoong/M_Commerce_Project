package com.nguyenthithao.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nguyenthithao.model.PaymentMethod;
import com.nguyenthithao.thestore.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PaymentMethodAdapter extends ArrayAdapter<PaymentMethod> {
    Activity context;
    int resource;
    public PaymentMethodAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(resource, null);
        ImageView imgPaymentMethod = row.findViewById(R.id.imgPaymentMethod);
        TextView txtPaymentMethodName = row.findViewById(R.id.txtPaymentMethodName);
        TextView txtPaymentMethodDes = row.findViewById(R.id.txtPaymentMethodDes);

        PaymentMethod paymentMethod = getItem(position);
        txtPaymentMethodName.setText(paymentMethod.getName());
        txtPaymentMethodDes.setText(paymentMethod.getDescription());
        Picasso.get().load(paymentMethod.getImageLink()).into(imgPaymentMethod);

        return row;
    }
}
