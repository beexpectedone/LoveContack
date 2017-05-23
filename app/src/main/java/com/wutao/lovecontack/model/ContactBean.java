package com.wutao.lovecontack.model;

/**
 * Created by mingyue on 2017/5/21.
 */

public class ContactBean {

    public ContactBean(String name, int number, String photoPath){

    }

    private String name;
    private int number;
    private String photoPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isEmpty() {
        return (name == null || "".equals(name)) &&
                (photoPath == null || "".equals(photoPath)) && number == 0;
    }
}
