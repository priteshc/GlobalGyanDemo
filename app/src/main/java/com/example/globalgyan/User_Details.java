package com.example.globalgyan;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.globalgyan.utils.ValidationUtils;
import com.github.kayvannj.permission_utils.Func2;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class User_Details extends AppCompatActivity {

    private ImageButton cam;
    private CircleImageView circleImageView;
    private PermissionUtil.PermissionRequestObject mBothPermissionRequest;
    private EditText fname,lname,emailid,mobilemo;
    private String id,path;
  private Context context = User_Details.this;
    private Toolbar toolbar;
    private GlobalDatabase globalDatabase;
    private RippleView dell,edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            User_Details.this.getWindow().setStatusBarColor(getResources().getColor(R.color.warning_stroke_color));
        }


        circleImageView = (CircleImageView) findViewById(R.id.profile_image);


        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        emailid = (EditText) findViewById(R.id.email);
        mobilemo = (EditText) findViewById(R.id.mobile);

        dell = (RippleView) findViewById(R.id.del);
        edit = (RippleView) findViewById(R.id.edit);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        globalDatabase = new GlobalDatabase(this);

        if(getIntent().getExtras()!=null){

            fname.setText(getIntent().getExtras().getString("name"));
            lname.setText(getIntent().getExtras().getString("lname"));
            emailid.setText(getIntent().getExtras().getString("email"));
            mobilemo.setText(getIntent().getExtras().getString("mob"));
            id = String.valueOf(getIntent().getExtras().getInt("id"));
            path = getIntent().getExtras().getString("path");

            Picasso.with(context)
                    .load(getIntent().getExtras().getString("path"))
                    .resize(100,100)
                    .centerCrop()
                    .error(R.drawable.profile)      // optional// optional
                    .into(circleImageView);



        }


      dell.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


              new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                      .setTitleText("Delete")
                      .setContentText("Are you sure?")
                      .setConfirmText("Ok")
                      .setCancelText("Cancel")
                      .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                          @Override
                          public void onClick(SweetAlertDialog sweetAlertDialog) {

                              sweetAlertDialog.dismiss();

                              globalDatabase.open();

                              globalDatabase.deleteItem1(id);

                              globalDatabase.close();

                              popuup("deleted successfully");

                          }
                      }).
                      setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                          @Override
                          public void onClick(SweetAlertDialog sweetAlertDialog) {

                              sweetAlertDialog.dismiss();

                          }
                      }).
                      show();




          }
      });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("name",fname.getText().toString());
                intent.putExtra("lname",lname.getText().toString());
                intent.putExtra("email",emailid.getText().toString());
                intent.putExtra("mob",mobilemo.getText().toString());
                intent.putExtra("path",path);
                intent.putExtra("id",id);
                startActivity(intent);
                 finish();
                }
        });

    }

    public void popuup(String msg1){

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Delete")
                .setContentText(msg1)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismiss();
                        Intent intent = new Intent(context, List.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();



                    }
                }).show();

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
