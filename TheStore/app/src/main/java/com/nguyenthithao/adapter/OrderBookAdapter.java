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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.thestore.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

public class OrderBookAdapter extends ArrayAdapter<OrderBook> {
    private final Context context;
    private final int resource;
    private final List<OrderBook> objects;

    public OrderBookAdapter(@NonNull Context context, int resource, @NonNull List<OrderBook> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imgBook = convertView.findViewById(R.id.imgBook);
            viewHolder.txtName = convertView.findViewById(R.id.txtBookName);
            viewHolder.txtUnitPrice = convertView.findViewById(R.id.txtUnitPrice);
            viewHolder.txtOldPrice = convertView.findViewById(R.id.txtOldPrice);
            viewHolder.txtBookQuantity = convertView.findViewById(R.id.txtBookQuantity);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OrderBook book = objects.get(position);
        viewHolder.txtName.setText(book.getName());
        viewHolder.txtUnitPrice.setText(formatCurrency(book.getUnitPrice()) + "đ");

        if (book.getOldPrice() != 0) {
            viewHolder.txtOldPrice.setText(formatCurrency(book.getOldPrice()) + "đ");
            viewHolder.txtOldPrice.setPaintFlags(viewHolder.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.txtOldPrice.setText("");
        }

        viewHolder.txtBookQuantity.setText(String.valueOf(book.getQuantity()));
        new ImageLoadTask(viewHolder.imgBook).execute(book.getImageLink());

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgBook;
        TextView txtName;
        TextView txtUnitPrice;
        TextView txtOldPrice;
        TextView txtBookQuantity;
    }

    private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

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