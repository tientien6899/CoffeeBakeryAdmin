package com.example.coffeebakeryadmin.Banner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffeebakeryadmin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.Holder> {
    private List mPoster;
    private Context mContext;
    private DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    public BannerAdapter(List mPoster, Context context){
        this.mPoster = mPoster;
        this.mContext = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_poster, parent, false);
        return new BannerAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Banner bn = (Banner) mPoster.get(position);
        holder.name.setText(bn.getTen());
        Glide.with(holder.img.getContext()).load(bn.getLink()).into(holder.img);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Xác nhận xóa poster này?");
                builder.setNeutralButton("Quay lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                        .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mData.child("Poster").child(bn.getTen()).removeValue();
                                Toast.makeText(mContext, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mContext,ListBannerActivity.class);
                                mContext.startActivity(intent);
                                dialogInterface.cancel();
                            }
                        }).show();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, BannerActivity.class);
                intent.putExtra("NAME",bn.getTen());
                intent.putExtra("LINK",bn.getLink());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPoster.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img, delete;
        TextView name;
        public Holder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.poster_img);
            name = (TextView) itemView.findViewById(R.id.poster_name);
            delete = itemView.findViewById(R.id.poster_delete);
        }
    }
}
