package com.wutao.lovecontack.model;

/**
 * Created by mingyue on 2017/5/21.
 */

public class ContactBean {

    public ContactBean(String name, double number, String photoPath,double number2){
        this.name = name;
        this.number = number;
        this.number2 = number2 == 0? 0: number2;
        this.photoPath = photoPath;
    }

    private String name;
    private double number;
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

    public double getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getNumber2() {
        return number2;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public boolean isEmpty() {
        return (name == null || "".equals(name)) ||
                (photoPath == null || "".equals(photoPath)) || number == 0 && number2 == 0;
    }
}
