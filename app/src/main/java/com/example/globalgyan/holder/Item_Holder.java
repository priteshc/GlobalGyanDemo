package com.example.globalgyan.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.globalgyan.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by pritesh on 5/24/2017.
 */

public class Item_Holder extends RecyclerView.ViewHolder {

    public TextView name;
    public CircleImageView img;
     public LinearLayout lin;

    public Item_Holder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.proName);
         img = itemView.findViewById(R.id.imageView);

        lin = itemView.findViewById(R.id.lin);
      //  continer = itemView.findViewById(R.id.container);

    }
}
