package com.wutao.lovecontack.model.source.function;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.wutao.lovecontack.Utils.DataBaseUtils;
import com.wutao.lovecontack.Utils.FileUtils;
import com.wutao.lovecontack.Utils.ToastUtils;
import com.wutao.lovecontack.application.LoveApplication;
import com.wutao.lovecontack.model.ContactBean;

import java.io.File;

import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/5/30.
 */

public class SaveDataAsyncTask extends AsyncTask<ContactBean, Object,Object> {

    private ProgressDialogHandler mHandler;
    private Activity mAct;
    private ContactDao mContactDao;
    private String mPhotoPath;
    private boolean insertSuccess;

    public SaveDataAsyncTask(ContactDao contactDao, String photoPath,ProgressDialogHandler handler,Activity act){
        this.mContactDao = contactDao;
        this.mPhotoPath = photoPath; //这里的photoPath是图片原先存储在手机中的路径
        this.mHandler = handler; //主线程的handler
        this.mAct = act;
    }

    @Override
    protected void onPreExecute() {
        //在后台开启一条线程之前，该方法在祝线程执行
        if(null != mHandler){
            mHandler.sendEmptyMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG);
        }
    }

    @Override
    protected Object doInBackground(ContactBean[] params) {
        /**具体实现  存储联系人信息  和  存储照片*/
        ContactBean contactBean = params[0];
        if(null != contactBean){
            if(!TextUtils.isEmpty(mPhotoPath) && !TextUtils.isEmpty(contactBean.getName())){
                if(FileUtils.writeImageFromSytem(mPhotoPath, contactBean.getName())){ //如果存储照片成功
                    String contactPath = LoveApplication.mApplication.getSdDir() + File.separator + contactBean.getName() + ".jpg"; //设置图片文件存储路径
                    if(DataBaseUtils.search(mContactDao,contactBean)){
                        ToastUtils.showShortToastOnUIThread(mAct,"联系人已存在");
                        return null;
                    }
                    DataBaseUtils.insert(mContactDao,contactPath,contactBean.getName(),contactBean.getNumber(),contactBean.getNumber2(),mAct);
                    ToastUtils.showShortToastOnUIThread(mAct,"插入数据成功");
                    insertSuccess = true;
                }
            }
        }

        return null;
    }

    @Override/** 在主线程执行  */
    protected void onPostExecute(Object o) {
        if(null != mHandler){
            mHandler.sendEmptyMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG);
        }
        if(null != mAct && insertSuccess){ //如果insertSuccess为false，说明插入不成功，联系人已经存在。这时需要重新插入联系人
            mAct.finish();
        }

    }
}
