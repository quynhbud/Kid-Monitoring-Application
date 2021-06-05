package com.example.kidmonitoring.model;

public class Application {
    private String mName;
    private String mPackage, mEmail;
    private byte[] mIcon;
    private boolean isChecked;

    public Application(){}
    public Application(String mEmail, String mName, String mPackage, byte[] mIcon, boolean isChecked) {
        this.mEmail=mEmail;
        this.mName = mName;
        this.mPackage = mPackage;
        this.mIcon = mIcon;
        this.isChecked = isChecked;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
