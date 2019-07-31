package com.lexieluv.homeworkseventeenth;


import android.graphics.drawable.Drawable;

public class AppInfo {
    private Drawable img;
    private String name;
    private String packageName;

    public AppInfo(){ }
    public AppInfo(Drawable img, String name, String packageName) {
        this.img = img;
        this.name = name;
        this.packageName = packageName;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "img=" + img +
                ", name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
