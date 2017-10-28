package com.example.zac.myapplication.classes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zac.myapplication.interfaces.ItemClickListener;

/**
 * Created by Zac on 2017-10-09.
 */

public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView img;
    TextView text;
    ItemClickListener itemClickListener;

    public ImageHolder(View itemView) {
        super(itemView);
    }

    public void setItemClickListener(ItemClickListener i) {
        this.itemClickListener = i;
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemclick(view, getLayoutPosition());
    }
}
