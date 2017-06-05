package com.wutao.lovecontack.model.source.function;

import android.app.Activity;
import android.text.TextUtils;

import com.wutao.lovecontack.Utils.DataBaseUtils;
import com.wutao.lovecontack.Utils.FileUtils;
import com.wutao.lovecontack.Utils.ToastUtils;
import com.wutao.lovecontack.application.LoveApplication;
import com.wutao.lovecontack.model.ContactBean;

import java.io.File;

import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/6/1.
 */

public class SaveDataThreadPool implements Runnable {

    private ContactDao mContactDao;
    private String mPhotoPath;
    private String mName;
    private String mNumber1;
    private double mNumber2;
    private Activity mAct;
    private ProgressDialogHandler mHandler; //主线程的handler

    /** 构造函数传递参数 */
    public SaveDataThreadPool(ContactDao contactDao, String photoPath, String name, String number1, double number2,
                              Activity context, ProgressDialogHandler handler){
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
                        mHandler.sendEmptyMessageDelayed(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG,1000);
                        ToastUtils.showShortToastOnUIThread(mAct,"联系人已存在！");
                        return;
                    }
                    DataBaseUtils.insert(mContactDao,mPhotoPath,mName,mNumber1,mNumber2,mAct);
                    mHandler.sendEmptyMessageDelayed(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG,1000);
                    mAct.finish();
                }
            }
        }
    }
}
