package com.wutao.lovecontack.model.source.function;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.wutao.lovecontack.model.ContactBean;

import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/5/30.
 */

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1; //
    public static final int DISMISS_PROGRESS_DIALOG = 2;//
    public static final int INSERT_DATA = 3; //

    private ProgressDialog mDialog;
    private Activity mAct;
    private ContactDao mContactDao;
    private String mPhotoPath;
    private ContactBean mContactBean;


    public ProgressDialogHandler(Activity act){
        super();
        this.mAct = act;
    }

    public ProgressDialogHandler(ContactDao contactDao, String photoPath, Activity act, ContactBean contactBean){
        this.mContactDao = contactDao;
        this.mPhotoPath = photoPath;
        this.mAct = act;
        this.mContactBean = contactBean;
    }

    /**
     *初始化dialog对象
     */
    private void initDialog(){

        if(mDialog == null){
            mDialog = new ProgressDialog(mAct);
        }

        if(!mDialog.isShowing()){
            mDialog.show();
        }
    }

    /**
     *使得dialog消失
     */
    private void dismissDialog(){
        if(mDialog != null){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case SHOW_PROGRESS_DIALOG:
                initDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissDialog();
                break;
//            case INSERT_DATA:
//                insertAndUpdate();
//                break;
        }
    }

//    private void insertAndUpdate() {
//        if(null != mContactBean){
//            if(!TextUtils.isEmpty(mPhotoPath) && !TextUtils.isEmpty(mContactBean.getName())){
//                this.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
//                if(FileUtils.writeImageFromSytem(mPhotoPath, mContactBean.getName())){ //如果存储照片成功
//                    String contactPath = LoveApplication.mApplication.getSdDir() + File.separator + mContactBean.getName() + ".jpg"; //设置图片文件存储路径
//                    if(DataBaseUtils.search(mContactDao,mContactBean)){
//                        ToastUtils.showShortToastOnUIThread(mAct,"联系人已存在");
//                        this.sendEmptyMessage(DISMISS_PROGRESS_DIALOG);
//                        return ;
//                    }
//                    DataBaseUtils.insert(mContactDao,contactPath,mContactBean.getName(),mContactBean.getNumber(),mContactBean.getNumber2(),mAct);
//                    ToastUtils.showShortToastOnUIThread(mAct,"插入数据成功");
//                    this.sendEmptyMessage(DISMISS_PROGRESS_DIALOG);
//                    mAct.finish();
//                }
//            }
//        }
//    }
}
