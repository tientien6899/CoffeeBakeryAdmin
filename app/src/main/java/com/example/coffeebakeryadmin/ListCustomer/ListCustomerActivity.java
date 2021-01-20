package com.example.coffeebakeryadmin.ListCustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.coffeebakeryadmin.AdminActivity;
import com.example.coffeebakeryadmin.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListCustomerActivity extends AppCompatActivity {
    RecyclerView danhsach;
    CustomerAdapter adapter;
    private DatabaseReference danhsachRef, mData;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);
        AnhXa();
        mData = FirebaseDatabase.getInstance().getReference();
        danhsach.setLayoutManager(new LinearLayoutManager(this));
        danhsachRef = mData.child("KHACHHANG");
        FirebaseRecyclerOptions<Customer> options =
                new FirebaseRecyclerOptions.Builder<Customer>()
                        .setQuery(danhsachRef, new SnapshotParser<Customer>() {
                            @NonNull
                            @Override
                            public Customer parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Customer(snapshot.child("gmail").getValue().toString(),
                                        snapshot.child("hoten").getValue().toString(),
                                        snapshot.child("sdt").getValue().toString(),
                                        snapshot.child("sonha").getValue().toString(),
                                        snapshot.child("phuong").getValue().toString(),
                                        snapshot.child("quan").getValue().toString(),
                                        snapshot.child("thanhpho").getValue().toString());
                            }
                        })
                        .build();
        adapter = new CustomerAdapter(options);
        danhsach.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListCustomerActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

    }
    private void AnhXa() {
        danhsach = (RecyclerView) findViewById(R.id.list_customer);
        back = (ImageView) findViewById(R.id.btn_BackCustomer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}