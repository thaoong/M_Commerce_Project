package com.nguyenthithao.adapter;

import android.app.Activity;
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

import com.nguyenthithao.model.Book;
import com.nguyenthithao.model.CartItem;
import com.nguyenthithao.thestore.R;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BookAdapter2 extends ArrayAdapter<Book> {
    Activity context;
    int resource;

    public BookAdapter2(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(resource, null);
        ImageView imgBook = row.findViewById(R.id.imgBook);
        TextView bookName = row.findViewById(R.id.txtBookName);
        TextView unitPrice = row.findViewById(R.id.txtUnitPrice);
        TextView oldPrice = row.findViewById(R.id.txtOldPrice);
        TextView rating = row.findViewById(R.id.txtRating);
        TextView reviewNum = row.findViewById(R.id.txtReviewNum);

        Book book = getItem(position);
        bookName.setText(book.getName());
        unitPrice.setText(formatCurrency(book.getUnitPrice()) + "đ");

        if (book.getOldPrice() != 0) {
            oldPrice.setText(formatCurrency(book.getOldPrice()) + "đ");
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            oldPrice.setText("");
        }
        rating.setText(book.getRating() + "");
        reviewNum.setText("(" +book.getReviewNum()+")");
        new ImageLoadTask(imgBook).execute(book.getImageLink().get(0));
        return row;
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
