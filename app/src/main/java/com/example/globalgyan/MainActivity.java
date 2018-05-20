package com.example.globalgyan;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
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
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity {

    private ImageButton cam;
    private CircleImageView circleImageView;
    private static final int GALLERY_REQUEST1 = 15;
    private Context context = MainActivity.this;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private PermissionUtil.PermissionRequestObject mBothPermissionRequest;
    private static final int REQUEST_CODE_BOTH = 3;
    private Button reg;
    private EditText fname,lname,emailid,mobilemo;
    private ValidationUtils validationUtils;
    private GlobalDatabase globalDatabase;
    private Bitmap bitmap;
    private String fileName;
    private FileOutputStream output;
    private String s,id,path,msg;
    private ProgressDialog dialog;

    private SharedPreferences sharedPreferences;
    private TextInputLayout email1,mob;
    private RippleView rippleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            MainActivity.this.getWindow().setStatusBarColor(getResources().getColor(R.color.warning_stroke_color));
        }



        cam = (ImageButton) findViewById(R.id.cam);
        circleImageView = (CircleImageView) findViewById(R.id.profile_image);

        reg = (Button) findViewById(R.id.log);

        rippleView = (RippleView) findViewById(R.id.more);

        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        emailid = (EditText) findViewById(R.id.email);
        mobilemo = (EditText) findViewById(R.id.mobile);

        email1 = (TextInputLayout) findViewById(R.id.emi);
        mob = (TextInputLayout) findViewById(R.id.mob);
        validationUtils = new ValidationUtils(this);

        globalDatabase = new GlobalDatabase(this);

        dialog = new ProgressDialog(this);

        dialog.setTitle("please wait");
        dialog.setCancelable(false);

        sharedPreferences = this.getSharedPreferences("REG",0);

        if(getIntent().getExtras()!=null){

            fname.setText(getIntent().getExtras().getString("name"));
            lname.setText(getIntent().getExtras().getString("lname"));
            emailid.setText(getIntent().getExtras().getString("email"));
            mobilemo.setText(getIntent().getExtras().getString("mob"));
            id = getIntent().getExtras().getString("id");
            path = getIntent().getExtras().getString("path");

//            Log.d("ID",id);

            Picasso.with(context)
                    .load(getIntent().getExtras().getString("path"))
                    .resize(100,100)
                    .centerCrop()
                    .error(R.drawable.profile)      // optional// optional
                    .into(circleImageView);

        }

        mBothPermissionRequest = PermissionUtil.with(MainActivity.this).request(WRITE_EXTERNAL_STORAGE, READ_STORAGE).onResult(new Func2() {
            @Override
            protected void call(int requestCode, String[] permissions, int[] grantResults) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        doOnPermissionGranted(permissions[i]);
                    } else {
                        doOnPermissionDenied(permissions[i]);
                    }
                }
            }
        }).ask(REQUEST_CODE_BOTH);



        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT > 22) {


                    int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {


                        mBothPermissionRequest = PermissionUtil.with(MainActivity.this).request(WRITE_EXTERNAL_STORAGE, READ_STORAGE).onResult(new Func2() {
                            @Override
                            protected void call(int requestCode, String[] permissions, int[] grantResults) {
                                for (int i = 0; i < permissions.length; i++) {
                                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                        doOnPermissionGranted(permissions[i]);
                                    } else {
                                        doOnPermissionDenied(permissions[i]);
                                    }
                                }
                            }
                        }).ask(REQUEST_CODE_BOTH);
                    }
                    else {

                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, GALLERY_REQUEST1);


                    }
                } else {


                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST1);


                }
            }
        });

        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {


                if (!fname.getText().toString().isEmpty() && !lname.getText().toString().isEmpty() && !emailid.getText().toString().isEmpty() && !mobilemo.getText().toString().isEmpty()) {

                    if (!validationUtils.isValidEmail(emailid.getText().toString())) {

                        Toast.makeText(context, "enter valid email address", Toast.LENGTH_SHORT).show();

                    }else if (mobilemo.getText().toString().length() != 10) {

                        Toast.makeText(context, "enter 10 digit mobile no", Toast.LENGTH_SHORT).show();
                    }
                    else if (!validationUtils.isValidMobile(mobilemo.getText().toString())) {


                        Toast.makeText(context, "enter valid mobile no", Toast.LENGTH_SHORT).show();

                    }
                    else if(circleImageView.getDrawable() == null){

                        Toast.makeText(context, "please upload your picture", Toast.LENGTH_SHORT).show();

                    }

                    else {

                        fileName = fname.getText().toString()+".png";
                        BitmapDrawable drawable = (BitmapDrawable) circleImageView.getDrawable();
                        bitmap = drawable.getBitmap();

                        //  saveImg(bitmap);
                        AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
                        asyncTaskRunner.execute();

                    }
                }

                else {


                    Toast.makeText(context, R.string.error_field_required, Toast.LENGTH_SHORT).show();

                }


            }
        });

      //  rippleView.setRippleDuration(1);
        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                }
        });

    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            File f = new File(Environment.getExternalStorageDirectory() +"/Protine/");
            f.mkdirs();


            File dest = new File(f, fileName);
            Uri imageUri = Uri.fromFile(dest);
            s = imageUri.toString();

            try {

                output = new FileOutputStream(dest);
                // Compress into png format image from 0% - 100%



                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, output);
                output.flush();
                output.close();

                //     dialog.dismiss();
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //      imageView.setImageDrawable(null);


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            dialog.dismiss();

            globalDatabase.open();

            if(id == null || id.isEmpty()) {

                globalDatabase.Regentry(fname.getText().toString(), lname.getText().toString(), emailid.getText().toString(), mobilemo.getText().toString(), s);

            //    Toast.makeText(context, "Save Successfully", Toast.LENGTH_SHORT).show();

                msg = "register Successfully";

                popuup(msg);

                sharedPreferences.edit().putString("YES","ya").apply();

            }
            else {


                globalDatabase.Updateentry(fname.getText().toString(), lname.getText().toString(), emailid.getText().toString(), mobilemo.getText().toString(), s,Integer.parseInt(id));

              //  Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show();


                msg = "update Successfully";

                popuup(msg);

            }


            globalDatabase.close();




        }
    }

    public void popuup(String msg1){

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Regiser")
                .setContentText(msg1)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismiss();



                        Intent intent = new Intent(context,List.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                }).show();




    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == GALLERY_REQUEST1 && resultCode == Activity.RESULT_OK){

            Uri uri = data.getData();

             s = getRealPathFromURI(uri);

            File actualFile=new File(s);



            try {

                Bitmap   compressedImageBitmap = Compressor.getDefault(this).compressToBitmap(actualFile);


                // Bitmap photo = (Bitmap) MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                circleImageView.setImageBitmap(compressedImageBitmap);

            }
            catch (Exception e){

                e.printStackTrace();
            }

        }

    }

    public String getRealPathFromURI(Uri uri) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }



    private void doOnPermissionDenied(String permission) {
        // updateStatus(permission + " Permission Denied or is on \"Do Not SHow Again\"");
    }

    private void doOnPermissionGranted(String permission) {
        //   updateStatus(permission + " Permission Granted");

//        Toast.makeText(context,permission,Toast.LENGTH_SHORT).show();

        /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image*//*");
*/
       /* Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(photoPickerIntent, GALLERY_REQUEST1);

*/
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (mBothPermissionRequest != null) {

            mBothPermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
