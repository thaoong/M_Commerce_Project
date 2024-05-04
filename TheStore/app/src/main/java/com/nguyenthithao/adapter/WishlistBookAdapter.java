package com.nguyenthithao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.thestore.ProductDetailActivity;
import com.nguyenthithao.thestore.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WishlistBookAdapter extends BaseAdapter {
    ArrayList<Book> items;
    Context context;
    LayoutInflater inflater;
    FirebaseAuth mAuth;

    public WishlistBookAdapter(Context context, ArrayList<Book> items) {
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_wishlist, parent, false);
        }
        TextView txtName = view.findViewById(R.id.txtName);
        txtName.setText(items.get(position).getName());

        float unitPrice = items.get(position).getUnitPrice();
        String formattedUnitPrice = formatCurrency(unitPrice);
        TextView txtPrice = view.findViewById(R.id.txtPrice);
        txtPrice.setText(formattedUnitPrice + "đ");

        float oldPrice = items.get(position).getOldPrice();
        if (oldPrice!= 0) {
            String formattedOldPrice = formatCurrency(oldPrice);
            TextView txtOldPrice = view.findViewById(R.id.txtOldPrice);
            txtOldPrice.setText(formattedOldPrice + "đ");
            txtOldPrice.setPaintFlags(txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            TextView txtOldPrice = view.findViewById(R.id.txtOldPrice);
            txtOldPrice.setText("");
        }

        ImageView imgFavoBook = view.findViewById(R.id.imgFavoBook);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());

        Glide.with(context)
                .load(items.get(position).getImageLink().get(0))
                .apply(requestOptions)
                .into(imgFavoBook);
        ImageView imgDelete = view.findViewById(R.id.imgDelete);

        CheckBox chkBuy = view.findViewById(R.id.chkBuy);
        chkBuy.setChecked(items.get(position).isBuy());
        chkBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = (int) v.getTag(); // Get the position from the tag
                Book book = items.get(currentPosition);
                boolean isChecked = chkBuy.isChecked();
                book.setBuy(isChecked);
                notifyDataSetChanged();
            }
        });
        chkBuy.setTag(position); // Set the position as a tag for the checkbox

        final int currentPosition = position; // store the position in a final variable
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = items.get(currentPosition); // use the stored position
                String userId = mAuth.getCurrentUser().getUid();
                String bookId = book.getId();
                DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("wishlists").child(userId).child("books").child(bookId);

                // Remove item from adapter
                items.remove(currentPosition);
                notifyDataSetChanged();

                // Remove item from database
                wishlistRef.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to remove from wishlist", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("SELECTED_BOOK", items.get(position));
                context.startActivity(intent);
            }
        });

        return view;
    }

    private String formatCurrency(float price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price);
    }

}