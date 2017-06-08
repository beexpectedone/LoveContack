package com.wutao.lovecontack.model.source;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.view.MainActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/5/21.
 * 获取联系人列表的M层对象
 */

public class ContactsRepository implements ContactDataSource{

    private static ContactsRepository INSTANCE = null;

    private final ContactDataSource mContactDataBaseSource;

//    private final ContactDataSource mContactMemorySource;

    /**
     * 存储在内存中的 缓存数据， 如果数据没有改变取这里的数据就好了
     */
    Map<String, ContactBean> mCachedContacts;

    /**
     * 判断数据是否已经改变
     */
    boolean dataChanged = false;

    private ContactsRepository(/*@NonNull ContactDataSource contactMemorySource,*/@NonNull  ContactDataSource contactDataBaseSource) {
//        mContactMemorySource = contactMemorySource;
        mContactDataBaseSource = contactDataBaseSource;
    }


    public static ContactsRepository getInstance(/*ContactDataSource contactMemorySource,*/ContactDataSource contactDataBaseSource){
        if (null == INSTANCE){
            INSTANCE = new ContactsRepository(/*contactMemorySource,*/contactDataBaseSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }

    public ContactsRepository(){
        mContactDataBaseSource = null;
//        mContactMemorySource = null;
    }


    @Override
    public void getContactsList(@NonNull final ContactDao contactDao, @NonNull final LoadContactsCallback callback,@NonNull final Activity context) {

        /** 这里要能将map转化成List集合 */
//        SaveDataHandlerThread saveDataHandlerThread = new SaveDataHandlerThread("handle_thread",contactDao, ((MainActivity)context).mHandler,callback);
//        saveDataHandlerThread.start();
//        saveDataHandlerThread.getLooper();
//        saveDataHandlerThread.saveDataHandler.sendEmptyMessage(SaveDataHandlerThread.MSG_CONTACT_LIST_INFO);

        if(null != mCachedContacts && !dataChanged){ //数据没改变，从缓存中读取数据就可以
            callback.onContactsLoaded(new ArrayList<>(mCachedContacts.values()));
            return;
        }

        if(dataChanged){ //如果数据源已经改变，那么就需要从数据库中重新获取到数据
            /**这里做具体的获取数据库中数据的操作*/
            getContactsFromDataBaseDataSource(contactDao, callback, (MainActivity) context);
        }

        /*else {

            mContactMemorySource.getContactsList(contactDao,new LoadContactsCallback() {
                @Override
                public void onContactsLoaded(List<ContactBean> contacts) {
                    refreshCache(contacts);
                    callback.onContactsLoaded(new ArrayList<>(mCachedContacts.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getContactsList(contactDao,callback,context);
                }
            },context);
        }*/

    }

    private void getContactsFromDataBaseDataSource(@NonNull ContactDao contactDao, @NonNull final LoadContactsCallback callback, @NonNull MainActivity context) {
        if(null != mContactDataBaseSource){
            mContactDataBaseSource.getContactsList(contactDao,new LoadContactsCallback() { /** 这个callback是为了更新缓存数据的 */
                @Override
                public void onContactsLoaded(List<ContactBean> contacts) {
                    refreshCache(contacts);
//                refreshMemoryDataSource(contacts);
                    callback.onContactsLoaded(new ArrayList<>(mCachedContacts.values())); /** 这里的callback是为了更新UI的 */
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            },context);
        }
    }

//    private void refreshMemoryDataSource(List<ContactBean> contacts) {
//        mContactMemorySource.deleteAllContacts();
//        for(ContactBean contact : contacts){
//            mContactMemorySource.saveTemplate(contact);
//        }
//    }

    private void refreshCache(List<ContactBean> contacts) {
        if(null == mCachedContacts){
            mCachedContacts = new LinkedHashMap<>();
        }
        mCachedContacts.clear();
        for (ContactBean contact : contacts){
            mCachedContacts.put(contact.getName(),contact);
        }

        dataChanged = false;
    }

    @Override
    public void getContact(@NonNull String taskId, @NonNull GetContactCallback callback) {

    }

    @Override
    public void saveContact(@NonNull ContactDao contactDao, final String photoPath, final String name, final String number1, final double number2,
                            @NonNull Activity context, @NonNull final SaveCallback saveCallback) { /** 港真，这个callback没用 */

        /** 单独开启一条线程进行存储 */
//        SaveDataThread saveDataThread = new SaveDataThread(contactDao,photoPath,name,number1,number2,context,((AddEditNewContactActivity)context).mHandler);
//        saveDataThread.start();

        if(null != mContactDataBaseSource){
//            mContactDataBaseSource.saveContact(contactDao, photoPath, name, number1, number2, context, new SaveCallback() { /** 这里的callback就是为了更新缓存数据的 */
//                @Override
//                public void saveSuccess() {
//                    if(null == mCachedContacts){
//                        mCachedContacts = new LinkedHashMap<>();
//                    }
//                    mCachedContacts.put(name,new ContactBean(name,number1,photoPath,number2));
//                }
//
//                @Override
//                public void saveFailure() {
//
//                }
//            });
            mContactDataBaseSource.saveContact(contactDao,photoPath,name,number1,number2,context,saveCallback);
        }
    }

    @Override
    public void saveContact(@NonNull ContactDao contactDao, final String photoPath, final String name, final String number1, final double number2, @NonNull Activity context) {
        saveContact(contactDao,photoPath,name,number1,number2,context,new SaveCallback() { /** 这里的callback就是为了更新缓存数据的 */
        @Override
        public void saveSuccess() {
            if(null == mCachedContacts){
                mCachedContacts = new LinkedHashMap<>();
            }
            mCachedContacts.put(name,new ContactBean(name,number1,photoPath,number2));
        }

            @Override
            public void saveFailure() {

            }
        });
    }

    @Override
    public void deleteContact(@NonNull ContactDao contactDao, @NonNull final ContactBean contactBean, @NonNull DeleteState deleteState, @NonNull Activity context) {
        if(null != mContactDataBaseSource){
            mContactDataBaseSource.deleteContact(contactDao, contactBean, deleteState, context, new DeleteCallback() {
                @Override
                public void deleteSuccess() {
                    if(null != mCachedContacts){ //删除后，缓存中的数据也要更新
                        mCachedContacts.remove(contactBean.getName());
                        Log.i("","");
                    }
                }

                @Override
                public void deleteFailure() {

                }
            });
        }
    }

    @Override
    public void deleteContact(@NonNull final ContactDao contactDao, @NonNull final ContactBean contactBean, @NonNull final DeleteState deleteState,@NonNull Activity context,@NonNull DeleteCallback callback) {

        if(null != mContactDataBaseSource){
            mContactDataBaseSource.deleteContact(contactDao,contactBean,deleteState,context,callback);
        }
        /** 实现删除任务的功能 */
//        SaveDataHandlerThread saveDataHandlerThread = new SaveDataHandlerThread("handler_thread",contactDao,contactBean, ((MainActivity)context).mHandler,deleteState);
//        saveDataHandlerThread.start();
//        saveDataHandlerThread.getLooper();
//        saveDataHandlerThread.saveDataHandler.sendEmptyMessage(SaveDataHandlerThread.MSG_DELETE_INFO);
    }

    @Override
    public void deleteAllContacts() {
//        mContactMemorySource.deleteAllContacts();
        if(null != mContactDataBaseSource){
            mContactDataBaseSource.deleteAllContacts();
            if(null == mCachedContacts){
                mCachedContacts = new LinkedHashMap<>();
            }
            mCachedContacts.clear();
        }
    }

    @Override
    public void saveTemplate(ContactBean contactBean) {
//        mContactMemorySource.saveTemplate(contactBean);
        mContactDataBaseSource.saveTemplate(contactBean);

        if(null == mCachedContacts){
            mCachedContacts = new LinkedHashMap<>();
        }
        mCachedContacts.put(contactBean.getName(),contactBean);
    }

    @Override
    public void refreshContacts() {
        dataChanged = true;
    }
}
