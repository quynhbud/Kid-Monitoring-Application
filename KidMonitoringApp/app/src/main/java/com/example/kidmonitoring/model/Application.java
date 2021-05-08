package com.example.kidmonitoring.model;

public class Application {
    private String mName;
    private String mPackage, mEmail;
    private byte[] mIcon;
    private boolean isChecked;

    public Application(){}
    public Application(String mEmail, String mName, String mPackage, byte[] mIcon) {
        this.mEmail=mEmail;
        this.mName = mName;
        this.mPackage = mPackage;
        this.mIcon = mIcon;
    }


    public byte[] getmIcon() {
        return mIcon;
    }

    public void setmIcon(byte[] mIcon) {
        this.mIcon = mIcon;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPackage() {
        return mPackage;
    }

    public void setmPackage(String mPackage) {
        this.mPackage = mPackage;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }
}
