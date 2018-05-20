package com.example.globalgyan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pritesh on 6/15/2017.
 */

public class GlobalDatabase {



    private static final String DATABASE_NAME = "global";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_REG = "register";

    public static final String KEY_PID = "id";

    public static final String KEY_FNAME = "fname";
    public static final String KEY_LNAME = "lname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_IMAGE = "image";









    private Dbhelper oHelper;
    private final Context ocontext;
    private SQLiteDatabase oDtabase;



    private static class Dbhelper extends SQLiteOpenHelper {

        public Dbhelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(" CREATE TABLE " + DATABASE_REG + " (" + KEY_PID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_FNAME + " TEXT, " + KEY_LNAME + " TEXT, " + KEY_EMAIL + " TEXT, " + KEY_MOBILE + " TEXT, " + KEY_IMAGE + " TEXT);"
            );




        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXIST" + DATABASE_REG);
            onCreate(db);

        }
    }

    public GlobalDatabase(Context c) {
        ocontext = c;
        oHelper = new Dbhelper(c);
        oDtabase = oHelper.getWritableDatabase();
    }

    public GlobalDatabase open() {
        oHelper = new Dbhelper(ocontext);
        oDtabase = oHelper.getWritableDatabase();
        oDtabase = oHelper.getReadableDatabase();
        return this;

    }

    public void close() {
        oHelper.close();

    }



    public long Regentry (String name, String lname,String email,String mobile,String imgpath){



        ContentValues lcv = new ContentValues();

        lcv.put(KEY_FNAME,name);
        lcv.put(KEY_LNAME,lname);
        lcv.put(KEY_EMAIL,email);
        lcv.put(KEY_MOBILE,mobile);
        lcv.put(KEY_IMAGE,imgpath);


        return oDtabase.insert(DATABASE_REG, null, lcv);


    }

    public void Updateentry (String name, String lname,String email,String mobile,String imgpath,int id){

        ContentValues lcv = new ContentValues();

        lcv.put(KEY_FNAME,name);
        lcv.put(KEY_LNAME,lname);
        lcv.put(KEY_EMAIL,email);
        lcv.put(KEY_MOBILE,mobile);
        lcv.put(KEY_IMAGE,imgpath);


        oDtabase.update(DATABASE_REG, lcv, KEY_PID + "=" + id, null);


    }




    public Cursor getaddData() {
        // TODO Auto-generated method stub

        Cursor ssyll1 = oDtabase.query(DATABASE_REG, null, null, null, null, null,
                null);


        return ssyll1;
    }


/*
    public long contactentry (String customername, String contactid){


       // deleteitm();

        ContentValues lcv = new ContentValues();

        lcv.put(KEY_NAME,customername);
        lcv.put(KEY_CSTID,contactid);

        return oDtabase.insert(DATABASE_CONTACT, null, lcv);


    }
*/

   /* public Cursor getdata(String name,String brand,String flvr,String weight) {

        Cursor c = oDtabase.rawQuery(
                "SELECT * FROM " + DATABASE_PURCHASEITEM + " WHERE "
                        + KEY_INAME + "='" + name +"'AND " + KEY_IBRAND + "='" + brand +"'AND " + KEY_IFLVR + "='" + flvr +"'AND " + KEY_IWEIGHT + "='" + weight +"'" , null);

        return c;
    }
*/




    public void deleteItem1(String id){


        oDtabase.execSQL("delete from " + DATABASE_REG + " WHERE " + KEY_PID + "=" + id);

    }




}
