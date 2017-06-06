package com.wutao.lovecontack.model.source;

import android.content.Context;
import android.support.annotation.NonNull;

import com.wutao.lovecontack.model.source.remote.ContactsRemoteDataSource;

/**
 * Created by mingyue on 2017/6/6.
 */

public class Injection {

//    public static TasksRepository provideTasksRepository(@NonNull Context context) {
//        checkNotNull(context);
//        return TasksRepository.getInstance(FakeTasksRemoteDataSource.getInstance(),
//                TasksLocalDataSource.getInstance(context));
//    }

    public static ContactsRepository provideContactsRepository(@NonNull Context context){
        return ContactsRepository.getInstance(ContactsRemoteDataSource.getInstance());
    }
}
