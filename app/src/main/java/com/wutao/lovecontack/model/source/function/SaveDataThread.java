package com.wutao.lovecontack.model.source.function;

import android.app.Activity;
import android.text.TextUtils;

import com.wutao.lovecontack.Utils.DataBaseUtils;
import com.wutao.lovecontack.Utils.FileUtils;
import com.wutao.lovecontack.application.LoveApplication;
import com.wutao.lovecontack.model.ContactBean;

import java.io.File;

import me.qianyue.dao.ContactDao;

/**
 * Created by qianyue.wang on 2017/5/24.
 *
 * 添加联系人第一种方法：直接在后台开启一条线程，缺点是优先级较低容易被杀死。
 */

public class SaveDataThread extends Thread {

    private ContactDao mContactDao;
    private String mPhotoPath;
    private String mName;
    private String mNumber1;
    private double mNumber2;
    private Activity mAct;
    private ProgressDialogHandler mHandler; //主线程的handler

    /** 这里使用新建一条线程的方式获取资源，参数通过构造函数传递过来 */
    public SaveDataThread(ContactDao contactDao, String photoPath, String name, String number1, double number2,
                          Activity context, ProgressDialogHandler handler ){
        this.mContactDao = contactDao;
        this.mPhotoPath = photoPath;
        this.mName =name;
        this.mNumber1 = number1;
        this.mNumber2 = number2;
        this.mAct = context;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        if(null != mAct ){
            mHandler.sendEmptyMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG);
            if(!TextUtils.isEmpty(mPhotoPath) && !TextUtils.isEmpty(mName)){
                if(FileUtils.writeImageFromSytem(mPhotoPath, mName)){ //如果存储照片成功
                    String contactPath = LoveApplication.mApplication.getSdDir() + File.separator + mName + ".jpg"; //设置图片文件存储路径
                    ContactBean contactBean = new ContactBean(mName,mNumber1,contactPath,mNumber2);
                    if(DataBaseUtils.search(mContactDao,contactBean)){
                        mHandler.sendEmptyMessageDelayed(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG,2000);
                        return;
                    }
                    DataBaseUtils.insert(mContactDao,mPhotoPath,mName,mNumber1,mNumber2,mAct);
                    mHandler.sendEmptyMessageDelayed(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG,2000);
                    mAct.finish();
                }
            }
        }

        Thread.currentThread().interrupt();  //手动调用该方法终结线程
    }
}
