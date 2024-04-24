package com.nguyenthithao.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nguyenthithao.model.Address;
import com.nguyenthithao.thestore.R;

import java.util.List;

public class AddressAdapter extends ArrayAdapter<Address> {
    private Activity mContext;
    private int mResource;

    public AddressAdapter(@NonNull Activity context, int resource, @NonNull List<Address> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        Address address = getItem(position);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtPhoneNumber = convertView.findViewById(R.id.txtPhoneNumber);
        TextView txtStreet = convertView.findViewById(R.id.txtStreet);
        TextView txtAddress = convertView.findViewById(R.id.txtAddress);
        TextView txtDefault = convertView.findViewById(R.id.textView47);

        if (address != null) {
            txtName.setText(address.getName());
            txtPhoneNumber.setText(address.getPhone());
            txtStreet.setText(address.getStreet());
            txtAddress.setText(address.getWard() + ", " + address.getDistrict() + ", " + address.getProvince());

            if (address.isDefault()) {
                txtDefault.setVisibility(View.VISIBLE);
            } else {
                txtDefault.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}
