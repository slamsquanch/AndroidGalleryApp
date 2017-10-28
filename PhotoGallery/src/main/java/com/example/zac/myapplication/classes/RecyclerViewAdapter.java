package com.example.zac.myapplication.classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zac.myapplication.interfaces.ItemClickListener;
import com.example.zac.myapplication.R;

import java.util.ArrayList;

/**
 * Created by Zac on 2017-10-09.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<ImageHolder> {
    Context context;
    ArrayList<Image> list;

    public RecyclerViewAdapter(Context context, ArrayList<Image> images) {
        this.context = context;
        this.list = images;
    }

    // Initialize view holder.
    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout, null);
        ImageHolder holder = new ImageHolder(v);
        return holder;

    }

    // Bind the views to data.
    @Override
    public void onBindViewHolder(ImageHolder holder, final int position) {
        holder.text.setText(list.get(position).getImgName());
        holder.img.setImageResource(list.get(position).getID());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemclick(View view, int pos) {
                Toast.makeText(context, list.get(position).getImgName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Total count of items.
    @Override
    public int getItemCount() {
        return 0;
    }
}
