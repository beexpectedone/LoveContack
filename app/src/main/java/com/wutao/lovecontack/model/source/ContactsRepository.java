package com.wutao.lovecontack.model.source;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.wutao.lovecontack.Utils.DataBaseUtils;
import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.model.source.function.ProgressDialogHandler;
import com.wutao.lovecontack.model.source.function.SaveDataAsyncTask;

import java.util.List;

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
    public void getContactsList(@NonNull final ContactDao contactDao, @NonNull final LoadContactsCallback callback) {
        /**这里做具体的获取数据库中数据的操作*/
        new Thread(new Runnable() {
            @Override
            public void run() {
               List<ContactBean> contactBeanList =  DataBaseUtils.search(contactDao);
                if(null!= contactBeanList){
                    callback.onContactsLoaded(contactBeanList);
                }else {
                    callback.onDataNotAvailable();
                }
            }
        }).start();
    }

    @Override
    public void getContact(@NonNull String taskId, @NonNull GetContactCallback callback) {

    }

    @Override
    public void saveContact(@NonNull ContactDao contactDao, String photoPath, String name, String number1, double number2,@NonNull Activity context) {
//        SaveDataService saveDataService = new SaveDataService(contactDao,photoPath,name,number1,number2,context);
//        saveDataService.start();
        String currentThread = Thread.currentThread().getName();
        SaveDataAsyncTask saveDataAsyncTask = new SaveDataAsyncTask(contactDao,photoPath,new ProgressDialogHandler(context),context);
        ContactBean contactBean = new ContactBean(name,number1,photoPath,number2);
        saveDataAsyncTask.execute(contactBean,null,null);

    }

}
