package com.wutao.lovecontack.contract;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.wutao.lovecontack.base.BasePresenter;
import com.wutao.lovecontack.base.BaseView;
import com.wutao.lovecontack.model.ContactBean;

import java.util.List;

import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/5/21.
 */

public interface ContactContract {

    /**
     *  所有view中的方法在该类中进行统一管理
     */
    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);//设置 加载的小圆圈
        void showLoadingTasksError();
        boolean isActive(); //View是否已经被销毁
        void showContacts(List<ContactBean> contactBeanList);
        void showNoContacts();
        void showAddNewContact();
        void deleteContact(ContactDao contactDao,ContactBean contactBean);
        void ifDeleteSuccess(ContactBean contactBean);
        void deleteFailure();
    }


    interface Presenter extends BasePresenter{
        void addNewContact();
        void deleteContact(@NonNull ContactDao contactDao, ContactBean contactBean,@NonNull Activity context);
    }
}
