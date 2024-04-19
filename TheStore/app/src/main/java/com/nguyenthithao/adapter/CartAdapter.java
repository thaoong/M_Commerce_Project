package com.nguyenthithao.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyenthithao.model.CartItem;
import com.nguyenthithao.thestore.CartActivity;
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
    private SparseBooleanArray selectedItems;

    public CartAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.selectedItems = new SparseBooleanArray();
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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference myRef = firebaseDatabase.getReference("carts").child(userId).child("products");
                myRef.child(cartItem.getID()).removeValue();
                Toast.makeText(context, "Delete product successfully", Toast.LENGTH_SHORT).show();
                context.recreate();
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cartItem.getQuantity();
                if (quantity > 1) {
                    quantity--;
                    cartItem.setQuantity(quantity);
                    updateCartItemQuantity(cartItem.getID(), quantity);
                    context.recreate();
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cartItem.getQuantity();
                quantity++;
                cartItem.setQuantity(quantity);
                updateCartItemQuantity(cartItem.getID(), quantity);
                context.recreate();
            }
        });

        boolean isBuyAllChecked = ((CartActivity) context).isBuyAllChecked();
        if (isBuyAllChecked) {
            chkBuy.setOnCheckedChangeListener(null);
            chkBuy.setChecked(selectedItems.get(position));
            chkBuy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setChecked(position, isChecked);
                }
            });
        } else {
            chkBuy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectedItems.put(position, isChecked);
                    updateTextBuyButton();
                    updateTotalValue();
                }
            });
        }
        return cart_item;
    }

    public void updateTextBuyButton() {
        int count = 0;
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.valueAt(i)) {
                count++;
            }
        }
        ((CartActivity) context).updateSelectedCount(count);
    }

    public void updateTotalValue() {
        float totalValue = 0.0f;
        for (int i = 0; i < selectedItems.size(); i++) {
            int position = selectedItems.keyAt(i);
            if (selectedItems.valueAt(i)) {
                CartItem cartItem = getItem(position);
                totalValue += cartItem.getUnitPrice() * cartItem.getQuantity();
            }
        }
        String totalValueString = formatCurrency(totalValue) +"đ";
        // Gửi giá trị tổng về CartActivity
        ((CartActivity) context).updateTotalValue(totalValueString);
    }

    private void updateCartItemQuantity(String cartItemId, int quantity) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = firebaseDatabase.getReference("carts").child(userId).child("products").child(cartItemId).child("quantity");
        myRef.setValue(quantity);
    }

    public void setChecked(int position, boolean isChecked) {
        selectedItems.put(position, isChecked);
        notifyDataSetChanged();
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
