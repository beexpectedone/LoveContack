package com.wutao.lovecontack.model.source.remote;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.wutao.lovecontack.Utils.ThreadManager;
import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.model.source.ContactDataSource;
import com.wutao.lovecontack.model.source.function.SaveDataHandlerThread;
import com.wutao.lovecontack.model.source.function.SaveDataThreadPool;
import com.wutao.lovecontack.view.AddEditNewContactActivity;
import com.wutao.lovecontack.view.MainActivity;

import java.util.LinkedHashMap;
import java.util.Map;

import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/6/6.
 */

public class ContactsRemoteDataSource implements ContactDataSource {

    private static ContactsRemoteDataSource INSTANCE;  //

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;//

    private final static Map<String, ContactBean> CONTACTS_SERVICE_DATA = new LinkedHashMap<>();


    private ContactsRemoteDataSource(){}

    /**
     * 还是以INSTANCE的样式新建对象，私有化构造函数
     * @return
     */
    public static ContactsRemoteDataSource getInstance(){
        if(null == INSTANCE){
            INSTANCE = new ContactsRemoteDataSource();
        }
        return INSTANCE;
    }


    @Override
    public void getContactsList(@NonNull ContactDao contactDao, @NonNull LoadContactsCallback callback, @NonNull Activity context) {
        /** 这里要能将map转化成List集合 */
        SaveDataHandlerThread saveDataHandlerThread = new SaveDataHandlerThread("handle_thread",contactDao, ((MainActivity)context).mHandler,callback);
        saveDataHandlerThread.start();
        saveDataHandlerThread.getLooper();
        saveDataHandlerThread.saveDataHandler.sendEmptyMessage(SaveDataHandlerThread.MSG_CONTACT_LIST_INFO);
    }


    @Override
    public void getContact(@NonNull String taskId, @NonNull GetContactCallback callback) {

    }

    @Override
    public void saveContact(@NonNull ContactDao contactDao, String photoPath, String name, String number1, double number2, @NonNull Activity context, @NonNull SaveCallback saveCallback) {
        /** 使用 线程池 的方式存储数据 */
        SaveDataThreadPool saveDataThreadPool = new SaveDataThreadPool(contactDao,photoPath,name,number1,
                number2,context,((AddEditNewContactActivity)context).mHandler, saveCallback);
        ThreadManager.getShortPool().execute(saveDataThreadPool);

        /** 使用 Asynctask 类存储信息 */
//        SaveDataAsyncTask saveDataAsyncTask = new SaveDataAsyncTask(contactDao,photoPath,((AddEditNewContactActivity)context).mHandler,context);
//        ContactBean contactBean = new ContactBean(name,number1,photoPath,number2);
//        saveDataAsyncTask.execute(contactBean,null,null);

        /** 使用 HandlerThread 进行存储 */
//        ((AddEditNewContactActivity)context).mHandler = new ProgressDialogHandler(contactDao,photoPath,context,new ContactBean(name,number1,photoPath,number2));
//        ((AddEditNewContactActivity)context).mHandler.sendEmptyMessage(ProgressDialogHandler.INSERT_DATA);
//        SaveDataHandlerThread saveDataHandlerThread = new SaveDataHandlerThread("handler_thread",contactDao,photoPath,context,
//                new ContactBean(name,number1,photoPath,number2),((AddEditNewContactActivity)context).mHandler);
//        saveDataHandlerThread.start();
//        saveDataHandlerThread.getLooper();
//        saveDataHandlerThread.saveDataHandler.sendEmptyMessage(SaveDataHandlerThread.MSG_SAVE_INFO);
    }

    @Override
    public void saveContact(@NonNull ContactDao contactDao, String photoPath, String name, String number1, double number2, @NonNull Activity context) {

    }

    @Override
    public void deleteContact(@NonNull ContactDao contactDao, @NonNull ContactBean contactBean, @NonNull DeleteState deleteState, @NonNull Activity context) {

    }

    @Override
    public void deleteContact(@NonNull ContactDao contactDao, @NonNull ContactBean contactBean, @NonNull DeleteState deleteState, @NonNull Activity context,@NonNull DeleteCallback callback) {
        /** 实现删除任务的功能 */
        SaveDataHandlerThread saveDataHandlerThread = new SaveDataHandlerThread("handler_thread",contactDao,contactBean, ((MainActivity)context).mHandler,deleteState,callback);
        saveDataHandlerThread.start();
        saveDataHandlerThread.getLooper();
        saveDataHandlerThread.saveDataHandler.sendEmptyMessage(SaveDataHandlerThread.MSG_DELETE_INFO);
    }

    @Override
    public void deleteAllContacts() {

    }

    @Override
    public void saveTemplate(ContactBean contactBean) {

    }

    @Override
    public void refreshContacts() {

    }
}
