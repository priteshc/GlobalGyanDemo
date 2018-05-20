package com.example.globalgyan;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.globalgyan.Interface.OnStartDragListener;
import com.example.globalgyan.adapter.Item_Adapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

/**
 * Created by HP on 5/18/2018.
 */

public class List extends AppCompatActivity implements OnStartDragListener {


    private FloatingActionsMenu menuMultipleActions;
    private RecyclerView recyclerView;

    private Context context = List.this;

    private GlobalDatabase globalDatabase;
    private Cursor cursor;

    private ArrayList<NameListpojo>nameListpojos;

    private Item_Adapter itemAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private TextView texta;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewlist);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            List.this.getWindow().setStatusBarColor(getResources().getColor(R.color.warning_stroke_color));
        }


        globalDatabase = new GlobalDatabase(this);

        nameListpojos = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recordlist);

        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        View actionB = findViewById(R.id.action_a);

        texta = (TextView) findViewById(R.id.texta);

        FloatingActionButton actionC = (FloatingActionButton) findViewById(R.id.action_a);

        actionC.setTitle("Add");

        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuMultipleActions.collapse();
                Intent redirectAadhaar = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(redirectAadhaar);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(llm);

        nameListpojos.clear();

        globalDatabase.open();

        cursor = globalDatabase.getaddData();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            NameListpojo nameListpojo = new NameListpojo(cursor.getString(cursor.getColumnIndex(globalDatabase.KEY_FNAME)),cursor.getString(cursor.getColumnIndex(globalDatabase.KEY_LNAME)),cursor.getString(cursor.getColumnIndex(globalDatabase.KEY_EMAIL)),cursor.getString(cursor.getColumnIndex(globalDatabase.KEY_MOBILE)),cursor.getString(cursor.getColumnIndex(globalDatabase.KEY_IMAGE)),cursor.getInt(cursor.getColumnIndex(globalDatabase.KEY_PID)));
            nameListpojos.add(nameListpojo);
        }

        itemAdapter = new Item_Adapter(this,this);
        itemAdapter.SetList(nameListpojos);

        ItemTouchHelper.Callback callback = new EditItemTouchHelperCallback(itemAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        if(nameListpojos.size() <=0){

            texta.setVisibility(View.VISIBLE);

        }
        else {

            texta.setVisibility(View.GONE);
        }


        recyclerView.setAdapter(itemAdapter);


    }


    @Override
    public void onBackPressed() {
        finish();
    }




    @Override
    protected void onRestart() {
        super.onRestart();


        menuMultipleActions.collapse();

        nameListpojos.clear();

        cursor = globalDatabase.getaddData();


        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            NameListpojo nameListpojo = new NameListpojo(cursor.getString(cursor.getColumnIndex(globalDatabase.KEY_FNAME)),cursor.getString(cursor.getColumnIndex(globalDatabase.KEY_LNAME)),cursor.getString(cursor.getColumnIndex(globalDatabase.KEY_EMAIL)),cursor.getString(cursor.getColumnIndex(globalDatabase.KEY_MOBILE)),cursor.getString(cursor.getColumnIndex(globalDatabase.KEY_IMAGE)),cursor.getInt(cursor.getColumnIndex(globalDatabase.KEY_PID)));
            nameListpojos.add(nameListpojo);
        }


        if(nameListpojos.size() <=0){

            texta.setVisibility(View.VISIBLE);

        }
        else {

            texta.setVisibility(View.GONE);
        }


            itemAdapter.SetList(nameListpojos);



    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

        mItemTouchHelper.startDrag(viewHolder);

    }
}
