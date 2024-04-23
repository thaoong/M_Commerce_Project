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
        new PaymentMethodAdapter.ImageLoadTask(imgPaymentMethod).execute(paymentMethod.getImageLink());

        return row;
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
}
