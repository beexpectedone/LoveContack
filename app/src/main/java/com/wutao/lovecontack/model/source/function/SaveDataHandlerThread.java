package com.wutao.lovecontack.model.source.function;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import com.wutao.lovecontack.Utils.DataBaseUtils;
import com.wutao.lovecontack.Utils.FileUtils;
import com.wutao.lovecontack.Utils.ToastUtils;
import com.wutao.lovecontack.application.LoveApplication;
import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.model.source.ContactDataSource;

import java.io.File;
import java.util.List;

import me.qianyue.dao.ContactDao;

import static com.wutao.lovecontack.model.source.function.ProgressDialogHandler.DISMISS_PROGRESS_DIALOG;
import static com.wutao.lovecontack.model.source.function.ProgressDialogHandler.SHOW_PROGRESS_DIALOG;

/**
 * Created by mingyue on 2017/5/31.
 */

public class SaveDataHandlerThread extends HandlerThread{

    private Activity mAct;
    private ContactDao mContactDao;
    private String mPhotoPath;
    private ContactBean mContactBean;
    private ProgressDialogHandler mHandler;
    private ContactDataSource.DeleteState mDeleteState;

    public static final int MSG_UPDATE_INFO = 0x110;
    public static final int MSG_SAVE_INFO = 0x111; //原来大小写切换 是和图床工具冲突
    public static final int MSG_DELETE_INFO = 0x112;
    public static final int MSG_CONTACT_LIST_INFO = 0x113;
    private ContactDataSource.LoadContactsCallback mCallback;

    public SaveDataHandlerThread(String name, ContactDao contactDao, String photoPath, Activity act,
                                 ContactBean contactBean,ProgressDialogHandler handler) {
        super(name);
        this.mContactDao = contactDao;
        this.mPhotoPath = photoPath;
        this.mAct = act;
        this.mContactBean = contactBean;
        this.mHandler = handler;
    }

    public SaveDataHandlerThread(String name, ContactDao contactDao, ContactBean contactBean, ProgressDialogHandler handler, ContactDataSource.DeleteState deleteState){
        super(name);
        this.mContactDao = contactDao;
        this.mContactBean = contactBean;
        this.mDeleteState = deleteState;
        this.mHandler = handler;
    }

    public SaveDataHandlerThread(String name, ContactDao contactDao, ProgressDialogHandler handler, ContactDataSource.LoadContactsCallback callback){
        super(name);
        this.mContactDao = contactDao;

        this.mCallback = callback;
        this.mHandler = handler;
    }


    public Handler saveDataHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_SAVE_INFO:
                    saveData();
                    break;
                case MSG_DELETE_INFO:
                    deleteData();
                    break;
                case MSG_CONTACT_LIST_INFO:
                    getContactsList();
                    break;
            }
        }
    };

    private void getContactsList() {
        mHandler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
        List<ContactBean> contactBeanList =  DataBaseUtils.search(mContactDao);
        if(null!= contactBeanList){
            mCallback.onContactsLoaded(contactBeanList);
        }else {
            mCallback.onDataNotAvailable();
        }
        mHandler.sendEmptyMessage(DISMISS_PROGRESS_DIALOG);
    }

    private void deleteData() {
        if(null != mContactBean){
            mHandler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
            DataBaseUtils.delete(mContactDao,mContactBean);
            if(!DataBaseUtils.search(mContactDao,mContactBean)){
                mDeleteState.deleteSuccess();
            }else {
                mDeleteState.deleteFailure();
            }
            mHandler.sendEmptyMessage(DISMISS_PROGRESS_DIALOG);
        }
    }

    private void saveData() {
        if(null != mContactBean){
            if(!TextUtils.isEmpty(mPhotoPath) && !TextUtils.isEmpty(mContactBean.getName())){
                mHandler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
                if(FileUtils.writeImageFromSytem(mPhotoPath, mContactBean.getName())){ //如果存储照片成功
                    String contactPath = LoveApplication.mApplication.getSdDir() + File.separator + mContactBean.getName() + ".jpg"; //设置图片文件存储路径
                    if(DataBaseUtils.search(mContactDao,mContactBean)){
                        ToastUtils.showShortToastOnUIThread(mAct,"联系人已存在");
                        mHandler.sendEmptyMessage(DISMISS_PROGRESS_DIALOG);
                        return ;
                    }
                    DataBaseUtils.insert(mContactDao,contactPath,mContactBean.getName(),mContactBean.getNumber(),mContactBean.getNumber2(),mAct);
                    ToastUtils.showShortToastOnUIThread(mAct,"插入数据成功");
                    mHandler.sendEmptyMessage(DISMISS_PROGRESS_DIALOG);
                    mAct.finish();
                }
            }
        }
    }

}
