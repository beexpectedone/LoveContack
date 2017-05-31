package com.wutao.lovecontack.model.source;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.model.source.function.SaveDataHandlerThread;
import com.wutao.lovecontack.model.source.function.SaveDataService;
import com.wutao.lovecontack.view.AddEditNewContactActivity;
import com.wutao.lovecontack.view.MainActivity;

import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/5/21.
 * 获取联系人列表的M层对象
 */

public class ContactsRepository implements ContactDataSource{

    private static ContactsRepository INSTANCE = null;

//    private final ContactDataSource mContactLocalSource;

//    public ContactsRepository(ContactDataSource mContactLocalSource) {
//        this.mContactLocalSource = mContactLocalSource;
//    }
//
//
//    public static ContactsRepository getInstance(ContactDataSource contactLocalDataSource){
//        if (null == INSTANCE){
//            INSTANCE = new ContactsRepository(contactLocalDataSource);
//        }
//        return INSTANCE;
//    }

    public ContactsRepository(){}


    @Override
    public void getContactsList(@NonNull final ContactDao contactDao, @NonNull final LoadContactsCallback callback,@NonNull Activity context) {
        /**这里做具体的获取数据库中数据的操作*/
        SaveDataHandlerThread saveDataHandlerThread = new SaveDataHandlerThread("handle_thread",contactDao,((MainActivity)context).mHandler,callback);
        saveDataHandlerThread.start();
        saveDataHandlerThread.getLooper();
        saveDataHandlerThread.saveDataHandler.sendEmptyMessage(SaveDataHandlerThread.MSG_CONTACT_LIST_INFO);
    }

    @Override
    public void getContact(@NonNull String taskId, @NonNull GetContactCallback callback) {

    }

    @Override
    public void saveContact(@NonNull ContactDao contactDao, String photoPath, String name, String number1, double number2,@NonNull Activity context) {

        /** 单独开启一条线程进行存储 */
        SaveDataService saveDataService = new SaveDataService(contactDao,photoPath,name,number1,number2,context,((AddEditNewContactActivity)context).mHandler);
        saveDataService.start();

        String currentThread = Thread.currentThread().getName();
        /** 使用 Asynctask 类存储信息 */
//        SaveDataAsyncTask saveDataAsyncTask = new SaveDataAsyncTask(contactDao,photoPath,((AddEditNewContactActivity)context).mHandler,context);
//        ContactBean contactBean = new ContactBean(name,number1,photoPath,number2);
//        saveDataAsyncTask.execute(contactBean,null,null);

        /** 使用 HandlerThread 进行存储 */
//        ((AddEditNewContactActivity)context).mHandler = new ProgressDialogHandler(contactDao,photoPath,context,new ContactBean(name,number1,photoPath,number2));
//        ((AddEditNewContactActivity)context).mHandler.sendEmptyMessage(ProgressDialogHandler.INSERT_DATA);
        SaveDataHandlerThread saveDataHandlerThread = new SaveDataHandlerThread("handler_thread",contactDao,photoPath,context,
                new ContactBean(name,number1,photoPath,number2),((AddEditNewContactActivity)context).mHandler);
        saveDataHandlerThread.start();
        saveDataHandlerThread.getLooper();
        saveDataHandlerThread.saveDataHandler.sendEmptyMessage(SaveDataHandlerThread.MSG_SAVE_INFO);
    }

    @Override
    public void deleteContact(@NonNull final ContactDao contactDao, @NonNull final ContactBean contactBean, @NonNull final DeleteState deleteState,@NonNull Activity context) {
        /** 实现删除任务的功能 */
        SaveDataHandlerThread saveDataHandlerThread = new SaveDataHandlerThread("handler_thread",contactDao,contactBean, ((MainActivity)context).mHandler,deleteState);
        saveDataHandlerThread.start();
        saveDataHandlerThread.getLooper();
        saveDataHandlerThread.saveDataHandler.sendEmptyMessage(SaveDataHandlerThread.MSG_DELETE_INFO);
    }

}
