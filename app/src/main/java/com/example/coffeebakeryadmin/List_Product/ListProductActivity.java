package com.example.coffeebakeryadmin.List_Product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeebakeryadmin.AdminActivity;
import com.example.coffeebakeryadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListProductActivity extends AppCompatActivity {

    ImageView back, add;
    Intent intent;
    RecyclerView danhsach;
    ProductAdapter adapter;
    SearchView timkiem;
    ArrayList<Product> arrayList;
    private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        AnhXa();
        arrayList = new ArrayList<Product>();
        mData = FirebaseDatabase.getInstance().getReference();
        danhsach.setLayoutManager(new LinearLayoutManager(this));

        mData.child("Sản Phẩm").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    Product p = snap.getValue(Product.class);
                    arrayList.add(p);
                }
                adapter = new ProductAdapter(arrayList,ListProductActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListProductActivity.this);
                danhsach.setAdapter(adapter);
                danhsach.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if(timkiem != null){
            timkiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ListProductActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AnhXa() {
        back = findViewById(R.id.btn_BackDSSP);
        add = (ImageView) findViewById(R.id.btn_AddProduct);
        timkiem = (SearchView) findViewById(R.id.searchView) ;
        add = (ImageView) findViewById(R.id.btn_AddProduct);
        danhsach = (RecyclerView) findViewById(R.id.rv_ListProduct);
    }

    private void search(String s){
        ArrayList<Product> list = new ArrayList<>();
        for(Product obj : arrayList){
            if(obj.getTensp().toLowerCase().contains(s.toLowerCase())){
                list.add(obj);
            }
        }
        ProductAdapter adapter1 = new ProductAdapter(list,ListProductActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListProductActivity.this);
        danhsach.setAdapter(adapter1);
        danhsach.setLayoutManager(linearLayoutManager);
    }
}