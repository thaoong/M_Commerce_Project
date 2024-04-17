package com.nguyenthithao.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
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

import com.nguyenthithao.model.CartItem;
import com.nguyenthithao.thestore.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<CartItem> {
    Activity context;
    int resource;

    public CartAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View cart_item = inflater.inflate(resource, null);
        CheckBox chkBuy = cart_item.findViewById(R.id.chkBuy);
        ImageView imgBook = cart_item.findViewById(R.id.imgBook);
        TextView bookName = cart_item.findViewById(R.id.txtName);
        TextView unitPrice = cart_item.findViewById(R.id.txtUnitPrice);
        TextView oldPrice = cart_item.findViewById(R.id.txtOldPrice);
        ImageView btnMinus = cart_item.findViewById(R.id.btnMinus);
        ImageView btnPlus = cart_item.findViewById(R.id.btnPlus);
        ImageView btnDelete = cart_item.findViewById(R.id.btnDelete);
        TextView quantity = cart_item.findViewById(R.id.txtQuantity);

        CartItem cartItem = getItem(position);
        bookName.setText(cartItem.getName());
        unitPrice.setText(formatCurrency(cartItem.getUnitPrice())+"đ");

        if (cartItem.getOldPrice() != 0) {
            oldPrice.setText(formatCurrency(cartItem.getOldPrice()) + "đ");
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            oldPrice.setText("");
        }
        quantity.setText(cartItem.getQuantity()+"");
        new ImageLoadTask(imgBook).execute(cartItem.getImageLink());
        return cart_item;
    }

    private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public ImageLoadTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private String formatCurrency(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(value);
    }
}
