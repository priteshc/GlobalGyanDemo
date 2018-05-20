package com.example.globalgyan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.globalgyan.Interface.ItemTouchHelperAdapter;
import com.example.globalgyan.Interface.ItemTouchHelperViewHolder;
import com.example.globalgyan.Interface.OnStartDragListener;
import com.example.globalgyan.NameListpojo;
import com.example.globalgyan.R;
import com.example.globalgyan.User_Details;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pritesh on 5/24/2017.
 */

public class Item_Adapter extends RecyclerView.Adapter<Item_Adapter.Item_Holder> implements ItemTouchHelperAdapter {

    Context mContext;

    private  ArrayList<NameListpojo>nameListpojos1 ;

    private int lastPosition = -1;
    OnItemClickListener mItemClickListener;
    private static final int TYPE_ITEM = 0;
    private final LayoutInflater mInflater;
    private final OnStartDragListener mDragStartListener;



    public Item_Adapter(Context c,OnStartDragListener dragListner) {
        this.mInflater = LayoutInflater.from(c);
        mDragStartListener = dragListner;
        mContext = c;


    }

    public void SetList(ArrayList<NameListpojo>nameListpojos){

        //      purchaseListPojos.clear();


        this.nameListpojos1 = nameListpojos;

        notifyDataSetChanged();

    }


    @Override
    public Item_Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {


            View v = mInflater.inflate(R.layout.cardview, parent, false);
            return new Item_Holder(v );

        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }
    @Override
    public void onBindViewHolder(final Item_Holder holder, final int position) {


        final NameListpojo data = nameListpojos1.get(position);

        holder.name.setText(data.getFname()+" "+data.getLname());

        Log.d("name",data.getImgpath());

        Picasso.with(mContext)
                .load(data.getImgpath())
                .resize(100,100)
                .centerCrop()
                .error(R.drawable.profile)
                .memoryPolicy(MemoryPolicy.NO_CACHE)// optional// optional
                .into(holder.img);


        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, User_Details.class);
                intent.putExtra("name",data.getFname());
                intent.putExtra("lname",data.getLname());
                intent.putExtra("email",data.getEmail());
                intent.putExtra("mob",data.getMobno());
                intent.putExtra("path",data.getImgpath());
                intent.putExtra("id",data.getId());
                mContext.startActivity(intent);



            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });

         setAnimation(holder.itemView, position);


    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slideleft);
            animation.setStartOffset(position*100);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }



    @Override
    public int getItemCount() {
        return nameListpojos1.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public class Item_Holder extends RecyclerView.ViewHolder implements View.OnClickListener ,ItemTouchHelperViewHolder {

        public TextView name;
        public CircleImageView img;
        public LinearLayout lin;

        public Item_Holder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.proName);
            img = itemView.findViewById(R.id.imageView);

            lin = itemView.findViewById(R.id.lin);
            //  continer = itemView.findViewById(R.id.container);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);

        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);

        }
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < nameListpojos1.size() && toPosition < nameListpojos1.size()) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(nameListpojos1, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(nameListpojos1, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

        nameListpojos1.remove(position);
        notifyItemRemoved(position);
    }


    public void updateList(ArrayList<NameListpojo> list) {
        nameListpojos1 = list;
        notifyDataSetChanged();
    }
}
