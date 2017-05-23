package com.wutao.lovecontack.contract;

import com.wutao.lovecontack.base.BasePresenter;
import com.wutao.lovecontack.base.BaseView;
import com.wutao.lovecontack.model.ContactBean;

import java.util.List;

/**
 * Created by mingyue on 2017/5/21.
 */

public interface ContactContract {

    /**
     *  所有view中的方法在该类中进行统一管理
     */
    interface View extends BaseView<BasePresenter>{

        void setLoadingIndicator(boolean active);//设置 加载的小圆圈
        void showLoadingTasksError();
        boolean isActive(); //View是否已经被销毁
        void showContacts(List<ContactBean> contactBeanList);
        void showNoContacts();
        void showAddNewContact();

    }


    interface Presenter extends BasePresenter{
        void addNewContact();
    }
}
