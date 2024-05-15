package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.PaymentMethodAdapter;
import com.nguyenthithao.model.PaymentMethod;
import com.nguyenthithao.thestore.databinding.ActivityPaymentMethodBinding;

public class PaymentMethodActivity extends AppCompatActivity {
    ActivityPaymentMethodBinding binding;
    PaymentMethodAdapter paymentMethodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_payment_method);
        binding = ActivityPaymentMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        loadPaymentMethod();
        addEvents();
    }

    private void addEvents() {
        binding.lvPaymentMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PaymentMethod p = paymentMethodAdapter.getItem(position);
                if (p != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("SELECTED_PAYMENT_METHOD", p.getName());
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    private void loadPaymentMethod() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("paymentMethods");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dss : snapshot.getChildren()){
                    PaymentMethod paymentMethod = dss.getValue(PaymentMethod.class);
                    String key = dss.getKey();
                    paymentMethod.setId(key);
                    paymentMethodAdapter.add(paymentMethod);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        paymentMethodAdapter = new PaymentMethodAdapter(PaymentMethodActivity.this, R.layout.item_payment_method);
        binding.lvPaymentMethod.setAdapter(paymentMethodAdapter);
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strPaymentMethod);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>"+title+"</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}