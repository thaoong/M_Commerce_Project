package com.nguyenthithao.thestore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyenthithao.model.Address;
import com.nguyenthithao.thestore.databinding.ActivityAddAddressBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class AddAddressActivity extends AppCompatActivity {
    ActivityAddAddressBinding binding;
    ArrayAdapter<String> provinceAdapter, districtAdapter, wardAdapter;
    public static final String DATABASE_NAME = "diachivietnam.db";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;

    DatabaseReference addressRef;
    FirebaseAuth mAuth;

    private void copyDataBase(){
        try{
            File dbFile = getDatabasePath(DATABASE_NAME);
            if(!dbFile.exists()){
                if(CopyDBFromAsset()){
                    Toast.makeText(AddAddressActivity.this,
                            "Copy database successful!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(AddAddressActivity.this,
                            "Copy database fail!", Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            Log.e("Error: ", e.toString());
        }
    }

    private boolean CopyDBFromAsset() {
        String dbPath = getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024]; int length;
            while((length=inputStream.read(buffer))>0){
                outputStream.write(buffer,0, length);
            }
            outputStream.flush();  outputStream.close(); inputStream.close();
            return  true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_address);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        copyDataBase();
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        addressRef = FirebaseDatabase.getInstance().getReference().child("addresses").child(userId).child("addaddresses");
        loadProvince();
        loadDistrict();
        loadWard();
        addEvents();
    }

    private void addEvents() {
        binding.btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });
    }

    private void saveAddress() {
        String name = binding.edtName.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();
        String province = binding.edtProvince.getText().toString().trim();
        String district = binding.edtDistrict.getText().toString().trim();
        String ward = binding.edtWard.getText().toString().trim();
        String street = binding.edtStreet.getText().toString().trim();
        boolean isDefault = binding.chkDefaultAddress.isChecked();

        if (!name.isEmpty() && !phone.isEmpty() && !province.isEmpty() && !district.isEmpty() && !ward.isEmpty() && !street.isEmpty()) {
            Address address = new Address(name, phone, province, district, ward, street, isDefault);

            String addressId = addressRef.push().getKey();
            addressRef.child(addressId).setValue(address);

            Toast.makeText(AddAddressActivity.this, "Address saved successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.putExtra("address", (CharSequence) address);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(AddAddressActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadWard() {
        ArrayList<String> wardsArr = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM ward", null);
        while (cursor.moveToNext())
        {
            String wardName = cursor.getString(1);
            wardsArr.add(wardName);
        }
        cursor.close();
        String[] wards = wardsArr.toArray(new String[wardsArr.size()]);
        wardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wards);
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerWard.setAdapter(wardAdapter);
        binding.spinnerWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                binding.edtWard.setText(wards[arg2]);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                binding.edtWard.setText("");
            }
        });
    }

    private void loadDistrict() {
        ArrayList<String> districtsArr = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM district", null);
        while (cursor.moveToNext())
        {
            String districtName = cursor.getString(1);
            districtsArr.add(districtName);
        }
        cursor.close();
        String[] districts = districtsArr.toArray(new String[districtsArr.size()]);
        districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDistrict.setAdapter(districtAdapter);
        binding.spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                binding.edtDistrict.setText(districts[arg2]);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                binding.edtDistrict.setText("");
            }
        });
    }

    private void loadProvince() {
        ArrayList<String> provincesArr = new ArrayList<>();
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM province", null);
        while (cursor.moveToNext())
        {
            String provinceName = cursor.getString(1);
            provincesArr.add(provinceName);
        }
        cursor.close();
        String[] provinces = provincesArr.toArray(new String[provincesArr.size()]);
        provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerProvince.setAdapter(provinceAdapter);
        binding.spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                binding.edtProvince.setText(provinces[arg2]);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                binding.edtProvince.setText("");
            }
        });
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strAddNewAddress);
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
