package com.nguyenthithao.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.nguyenthithao.model.ReviewedBook;
import com.nguyenthithao.thestore.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ReviewedProductAdapter extends ArrayAdapter<ReviewedBook> {
    Activity context;
    int resource;
    public ReviewedProductAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(resource, null);
        TextView txtReviewerName = row.findViewById(R.id.txtReviewerName);
        RatingBar ratingBar = row.findViewById(R.id.ratingBar);
        ImageView imgProduct = row.findViewById(R.id.imgProduct);
        TextView txtProductName = row.findViewById(R.id.txtProductName);
        TextView txtReviewContent = row.findViewById(R.id.txtReviewContent);
        ImageView imgReview1 = row.findViewById(R.id.imgReview1);
        ImageView imgReview2 = row.findViewById(R.id.imgReview2);
        ImageView imgReview3 = row.findViewById(R.id.imgReview3);

        ReviewedBook reviewedBook = getItem(position);
        txtReviewerName.setText(reviewedBook.getReviewerName());
        ratingBar.setRating(reviewedBook.getRating());
        Picasso.get().load(reviewedBook.getBookImgUrl()).into(imgProduct);
        txtProductName.setText(reviewedBook.getBookName());
        txtReviewContent.setText(reviewedBook.getComment());
        ArrayList<String> imageUrls = reviewedBook.getImageUrls();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            Picasso.get().load(imageUrls.get(0)).into(imgReview1);
            if (imageUrls.size() > 1) {
                Picasso.get().load(imageUrls.get(1)).into(imgReview2);
            } else {
                imgReview2.setVisibility(View.GONE);
            }
            if (imageUrls.size() > 2) {
                Picasso.get().load(imageUrls.get(2)).into(imgReview3);
            } else {
                imgReview3.setVisibility(View.GONE);
            }
        } else {
            imgReview1.setVisibility(View.GONE);
            imgReview2.setVisibility(View.GONE);
            imgReview3.setVisibility(View.GONE);
        }
        return row;
    }
}
