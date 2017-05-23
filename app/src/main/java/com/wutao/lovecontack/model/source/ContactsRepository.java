package com.wutao.lovecontack.model.source;

import android.support.annotation.NonNull;

import com.wutao.lovecontack.model.ContactBean;

import java.util.Date;

import me.qianyue.dao.Contact;
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
    public void getContactsList(@NonNull LoadContactsCallback callback) {
        /**这里做具体的获取数据库中数据的操作*/


    }

    @Override
    public void getContact(@NonNull String taskId, @NonNull GetContactCallback callback) {

    }

    @Override
    public void saveContact(@NonNull ContactDao contactDao, @NonNull ContactBean contactBean) {
        /**这里做具体的保存联系人的操作*/
//        mContactLocalSource.saveContact(contactBean);
        Date date = new Date();
        Contact contact = new Contact();
        contact.setName(contactBean.getName());
        contact.setDate(date);
        contact.setPhotoPath(contactBean.getPhotoPath());
        contact.setNumber(contactBean.getNumber());
        contact.setNumbertwo(contactBean.getNumber2());
        contactDao.insert(contact);
    }


}
