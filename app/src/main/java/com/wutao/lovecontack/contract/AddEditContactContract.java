package com.wutao.lovecontack.contract;

import com.wutao.lovecontack.base.BasePresenter;
import com.wutao.lovecontack.base.BaseView;
import com.wutao.lovecontack.model.ContactBean;

import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/5/21.
 */

public interface AddEditContactContract {

    interface  View extends BaseView<Presenter>{

        void setName(ContactBean contactBean);

        void setNumber(ContactBean contactBean);

        void setmPhotoPath(ContactBean contactBean);

        boolean isActive();

        void showEmptyContactError();

        void showContactsList();

        void setLoadingIndicator(boolean active);

    }

    interface Presenter extends BasePresenter{
        void createContact(ContactDao contactDao, ContactBean contactBean);
        void saveContact(ContactDao contactDao,ContactBean contactBean);
    }
}
