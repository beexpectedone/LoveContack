package com.wutao.lovecontack.model.source;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.wutao.lovecontack.model.ContactBean;

import java.util.List;

import me.qianyue.dao.ContactDao;

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


    interface SaveCallback{
        void saveSuccess();
        void saveFailure();
    }

    interface DeleteCallback{
        void deleteSuccess();
        void deleteFailure();
    }

    interface DeleteState{
        void deleteSuccess();

        void deleteFailure();
    }


    void getContactsList(@NonNull ContactDao contactDao, @NonNull LoadContactsCallback callback,@NonNull Activity context);

    void getContact(@NonNull String taskId, @NonNull GetContactCallback callback);

    void saveContact(@NonNull ContactDao contactDao, String photoPath, String name, String number1, double number2,@NonNull Activity context, @NonNull SaveCallback callback);

    void saveContact(@NonNull ContactDao contactDao, String photoPath, String name, String number1, double number2,@NonNull Activity context);

    void deleteContact(@NonNull ContactDao contactDao,@NonNull ContactBean contactBean, @NonNull DeleteState deleteState,@NonNull Activity context);

    void deleteContact(@NonNull ContactDao contactDao,@NonNull ContactBean contactBean, @NonNull DeleteState deleteState,@NonNull Activity context,@NonNull DeleteCallback callback);

    void deleteAllContacts();

    void saveTemplate(ContactBean contactBean);

    void refreshContacts();
}
