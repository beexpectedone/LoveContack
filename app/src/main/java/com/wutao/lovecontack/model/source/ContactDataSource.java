package com.wutao.lovecontack.model.source;

import android.support.annotation.NonNull;

import com.wutao.lovecontack.model.ContactBean;

import java.util.List;

/**
 * Created by mingyue on 2017/5/21.
 */

public interface ContactDataSource {

    interface LoadContactsCallback{
        void onContactsLoaded(List<ContactBean> contacts);

        void onDataNotAvailable();
    }

    interface GetContactCallback{
        void onContactLoaded(ContactBean contactBean);

        void onDataNotAvailable();
    }



    void getContactsList(@NonNull LoadContactsCallback callback);

    void getContact(@NonNull String taskId, @NonNull GetContactCallback callback);

    void saveContact(@NonNull ContactBean contactBean);


}
