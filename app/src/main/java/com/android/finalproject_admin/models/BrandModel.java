package com.android.finalproject_admin.models;

public class BrandModel {
    String img_url;
    String name;

    public BrandModel() {
    }

    public BrandModel(String img_url, String name) {
        this.img_url = img_url;
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
