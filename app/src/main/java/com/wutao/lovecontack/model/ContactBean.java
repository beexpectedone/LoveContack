package com.wutao.lovecontack.model;

/**
 * Created by mingyue on 2017/5/21.
 */

public class ContactBean {

    public ContactBean(){}

    public ContactBean(String name, String number, String photoPath,double number2){
        this.name = name;
        this.number = number;
        this.number2 = number2 == 0? 0: number2;
        this.photoPath = photoPath;
    }

    private String name;
    private String number;
    private double number2;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getNumber2() {
        return number2;
    }

    public void setNumber2(double number2) {
        this.number2 = number2;
    }

    public boolean isEmpty() {
        return (name == null || "".equals(name)) ||
                (photoPath == null || "".equals(photoPath)) || (photoPath == null || "".equals(photoPath));
    }
}
