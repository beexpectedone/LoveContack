package com.wutao.lovecontack.function;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;

import com.wutao.lovecontack.Utils.DataBaseUtils;
import com.wutao.lovecontack.Utils.FileUtils;
import com.wutao.lovecontack.Utils.ToastUtils;
import com.wutao.lovecontack.application.LoveApplication;
import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.view.AddEditNewContactActivity;

import java.io.File;

import me.qianyue.dao.ContactDao;

/**
 * Created by qianyue.wang on 2017/5/24.
 */

public class SaveDataService extends Thread {

    private ContactDao mContactDao;
    private ContactBean mContactBean;
    private String mPhotoPath;
    private String mName;
    private String mNumber1;
    private double mNumber2;
    private Dialog mDialog;
    private Activity mAct;

    public SaveDataService(ContactDao contactDao, String photoPath, String name, String number1, double number2, Activity context){
        this.mContactDao = contactDao;
        this.mPhotoPath = photoPath;
        this.mName =name;
        this.mNumber1 = number1;
        this.mNumber2 = number2;
        mDialog = new Dialog(context);
        this.mAct = context;
    }

    @Override
    public void run() {
        if(null != mAct && null != mDialog && !mDialog.isShowing()){
            mAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.show();
                }
            });
            if(!TextUtils.isEmpty(mPhotoPath) && !TextUtils.isEmpty(mName)){
                if(FileUtils.writeImageFromSytem(mPhotoPath, mName)){ //如果存储照片成功
                    String contactPath = LoveApplication.mApplication.getSdDir() + File.separator + mName + ".jpg"; //设置图片文件存储路径

                    ContactBean contactBean = new ContactBean(mName,mNumber1,contactPath,mNumber2);
                    if(DataBaseUtils.search(mContactDao,contactBean)){
                        ToastUtils.showShortToastOnUIThrea(mAct,"联系人已存在");
                        return;
                    }

                    DataBaseUtils.insert(mContactDao,mPhotoPath,mName,mNumber1,mNumber2,mAct);
                    mAct.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(null!= mDialog && mDialog.isShowing()){
                                mDialog.dismiss();
                            }
                            ToastUtils.showShortToast(mAct,"插入数据成功");
                            if(mAct instanceof AddEditNewContactActivity){
                                mAct.finish();
                            }
                        }
                    });
//                mAddContactPresenter.saveContact(contactDao,contactBean);
                }
            }

        }

    }
}
