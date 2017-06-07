package com.wutao.lovecontack.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by qianyue.wang on 2017/6/7.
 */

public class AdvertiseBean implements Parcelable{
    //传过来的广告显示多长时间的秒数
    private int time;

    // 分辨是否有新图片、是否需要强制更新
    private int code; //暂定 code为9时强制更新，code为0时有新图片，code为1时没有新图片

    private String url; // 图片下载的地址或者是强制更新的地址

    public AdvertiseBean(Parcel in) {
        time = in.readInt();
        code = in.readInt();
        url = in.readString();
    }

    public AdvertiseBean() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(time);
        dest.writeInt(code);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<AdvertiseBean> CREATOR = new Parcelable.Creator<AdvertiseBean>() {
        @Override
        public AdvertiseBean createFromParcel(Parcel in) {
            return new AdvertiseBean(in);
        }

        @Override
        public AdvertiseBean[] newArray(int size) {
            return new AdvertiseBean[size];
        }
    };

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
