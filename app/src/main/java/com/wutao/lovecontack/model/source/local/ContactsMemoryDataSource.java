package com.wutao.lovecontack.model.source.local;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.model.source.ContactDataSource;

import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/6/6.
 */

public class ContactsMemoryDataSource implements ContactDataSource {

    private static ContactsMemoryDataSource INSTANCE;

    private ContactsMemoryDataSource(@NonNull Context context){}

    public static ContactsMemoryDataSource getInstance(@NonNull Context context){
        if(null == INSTANCE){
            INSTANCE = new ContactsMemoryDataSource(context);
        }

        return INSTANCE;
    }



    @Override
    public void getContactsList(@NonNull ContactDao contactDao, @NonNull LoadContactsCallback callback, @NonNull Activity context) {

    }

    @Override
    public void getContact(@NonNull String taskId, @NonNull GetContactCallback callback) {

    }

    @Override
    public void saveContact(@NonNull ContactDao contactDao, String photoPath, String name, String number1, double number2, @NonNull Activity context, @NonNull SaveCallback callback) {

    }

    @Override
    public void saveContact(@NonNull ContactDao contactDao, String photoPath, String name, String number1, double number2, @NonNull Activity context) {

    }

    @Override
    public void deleteContact(@NonNull ContactDao contactDao, @NonNull ContactBean contactBean, @NonNull DeleteState deleteState, @NonNull Activity context) {

    }


    @Override
    public void deleteContact(@NonNull ContactDao contactDao, @NonNull ContactBean contactBean, @NonNull DeleteState deleteState, @NonNull Activity context,@NonNull DeleteCallback callback) {

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
