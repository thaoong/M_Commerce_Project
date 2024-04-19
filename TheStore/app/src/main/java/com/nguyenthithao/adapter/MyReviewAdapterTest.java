package com.nguyenthithao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nguyenthithao.model.MyReviewTest;
import com.nguyenthithao.thestore.R;

import java.util.List;

public class MyReviewAdapterTest extends ArrayAdapter<MyReviewTest> {

    Activity context;
    int resource;
    List<MyReviewTest> objects;
    public MyReviewAdapterTest(@NonNull Activity context, int resource, List<MyReviewTest> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        TextView txtName = row.findViewById(R.id.txtProductName);
        TextView txtDate = row.findViewById(R.id.txtDate);
        TextView txtReview = row.findViewById(R.id.txtReview);
        TextView txtDayReview = row.findViewById(R.id.txtDayReview);
        TextView txtLike = row.findViewById(R.id.txtLike);
        TextView txtComment = row.findViewById(R.id.txtComment);


        MyReviewTest myReviewTest = this.objects.get(position);

        txtName.setText(myReviewTest.getName());
        txtDate.setText(myReviewTest.getDate());
        txtReview.setText(myReviewTest.getReview());
        txtDayReview.setText(myReviewTest.getDayReview());
        txtLike.setText(String.valueOf(myReviewTest.getLike()));
        txtComment.setText(String.valueOf(myReviewTest.getComment()));

        return row;
    }
}
