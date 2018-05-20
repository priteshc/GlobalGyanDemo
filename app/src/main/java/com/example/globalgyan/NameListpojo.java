package com.example.globalgyan;

/**
 * Created by HP on 5/19/2018.
 */

public class NameListpojo {

    private String fname,lname,email,mobno,imgpath;
    private int id;

    public NameListpojo(String fname, String lname, String email, String mobno, String imgpath,int id) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.mobno = mobno;
        this.imgpath = imgpath;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobno() {
        return mobno;
    }

    public void setMobno(String mobno) {
        this.mobno = mobno;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }
}
